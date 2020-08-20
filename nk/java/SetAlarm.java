package com.orangeline.foregroundstudy;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;


public class SetAlarm extends Service {

    String tag = "SetAlarm";
    String UserID = "123";

    public SetAlarm() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(tag, "onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(tag, "onStartCommand()");

        String addroom = intent.getStringExtra("addkey");
        String delroom = intent.getStringExtra("delkey");

        Log.d(tag, "전달받은 데이터\naddroom: " + addroom + " delroom: " + delroom);

        if (addroom != null){
            Log.d(tag, "not null");

            getMeeting(addroom);
        }
        else if(delroom != null){
            delAlarm(Integer.parseInt(delroom));
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
        Log.d("SetAlarm", "onDestroy()");
    }

    private void getMeeting(final String roomid){              // getMeeting이랑 getTime은 child리스너가 더 좋을거 같은데 Meeting meeting = snapshot.getValue(Meeting.class) 이런 식으로 하면 에러
        Log.d(tag, "in getMeeting()");             // 일단은 value 리스너로 해놓긴 하는데, 그러면 삭제되거나 변경됐을 때 어카지? 흠흠 이거 고민해보자.
        Log.d(tag, roomid);                             // 잘 하면 상관없을 수도.

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference roomdate = mDatabase.getReference("users").child(UserID).child("room").child(roomid).child("arrtime");
        /*roomdate.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String meeting = snapshot.getValue().toString();
                //LocalDateTime meettime = LocalDateTime.parse(meeting);      // T가 있을 때
                LocalDateTime meettime = LocalDateTime.parse(meeting, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));    // T가 없을 때
                Log.e(tag, "added!!!: " + meeting);

                addAlarm(Integer.parseInt(roomid), meettime);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String meeting = snapshot.getValue().toString();
                //LocalDateTime meettime = LocalDateTime.parse(meeting);
                LocalDateTime meettime = LocalDateTime.parse(meeting, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));    // T가 없을 때
                Log.e(tag, "changed!!!: " + meeting);

                addAlarm(Integer.parseInt(roomid), meettime);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
        roomdate.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String meeting = snapshot.getValue().toString();
                //LocalDateTime meettime = LocalDateTime.parse(meeting);
                LocalDateTime meettime = LocalDateTime.parse(meeting, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));    // T가 없을 때

                Log.e(tag, "changed!!!: " + meettime);
                System.out.println(meettime.getClass().getName());
                addAlarm(Integer.parseInt(roomid), meettime);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addAlarm(int reqcode, LocalDateTime t){     // 알람 추가
        Log.d(tag, "addAlarm()");
        System.out.println(t.getHour() + "시 "+ t.getMinute() + "분" + t.getYear() + "년" + t.getMonthValue() + "월" + t.getDayOfMonth()+"일");
        System.out.println(t);

        if (t.isAfter(LocalDateTime.now())) {
            Log.d(tag, "알람 추가하자!");
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            Calendar calendar = Calendar.getInstance();

            Intent intent = new Intent(this, Alarm.class);
            PendingIntent pIntent = PendingIntent.getBroadcast(this, reqcode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            calendar.set(Calendar.YEAR, t.getYear());
            calendar.set(Calendar.MONTH, t.getMonthValue());      // n월 이면 int형으론 n-1 // 즉 8월이면 int형으론 7
            calendar.set(Calendar.DATE, t.getDayOfMonth());
            calendar.set(Calendar.HOUR_OF_DAY, t.getHour());
            calendar.set(Calendar.MINUTE, t.getMinute());

            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);
        }

        //stopSelf();
    }

    private void delAlarm(int reqcode){     // 알람 삭제
        Log.d(tag, "delAlarm()");

        AlarmManager alarmManager = (AlarmManager)getApplicationContext().getSystemService(ALARM_SERVICE);

        Intent intent = new Intent(this, Alarm.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(this, reqcode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (pIntent != null){
            alarmManager.cancel(pIntent);
            pIntent.cancel();
        }
    }

}
