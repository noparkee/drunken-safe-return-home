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

import androidx.core.app.NotificationCompat;

public class MyService extends Service {
    private static final String CHANNEL_ID = "ForegroundServiceChannel";

    NotificationChannel channel;
    NotificationManager notificationManager;
    NotificationCompat.Builder builder;
    int a = 2;
    Notification notification;

    PendingIntent fullScreenPendingIntent;
    Intent fullScreenIntent;

    //Toast toast;

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
        createHeadsUpNotificationChannel();     // heads up 채널 생성

        // 포그라운드 알림 설정 //
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("술기로운 귀가생활")
                .setContentText("안전한 귀가를 위해 앱이 실행 중 입니다.")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                //.setPriority(Notification.PRIORITY_MAX)
                .build();

        // heads up 알림 설정 //
        fullScreenIntent = new Intent(this, MyService.class);       // MainActivity로 하면 무한 실행됨.
        fullScreenPendingIntent = PendingIntent.getActivity(this, 0, fullScreenIntent, 0);
        builder = new NotificationCompat.Builder(this, "HeadsUpNotificationChannel")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("술기로운 귀가생활")
                .setContentText("집 도착을 하지 못했어요")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setFullScreenIntent(fullScreenPendingIntent, true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        // 토스트 알림 위한 기본 구성
        /*Context context = getApplicationContext();
        CharSequence text = "Hello toast!";
        int duration = Toast.LENGTH_SHORT;
        toast = Toast.makeText(context, text, duration);*/

        Log.d("service","이게 잘 나오는지 한번 보자!!!!");
        /*Timer timer = new Timer();
        TimerTask TT = new TimerTask() {
            @Override
            public void run() {
                Log.d("service", "notify" + a);
                a++;
                //toast.show();
                //notificationManager.notify(a++, builder.build());
            }
        };
        timer.schedule(TT, 0, 10000); //Timer 실행*/

        /* 시간 알람 >>>> 이건 테스트용이니까.
            실제로 만들 때는 처음에 앱 실행하면 알람 보내고, 알람 리시버로 db 접근해서 정보 가져오게 하기.
            그 뒤로는 하루에 한 번 씩으로 울리게 해서 정보 하루에 한 번 씩 가져오게.
            정보 가져와서 알림 추가하는 것까지.
            일단 MyService에서 알람 시간이 되면 Alarm.class 열고, 거기에서 알람 추가하는 서비스로 연결하면 될 듯.
            그래서 그 알람 추가하는 서비스로 이동하면 거기서 알람 추가. 요거요거 하면 될 듯.*/

        /*  db 읽기
            유저가 어느 방에 들어 있는지 확인. 어느 방에 있는지 알았다면, AddAlarm 서비스로 넘어가서 알람 추가
            만약 어떤 방이 삭제 됐다면, onChildRemoved로 인지하고, AddAlarm 서비스로 넘어가서 알람 삭제
            알람 번호는 즉 requestcode는 방 번호로 구분하자!
        */

        startForeground(1, notification);
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
        Log.d("service", "서비스의 onDestroy() 호출");
        Intent setalarmservice = new Intent(this, SetAlarm.class);
        stopService(setalarmservice);
        stopForeground(true);
        stopSelf();
    }

}
