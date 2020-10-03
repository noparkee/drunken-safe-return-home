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

import java.util.HashMap;

public class ReadRoomid extends Service {
    String tag = "ReadRoomid";

    public ReadRoomid() {
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
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String UserID = intent.getStringExtra("UserID");
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

        // 유저가 들어 있는 room 정보 읽기
        DatabaseReference userroomidref = mDatabase.getReference("users").child(UserID).child("room");
        // 전체 방 정보
        DatabaseReference roomidref = mDatabase.getReference("room");

        userroomidref.addChildEventListener(new ChildEventListener() {      // 유저가 현재 들어가 있는 방에 대해서 하나 씩
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.e(tag, "onChildAdded: " + snapshot.getValue().toString());
                Log.e(tag, "onChildAdded: " + snapshot.getKey());

                /*HashMap<String, String> map;
                map = (HashMap<String, String>) snapshot.getValue();
                System.out.println(map.get("deptime"));*/

                // 새로 생성된 방의 id를 ReadRoomDatabase로 넘김 - makeroom에서 방 추가 되면 이게 추가되니까.
                Intent readroomdb = new Intent(getApplicationContext(), ReadRoomDatabase.class);
                readroomdb.putExtra("roomid", snapshot.getKey());
                startService(readroomdb);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.e(tag, "onChildChanged: " + snapshot.getValue().toString());
                Log.e(tag, "onChildChanged: " + snapshot.getKey());

                // 변경이 있는 방의 id를 ReadRoomDatabase로 넘김
                Intent readroomdb = new Intent(getApplicationContext(), ReadRoomDatabase.class);
                readroomdb.putExtra("roomid", snapshot.getKey());
                startService(readroomdb);
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



        /*roomidref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Intent statechange = new Intent(getApplicationContext(), StateChange.class);
                statechange.putExtra("roomid", snapshot.getKey());
                startService(statechange);
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
        });*/

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        //Intent rrdb = new Intent(getApplicationContext(), ReadRoomDatabase.class);
        //stopService(rrdb);
        super.onDestroy();
    }
}
