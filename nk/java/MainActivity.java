package com.orangeline.foregroundstudy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainActivity extends AppCompatActivity {
    String UserID = "123";
    private Button enterroom;
    private Button dbbtn;
    private Button makeroom;
    private Button test;
    private Button set;
    private Button roomdb;

    private String date;
    private String loc;
    private int roomid = 0;

    private final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1001;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    private int a = 4;

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("test", "onStart");

        startService();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("test","onCreate 실행");

        checkPermission();        // 퍼미션 체크 -> 없으면 허용 하도록 앱 실행할 때 퍼미션 체크

        enterroom = findViewById(R.id.enterroom);
        dbbtn = findViewById(R.id.db);
        makeroom = findViewById(R.id.makeroom);
        test = findViewById(R.id.test);
        set = findViewById(R.id.set);
        roomdb = findViewById(R.id.roomdb);

        set.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                date = "2020-08-21 16:00";
                loc = "Anam";
            }
        });

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {       // db에 추가! 유저부분에

                databaseReference.child("users").child("123").child("room").child(String.valueOf(roomid)).child("arrtime").setValue(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                databaseReference.child("users").child("123").child("room").child(String.valueOf(roomid)).child("deptime").setValue(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

                databaseReference.child("room").child(String.valueOf(roomid)).child("date").setValue(date);
                databaseReference.child("room").child(String.valueOf(roomid)).child("location").setValue(loc);

                roomid++;
            }
        });


        dbbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent database = new Intent(getApplicationContext(), ReadUserDatabase.class);
                startService(database);
            }
        });

        makeroom.setOnClickListener(new View.OnClickListener(){     // 방 만들었을 때의 동작 즉, 방을 처음 만든 사람의 동작
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MakeRoom.class);
                startService(intent);
            }
        });

        enterroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {         // 방에 들어갔을 때의 동작
                Intent intent = new Intent(getApplicationContext(), EnterRoom.class);
                startService(intent);
            }
        });

        roomdb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ReadRoomid.class);
                startService(intent);
            }
        });

    }

    @Override
    protected void onPause() {      // 뒤로가기든 홈버튼이로든 호출
        super.onPause();
        Log.d("test", "onPause");
    }

    @Override
    protected void onDestroy() {        // 뒤로가기 버튼으로 종료하면 destroy 호출되지만, 홈버튼으로 종료하면 호출안됨.
        super.onDestroy();
        Log.d("test", "in onDestroy");

        // 앱 액티비티를 종료하면 실행된 서비스 종료되도록 디비관련!
        Intent rudb = new Intent(getApplicationContext(), ReadUserDatabase.class);
        Intent mr = new Intent(getApplicationContext(), MakeRoom.class);
        Intent er = new Intent(getApplicationContext(), EnterRoom.class);
        Intent rr = new Intent(getApplicationContext(), ReadRoomid.class);

        stopService(rudb);
        stopService(mr);
        stopService(er);
        stopService(rr);
    }

    

    public void startService(){
        Log.d("test", "startService 실행");

        Intent serviceIntent = new Intent(getApplicationContext(), MyService.class);
        if (Build.VERSION.SDK_INT >= 26) {
            startForegroundService(serviceIntent);
        }
        else{
            startService(serviceIntent);
        }

        //startService(serviceIntent);
        //serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android");      // Myservice에서 name이 inputExtra인 intent를 가져오는 듯.
        //ContextCompat.startForegroundService(this, serviceIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {   // checkPermission 함수에서 퍼미션 요청
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSIONS_REQUEST_SEND_SMS){        // 퍼미션 ok
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //sendSms("01040550786", "소병희 님이 도착하지 못했어요! 현재 주소는: ~_~");
                Log.d("sms", "onRequestPermissionsResult() 함수 내부");

            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)){       // 퍼미션을 거부한 적이 있다면
                    Toast.makeText(this, "문자 서비스를 원하시면 sms 권한을 허용해주세요", Toast.LENGTH_LONG).show();
                } else {        // 다시 보지 않기로 거부했다면
                    Toast.makeText(this, "SMS 권한이 허용되지 않아 보낼 수 없습니다. 설정해서 허용해주세요.", Toast.LENGTH_LONG).show();
                }

            }
        }
    }

    private void checkPermission(){
        int permissionCheck = ContextCompat.checkSelfPermission(this,Manifest.permission.SEND_SMS);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED){      // 퍼미션 허용 되어있다면,
            //Log.d("sms", "sms permission 허용 상태");
            //Toast.makeText(this, "sms permission 허용 상태", Toast.LENGTH_LONG).show();
            //별도의 알림은 없음
        }
        else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)){       // 퍼미션을 거부한 적이 있다면
                Toast.makeText(this, "sms permission 거부 상태. addPermission 문자 서비스를 원하시면 sms 권한을 허용해주세요", Toast.LENGTH_LONG).show();
                /*ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);*/

            } else {        // 퍼미션 거부한 적이 없다면 퍼미션 요청
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }
    }

    private void sendSms(String num, String msg){
        int permissionCheck = ContextCompat.checkSelfPermission(this,Manifest.permission.SEND_SMS);
        Log.d("sms", "in sendSms function");

        if (permissionCheck == PackageManager.PERMISSION_GRANTED){      // 퍼미션 허용 되어있다면,
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(num, null, msg, null, null);
        }
        else {
            Toast.makeText(this, "문자 서비스를 원하시면 설정에서 sms 권한을 허용해주세요", Toast.LENGTH_LONG).show();
        }

    }
}