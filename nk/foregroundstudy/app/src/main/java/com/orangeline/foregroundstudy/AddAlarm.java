package com.orangeline.foregroundstudy;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddAlarm extends Service {
    private FirebaseDatabase mDatabase;
    private DatabaseReference timeref;
    private DatabaseReference dateref;

    String UserID = "123";

    //private ArrayAdapter<String> adapter;
    List<String> Array = new ArrayList<>();

    NotificationChannel channel;
    NotificationManager notificationManager;

    AlarmManager alarmManager = (AlarmManager)getApplicationContext().getSystemService(ALARM_SERVICE);
    Calendar calendar = Calendar.getInstance();
    int a = 0;

    /* 시간 알람 >>>> 이건 테스트용이니까.
            실제로 만들 때는 처음에 앱 실행하면 알람 보내고, 알람 리시버로 db 접근해서 정보 가져오게 하기.
            그 뒤로는 하루에 한 번 씩으로 울리게 해서 정보 하루에 한 번 씩 가져오게.
            정보 가져와서 알림 추가하는 것까지.
            일단 MyService에서 알람 시간이 되면 Alarm.class 열고, 거기에서 알람 추가하는 서비스로 연결하면 될 듯.
            그래서 그 알람 추가하는 서비스로 이동하면 거기서 알람 추가. 요거요거 하면 될 듯.*/
    public AddAlarm() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());
        //hour = Integer.parseInt((String) Array.get(0));
        //minute = Integer.parseInt((String) Array.get(1));
        mDatabase = FirebaseDatabase.getInstance();
        timeref = mDatabase.getReference("room/0812001/member").child(UserID).child("time"); // 변경값을 확인할 child 이름    /*여기서 경로는 나중에 쿼리 추가해서 해야함
        timeref.addValueEventListener(new ValueEventListener() {                                                                 // 그날 당일인거 추가하기. 추가한 약속은 flag 넣던가 하면될듯.
            @Override                                                                                                               // n월m일이면, n월md일에 약속있는 방 찾는 쿼리. */
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                    String msg = messageData.getValue().toString();
                    Array.add(msg);
                    // child 내에 있는 데이터만큼 반복합니다.
                }
                //adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        dateref = mDatabase.getReference("room/0812001/date");
        dateref.addValueEventListener(new ValueEventListener() {                                                                 // 그날 당일인거 추가하기. 추가한 약속은 flag 넣던가 하면될듯.
            @Override                                                                                                               // n월m일이면, n월md일에 약속있는 방 찾는 쿼리. */
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                    String msg = messageData.getValue().toString();
                    Array.add(msg);
                    // child 내에 있는 데이터만큼 반복합니다.
                }
                //adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return START_STICKY;
    }

    private void addAlarm(int month, int date, int hour, int minute){

        Intent intent = new Intent(this, Alarm.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(this, a++, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //calendar.set(Calendar.YEAR, 2020);
        calendar.set(Calendar.MONTH, Calendar.AUGUST);      // n월 이면 int형으론 n-1 // 즉 8월이면 int형으론 7
        calendar.set(Calendar.DATE, 14);
        calendar.set(Calendar.HOUR_OF_DAY, 3);
        calendar.set(Calendar.MINUTE, 19);
        calendar.set(Calendar.SECOND, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);

    }

}
