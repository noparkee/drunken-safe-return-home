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

public class ReadRoomid extends Service {     // 유저가 들어 있는 room 정보 읽기
    String tag = "ReadRoomid";
    String UserID = "123";
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference roomidref = mDatabase.getReference("users").child(UserID).child("room");
    static String room;
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
        roomidref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.e(tag, "onChildAdded: " + snapshot.getValue().toString());
                Log.e(tag, "onChildAdded: " + snapshot.getKey());

                // 새로 생성된 방의 id를 ReadRoomDatabase로 넘김
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
                Log.e(tag, "onChildRemoved: " + snapshot.getValue().toString());
                Log.e(tag, "onChildRemoved: " + snapshot.getKey());

                Intent deltedb = new Intent(getApplicationContext(), DeleteDatabase.class);
                deltedb.putExtra("delroomid", snapshot.getKey());
                startService(deltedb);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Intent rrdb = new Intent(getApplicationContext(), ReadRoomDatabase.class);
        stopService(rrdb);
        super.onDestroy();
    }
}
