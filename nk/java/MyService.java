package com.orangeline.foregroundstudy;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyService extends Service {        // 기본 개인 세팅?
    private static final String CHANNEL_ID = "ForegroundServiceChannel";

    int a = 2;
    Notification notification;
    //String UserID = "123";


    public MyService() {
        Log.d("service", "in Myservice");
    }

    @Override
    public void onCreate() {    // 서비스가 처음 생성됐을 때 호출 됨. 이미 서비스가 실행 중이면 호출하지 않음
        super.onCreate();
        Log.d("service", "in onCreate");

        // 채널 생성
        createNotificationChannel();    // 여기까지는 채널 생성


        // 포그라운드 알림 설정 //
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("개집찾")
                .setContentText("안전한 귀가를 위해 앱이 실행 중 입니다.")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                //.setPriority(Notification.PRIORITY_MAX)
                .build();

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference dataref = mDatabase.getReference("users").child("123").child("room").child("0");
        dataref.addChildEventListener(new ChildEventListener() {        // 리스너 달고
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                String state = snapshot.getValue().toString();
                System.out.println(state);
                System.out.println("---");
                System.out.println(snapshot);

                //System.out.println(previousChildName);
                if (state.equals("3")){
                    stopfunction();
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

        startForeground(1, notification);

        Log.d("test", "start F");



    }

    public void stopfunction(){
        Log.d("test", "stop FFF");
        stopForeground(true);
        stopSelf();
    }


    @Override
    public IBinder onBind(Intent intent) {
        Log.d("service", "in onBind");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {  // 서비스 호출할 때 마다.
        Log.d("service", "in onStartCommand");

        return START_STICKY;
    }

    private void createNotificationChannel() {      // 알림 채널 만들기
        Log.d("service", "createNotificationChannel() called");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_LOW
            );

            NotificationManager manager = (NotificationManager) getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("service", "서비스의 onDestroy() 호출");
        Intent setalarmservice = new Intent(this, SetAlarm.class);
        stopService(setalarmservice);
        stopForeground(true);
        stopSelf();
    }

}
