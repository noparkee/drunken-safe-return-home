package com.orangeline.foregroundstudy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button start;
    private Button dbbtn;
    private Button send;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private ChildEventListener mChild;

    private ListView listView;
    private ArrayAdapter<String> adapter;
    List<Object> Array = new ArrayList<Object>();

    String UserID = "123";
    String phonenum;

    private final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1001;


    @Override
    protected void onStart() {
        super.onStart();
        Log.d("test", "onStart");

        // 메인 액티비티 화면으로 들어오면 서비스 종료.
        Intent intent = new Intent(getApplicationContext(), MyService.class);
        stopService(intent);

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("test","onCreate 실행");

        checkPermission();        // 퍼미션 체크 -> 없으면 허용 하도록 앱 실행할 때 퍼미션 체크

        start = findViewById(R.id.start);
        dbbtn = findViewById(R.id.db);
        send = findViewById(R.id.send);

        send.setOnClickListener(new View.OnClickListener(){     // sms 보내기
            @Override
            public void onClick(View v) {       // send 버튼 누르면 문자 보냄
                sendSms("01040550786", "소병희 님이 도착하지 못했어요! 현재 주소는: ~_~");
            }
        });


        // db 읽기
        listView = (ListView) findViewById(R.id.listviewmsg);
        initDatabase();     // 이걸 통해서 child listener를 달아주는거 같음.
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());
        listView.setAdapter(adapter);

        mReference = mDatabase.getReference("users").child(UserID); // 변경값을 확인할 child 이름
        dbbtn.setOnClickListener(new View.OnClickListener(){        // db 읽기
            @Override
            public void onClick(View v) {
                mReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                            String msg2 = messageData.getValue().toString();
                            Log.d("dbdbdbdbdbdb", msg2);
                            Array.add(msg2);
                            adapter.add(msg2);
                            // child 내에 있는 데이터만큼 반복합니다.
                        }
                        adapter.notifyDataSetChanged();
                        listView.setSelection(adapter.getCount() - 1);

                        Log.d("dbdbdbdbdb", (String) Array.get(1));
                        phonenum = (String) Array.get(1);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("test", "invisible");
        startService();     // main activity 화면 닫으면 서비스 시작
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("test", "in onDestroy");
    }

    public void startService(){
        Log.d("test", "startService 실행");

        Intent serviceIntent = new Intent(getApplicationContext(), MyService.class);
        startService(serviceIntent);
        //serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android");      // Myservice에서 name이 inputExtra인 intent를 가져오는 듯.
        //ContextCompat.startForegroundService(this, serviceIntent);
    }

    private void initDatabase() {

        mDatabase = FirebaseDatabase.getInstance();

        mReference = mDatabase.getReference();

        mChild = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

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
        };
        mReference.addChildEventListener(mChild);
    }

    private void checkPermission(){
        int permissionCheck = ContextCompat.checkSelfPermission(this,Manifest.permission.SEND_SMS);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED){      // 퍼미션 허용 되어있다면,
            Log.d("sms", "sms permission 허용 상태");
            Toast.makeText(this, "sms permission 허용 상태", Toast.LENGTH_LONG).show();
            //sendSms("01040550786", "소병희 님이 도착하지 못했어요! 현재 주소는: ~_~");
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