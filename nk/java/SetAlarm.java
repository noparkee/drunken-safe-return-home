package com.orangeline.foregroundstudy;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

        String addroom = intent.getStringExtra("addvalue");         // 음 이게 왜 항상 addvalue가 나올까?
        String delroom = intent.getStringExtra("delvalue");

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
        Log.d("SetAlarm", "onDestroy()");
    }

    private void getMeeting(String roomid){              // getMeeting이랑 getTime은 child리스너가 더 좋을거 같은데 Meeting meeting = snapshot.getValue(Meeting.class) 이런 식으로 하면 에러
        Log.d(tag, "in getMeeting()");             // 일단은 value 리스너로 해놓긴 하는데, 그러면 삭제되거나 변경됐을 때 어카지? 흠흠 이거 고민해보자.
        Log.d(tag, roomid);                             // 잘 하면 상관없을 수도.
        final String id = roomid;

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference roomdate = mDatabase.getReference("room").child(roomid).child("date");
        roomdate.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Meeting meeting = snapshot.getValue(Meeting.class);
                Log.e(tag, "changed!!!");
                getTime(id, meeting);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getTime(final String roomid, final Meeting m){
        Log.d(tag, "in getTime()");
        Log.d(tag, roomid);

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference timeref = mDatabase.getReference("room").child(roomid).child("member").child(UserID).child("time");
        timeref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Hometime htime = snapshot.getValue(Hometime.class);

                addAlarm(Integer.parseInt(roomid), m, htime);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addAlarm(int reqcode, Meeting m, Hometime t){     // 알람 추가
        Log.d(tag, "addAlarm()");
        Log.d(tag, m.getYear() + m.getMonth() + m.getDay() + t.getHour() + t.getMinute());
        AlarmManager alarmManager = (AlarmManager)getApplicationContext().getSystemService(ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();

        Intent intent = new Intent(this, Alarm.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(this, reqcode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        calendar.set(Calendar.YEAR, Integer.parseInt(m.getYear()));
        calendar.set(Calendar.MONTH, Integer.parseInt(m.getMonth())-1);      // n월 이면 int형으론 n-1 // 즉 8월이면 int형으론 7
        calendar.set(Calendar.DATE, Integer.parseInt(m.getDay()));
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(t.getHour()));
        calendar.set(Calendar.MINUTE, Integer.parseInt(t.getMinute()));
        calendar.set(Calendar.SECOND, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);

        Intent sel = new Intent(this, SetAlarm.class);
        stopService(sel);
        stopSelf();
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

        Intent sel = new Intent(this, SetAlarm.class);
        stopService(sel);
        stopSelf();
    }
}
