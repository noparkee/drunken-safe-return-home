package com.orangeline.return_confirmation;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import static android.content.ContentValues.TAG;

//MainActivity에서 서비스 실행
public class StateService extends Service {
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    private double home_x;
    private double home_y;
    private double distance;
    ServiceThread thread;
    private double longitude, latitude;
    private LocationManager lm = null;

    private LocationListener ll = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {//변경시
        }
        @Override
        public void onProviderEnabled(String provider) {//활성화
        }
        @Override
        public void onProviderDisabled(String provider) {//비활성화
        }
    };

    private static String getTime() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdfNow = new SimpleDateFormat("MMddHHmmss");
        String formatDate = sdfNow.format(date);
        return formatDate;
    }

    @Override
    public void onCreate() {//가장 먼저 호출되어 서비스 생성
        super.onCreate();

        Log.e(TAG, "onCreate");
        if(lm == null) {
            lm = (LocationManager)getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
        try {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, ll); //1초 or 1미터 간격 변화 수집
        } catch (SecurityException e) {
            Log.i(TAG, "fail to request location update, ignore" + e.getMessage());
        } catch (IllegalArgumentException e) {
            Log.d(TAG, "gps provider does not exist" + e.getMessage());
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        home_x = intent.getDoubleExtra("home_x", 37.5887034223667); //집주소의 기본값을 안암캠퍼스 좌표로 설정
        home_y = intent.getDoubleExtra("home_y", 127.031698331241); //그냥 0으로 놔뒀다가 GPS 기본값이랑 맞물려서 시작하자마자 도착했다고 할 수도 있으니까...

        Toast.makeText(this, "Service starting", Toast.LENGTH_SHORT).show();

        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new Notification.Builder(this, CHANNEL_ID)
                .setContentTitle(getText(R.string.ServiceNotification_title))
                .setContentText(getText(R.string.ServiceNotification_message))
                .setContentIntent(pendingIntent)
                .setTicker(getText(R.string.ServiceTicker_text))
                .build();

        startForeground(1, notification);   //1st param = ONGOING_NOTIFICATION_ID

        //do heavy work on a background thread
        try {
            myServiceHandler handler = new myServiceHandler();
            thread = new ServiceThread(handler);
            thread.start();
        } catch (Exception ignore) { }

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(lm != null) {
            try {
                lm.removeUpdates(ll);
            } catch (Exception ignored) { }
        }
        thread.stopForever();
        thread = null;

        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    class ServiceThread extends Thread {
        Handler handler;
        boolean isRun = true;

        public ServiceThread(Handler handler) {
            this.handler = handler;
        }
        public void stopForever() {
            synchronized (this) {
                this.isRun = false;
            }
        }
        public void run() {
            while (isRun) {
                calcDistance(longitude, latitude);

                Log.e(TAG, "현재위치는 경도: " + longitude + ", 위도: " + latitude);
                if (distance > 1)
                    Log.e(TAG, "귀가지까리 남은 거리는 " + distance + "km입니다");
                else {
                    distance = distance * 1000;
                    Log.e(TAG, "귀가지까지 남은 거리는 " + distance + "m입니다");
                }

                checkHomeComing();
                try {
                    Thread.sleep(5000); //5초마다 위치 업데이트
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    class myServiceHandler extends Handler {
        //네트워크 기능?
    }

    public void calcDistance(double user_x, double user_y) {
        double theta = user_x - home_x;
        distance = Math.sin(deg2rad(user_y)) * Math.sin(deg2rad(home_y))
                    + Math.cos(deg2rad(user_y)) * Math.cos(deg2rad(home_y))  * Math.cos(deg2rad(theta));

        distance = Math.acos(distance);
        distance = rad2deg(distance) * 60 * 1.1515;
        distance = distance * 1.609344; //convert distance to kilometer unit
    }

    public void checkHomeComing() {
        //convert user's state according to distance
    }

    //convert decimal degrees to radians
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    //convert radians to decimal degrees
    private double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }
}
