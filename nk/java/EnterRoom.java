package com.orangeline.foregroundstudy;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EnterRoom extends Service {    // 방에 초대된 후 수락해서 방에 들어갈 때
    String UserID = "123";
    String tag = "EnterRoom";

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference enterroom = db.getReference();

    public EnterRoom() {
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


        enterroom.child("room").child("1").child("num").addListenerForSingleValueEvent(new ValueEventListener() {   // 현재 인원 수 읽어오기
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.e(tag, snapshot.getValue().toString());
                String snum = snapshot.getValue().toString();
                int num = Integer.parseInt(snum);
                System.out.println(num);

                //enterroom.child("room").child("1").child("mem").push().setValue("789"); // 방에 들어가면 room의 mem에 추가. 여기서 "789"는 다른 사람 id 일단 임의로.
                enterroom.child("room").child("1").child("mem").child("789").child("arrtime").setValue(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                enterroom.child("room").child("1").child("mem").child("789").child("deptime").setValue(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                enterroom.child("room").child("1").child("num").setValue(++num);

                enterroom.child("users").child("789").child("room").child("1").child("arrtime").setValue(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                enterroom.child("users").child("789").child("room").child("1").child("deptime").setValue(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                // 여기서 "1"은 roomid인데, 링크 누르면 방 아이디가 같이 넘어가야할 듯.
                // 링크에 방 번호가 있어야해.
                // 여기서는 789가 입장하는거 실제 코드에선 하나의 userid만 있으면 됨.
                // 그니까 실제로는 만약 아이디가 kkk라면 enter도, make에서도 디비에 쓰여지느 ㄴuserid는 kkk

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
