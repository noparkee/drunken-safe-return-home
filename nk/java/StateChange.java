package com.orangeline.foregroundstudy;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StateChange extends Service {
    String tag = "StateChange";
    String UserID = "123";

    NotificationChannel channel;
    NotificationManager notificationManager;
    PendingIntent fullScreenPendingIntent;
    Intent fullScreenIntent;
    NotificationCompat.Builder builder;

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();            /* 유저가 있는 방이 생기고, 없어질 때마다 알람 추가 및 삭제하도록 setalarm 서비스로 연결*/

    public StateChange() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createHeadsUpNotificationChannel();     // heads up 채널 생성

        // heads up 알림 설정 //
        fullScreenIntent = new Intent(this, StateChange.class);       // MainActivity로 하면 무한 실행됨.
        fullScreenPendingIntent = PendingIntent.getActivity(this, 0, fullScreenIntent, 0);
        builder = new NotificationCompat.Builder(this, "HeadsUpNotificationChannel")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentTitle("개도 집은 찾아간다")
                .setContentIntent(fullScreenPendingIntent)
                .setFullScreenIntent(fullScreenPendingIntent, true)
                .setAutoCancel(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String roomid = intent.getStringExtra("roomid");
        DatabaseReference stateref = mDatabase.getReference("room").child(roomid).child("state");

        stateref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String stateuser = snapshot.getValue().toString();
                int intstateuser = Integer.parseInt(stateuser);
                String usernickname = snapshot.getKey(); // -> 이거를 지금은 카카오 아이디인데, 이거를 닉네임으로 바꿔야해ㅠ


                System.out.println(stateuser);
                System.out.println(usernickname);
                if (usernickname.equals(UserID)) {
                    builder.setContentText(usernickname + "님이 집 도착을 하지 못했어요");
                    notificationManager.notify(intstateuser, builder.build());
                }
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
        });

        return START_STICKY;
    }

    private void createHeadsUpNotificationChannel() {   // 헤드업 알림 채널 만들기
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel("HeadsUpNotificationChannel",
                    "HeadsUp Notification Channel",
                    NotificationManager.IMPORTANCE_HIGH);
            Log.d("service", "in createHeadsUpNotificationChannel()");
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
