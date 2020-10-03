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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainActivity extends AppCompatActivity {
    /*
    * 지금은 UserID를 MainActivity에서 정의해서 intent로 넘겨주는데,
    * 이거 합칠 때는 채원이가 카카오 정보 받아오는 곳에서 string으로 저장한 다음에
    * 거기서 intent로 서비스들 호출 하는 곳으로 불러야해!
    * */
    String UserID = "123";

    private Button enterroom;
    private Button dbbtn;
    private Button makeroom;
    private Button test;
    private Button set;
    private Button roomdb;
    private Button exit;
    private Button send;

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

        checkPermission();        // 퍼미션 체크 -> 없으면 허용 하도록 앱 실행할 때! 퍼미션 체크

        enterroom = findViewById(R.id.enterroom);
        dbbtn = findViewById(R.id.db);
        makeroom = findViewById(R.id.makeroom);
        test = findViewById(R.id.test);
        set = findViewById(R.id.set);
        roomdb = findViewById(R.id.roomdb);
        exit = findViewById(R.id.exit);
        send = findViewById(R.id.send);

        exit.setOnClickListener(new View.OnClickListener() {    // exitroom
            @Override
            public void onClick(View v) {
                Intent ex = new Intent(getApplicationContext(), ExitRoom.class);
                ex.putExtra("UserID", UserID);
                startService(ex);
            }
        });

        /*dbbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   // readuserdb

            }
        });*/

        makeroom.setOnClickListener(new View.OnClickListener(){     // 방 만들었을 때의 동작 즉, 방을 처음 만든 사람의 동작
            @Override
            public void onClick(View v) {   //makeroom
                Intent intent = new Intent(getApplicationContext(), MakeRoom.class);
                intent.putExtra("UserID", UserID);
                startService(intent);
            }
        });

        enterroom.setOnClickListener(new View.OnClickListener() {   // 입장 버튼 -> 동적링크 눌렀을 떄!
            @Override
            public void onClick(View v) {         // 방에 들어갔을 때의 동작
                Intent intent = new Intent(getApplicationContext(), EnterRoom.class);
                intent.putExtra("UserID", UserID);
                startService(intent);
            }
        });

        /*roomdb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   //readroomid

            }
        });*/

        test.setOnClickListener(new View.OnClickListener() {    // 새로운 유저 추가시
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddnewUserDB.class);
                startService(intent);
            }
        });

        send.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                sendSms("010********", "유정이 안녕");
            }
        });


        Intent readroomid = new Intent(getApplicationContext(), ReadRoomid.class);
        readroomid.putExtra("UserID", UserID);
        startService(readroomid);       // 시작 readroomid

        Intent userdatabase = new Intent(getApplicationContext(), ReadUserDatabase.class);
        userdatabase.putExtra("UserID", UserID);
        startService(userdatabase);     // 시작 readuserdb

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

    @Override // 퍼미션 허용 여부값을 return 받는 듯
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {   // checkPermission 함수에서 퍼미션 요청
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("onRequestPermissionResult", "!!!!!!!!!!!!!!!!!!!");
        if (requestCode == MY_PERMISSIONS_REQUEST_SEND_SMS) {        // 퍼미션 ok
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("sms", "onRequestPermissionsResult() 함수 내부");
                Toast.makeText(this, "sms 권한을 허용하셨습니다.", Toast.LENGTH_LONG).show();

            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {       // 퍼미션을 거부한 적이 있다면
                    Toast.makeText(this, "sms 권한을 거부하셨습니다.", Toast.LENGTH_LONG).show();
                } else {        // 다시 보지 않기로 거부했다면
                    Toast.makeText(this, "sms 권한을 거부하셨습니다. 문자 서비스를 이용하시려면 설정에서 sms 권한을 허용해주세요.", Toast.LENGTH_LONG).show();
                }

            }
        }
    }

    private void checkPermission(){
        Log.e("checkPermission", "!!!!!!!!!!!!!!!!!!!");
        int permissionCheck = ContextCompat.checkSelfPermission(this,Manifest.permission.SEND_SMS);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED){      // 퍼미션 허용 되어있지 않으면,
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)){       // 퍼미션을 거부한 적이 있다면
                Toast.makeText(this, "문자 서비스를 이용하시려면 sms 권한이 필요합니다. sms 권한을 허용해주세요.", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);

            } else {        // 퍼미션 거부한 적이 없다면 퍼미션 요청
                Toast.makeText(this, "문자 서비스를 이용하시려면 sms 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }
    }

    private void sendSms(String num, String msg){
        Log.e("sendSms", "!!!!!!!!!!!!!!!!!!!");
        int permissionCheck = ContextCompat.checkSelfPermission(this,Manifest.permission.SEND_SMS);
        Log.d("sms", "in sendSms function");

        if (permissionCheck == PackageManager.PERMISSION_GRANTED){      // 퍼미션 허용 되어있다면,
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(num, null, msg, null, null);
        }
        else {
            Toast.makeText(this, "sms 권한이 없습니다. 문자 서비스를 이용하시려면 sms 권한이 필요합니다.", Toast.LENGTH_LONG).show();
        }

    }
}