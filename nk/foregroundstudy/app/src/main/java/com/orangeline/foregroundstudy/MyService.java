package com.orangeline.foregroundstudy;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import org.apache.http.conn.ConnectTimeoutException;

import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {
    private static final String CHANNEL_ID = "ForegroundServiceChannel";

    NotificationChannel channel;
    NotificationManager notificationManager;
    NotificationCompat.Builder builder;
    int a = 2;
    Notification notification;

    PendingIntent fullScreenPendingIntent;
    Intent fullScreenIntent;

    Toast toast;

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("test", "onCreate() called");

        createNotificationChannel();    // 여기까지는 채널 생성
        createHeadsUpNotificationChannel();     // heads up 채널 생성

        // 포그라운드 알림 설정 //
        Intent notificationIntent = new Intent(this, MyService.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        //String input = intent.getStringExtra("inputExtra");
        notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("술기로운 귀가생활")
                .setContentText("안전한 귀가를 위해 앱이 실행 중 입니다.")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_MAX)
                .build();


        // heads up 알림 설정 //
        fullScreenIntent = new Intent(this, MyService.class);       // MainActivity로 하면 무한 실행됨.
        fullScreenPendingIntent = PendingIntent.getActivity(this, 0,
                fullScreenIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        builder = new NotificationCompat.Builder(this, "HeadsUpNotificationChannel")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("술기로운 귀가생활")
                .setContentText("집 도착을 하지 못했어요")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setFullScreenIntent(fullScreenPendingIntent, true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);


        Context context = getApplicationContext();
        CharSequence text = "Hello toast!";
        int duration = Toast.LENGTH_SHORT;
        toast = Toast.makeText(context, text, duration);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("test","이게 잘 나오는지 한번 보자!!!!");
        Timer timer = new Timer();

        TimerTask TT = new TimerTask() {
            @Override
            public void run() {
                Log.d("test", "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                toast.show();

                notificationManager.notify(a++, builder.build());

            }

        };


        timer.schedule(TT, 0, 10000); //Timer 실행


        startForeground(1, notification);

        return START_NOT_STICKY;
    }

    private void createNotificationChannel() {      // 알림 채널 만들기
        Log.d("test", "createNotificationChannel() called");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = (NotificationManager) getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }


    private void createHeadsUpNotificationChannel() {   // 헤드업 알림 채널 만들기
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel("HeadsUpNotificationChannel",
                    "HeadsUp Notification Channel",
                    NotificationManager.IMPORTANCE_HIGH);
            Log.d("asdf", "in createHeadsUpNotificationChannel()");
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
