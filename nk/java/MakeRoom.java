package com.orangeline.foregroundstudy;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MakeRoom extends Service {     // 방을 만들어서 방을 처음 만든 사람은 방에 들어가있는 상태일 때
    String UserID = "123";
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference roomkey = mDatabase.getReference("key");

    public MakeRoom() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        roomkey.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference makeroom = db.getReference();

                String key = snapshot.getValue().toString();        // key 아이디 받아옴. 방키
                int numkey = Integer.parseInt(key);

                makeroom.child("room").child(key).child("date").setValue(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))); // 방 추가
                makeroom.child("room").child(key).child("location").setValue("Seocho");
                makeroom.child("room").child(key).child("mem").child("123").child("arrtime").setValue(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                makeroom.child("room").child(key).child("mem").child("123").child("deptime").setValue(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                makeroom.child("room").child(key).child("num").setValue(1);     // 방을 만들 때는 인원 1

                makeroom.child("users").child("123").child("room").child(key).child("arrtime").setValue(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                makeroom.child("users").child("123").child("room").child(key).child("deptime").setValue(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                // "123" 이거는 카카오 get id 하면 될거 같음.
                roomkey.setValue(++numkey);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}


