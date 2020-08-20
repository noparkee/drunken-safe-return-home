package com.orangeline.foregroundstudy;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ReadUserDatabase extends Service {
    String tag = "ReadUserDataBase";
    String UserID ="123";

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();            /* 유저가 있는 방이 생기고, 없어질 때마다 알람 추가 및 삭제하도록 setalarm 서비스로 연결*/
    private DatabaseReference userref = mDatabase.getReference("users").child(UserID).child("room");

    public ReadUserDatabase() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(tag, "in onCreate()");
        userref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {      // user가 있는 방이 추가 됐을 때
                //Log.e("db", "onChildAdded: " + snapshot.getValue().toString());
                Log.e("db", "onChildAdded: " + snapshot.getKey());

                Intent setalarmservice = new Intent(getApplicationContext(), SetAlarm.class);
                //setalarmservice.putExtra("addvalue", snapshot.getValue().toString());
                setalarmservice.putExtra("addkey", snapshot.getKey());
                startService(setalarmservice);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {        // 사용자가 집 가는 시간이 변경 됐을 때
                //Log.e("db", "onChildChanged: " + snapshot.getValue().toString());
                Log.e("db", "onChildChanged: " + snapshot.getKey());

                Intent setalarmservice = new Intent(getApplicationContext(), SetAlarm.class);
                //setalarmservice.putExtra("addvalue", snapshot.getValue().toString());
                setalarmservice.putExtra("addkey", snapshot.getKey());
                startService(setalarmservice);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {        // user가 있는 방이 없어졌을 때
                //Log.e("db", "onChildRemoved: " + snapshot.getValue().toString());
                Log.e("db", "onChildRemoved: " + snapshot.getKey());

                Intent setalarmservice = new Intent(getApplicationContext(), SetAlarm.class);
                //setalarmservice.putExtra("delvalue", snapshot.getValue().toString());
                setalarmservice.putExtra("delkey", snapshot.getKey());
                startService(setalarmservice);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.e("db", "onChildMoved: " + snapshot.getValue().toString());
                Log.e("db", "onChildMoved: " + snapshot.getKey());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("db", "왜 못 읽지?!?!");
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
