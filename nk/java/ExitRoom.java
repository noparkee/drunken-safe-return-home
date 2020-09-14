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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ExitRoom extends Service {     // 방에서 나갈 때!
    String tag = "ExitRoom";
    String roomkey = "1";       // 이거는 합칠 때 방 번호 UI 에서?
    String UserID = "123";

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference exit = mDatabase.getReference("users").child(UserID).child("room");
    private DatabaseReference room = mDatabase.getReference("room");

    public ExitRoom() {
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

        room.child(roomkey).child("num").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                exit.child(roomkey).setValue(null);     // 방에서 나갈 때 유저 정보에서 방 정보 삭제

                String snum = snapshot.getValue().toString();
                int num = Integer.parseInt(snum);
                System.out.println(num);
                room.child(roomkey).child("num").setValue(--num);       // 방 정보에서 num--
                System.out.println(num);

                // 방 정보의 mem에서 123 유저도 빼야해... 어케하지? 이상한 값을 지워야하는데...
                // -- 여기 채워야 해 --
                room.child(roomkey).child("mem").child("123").setValue(null);

                if (num == 0){
                    room.child(roomkey).setValue(null);   // 방의 num이 0이 되면 방도 삭제.

                }

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
