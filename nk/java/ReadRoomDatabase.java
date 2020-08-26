package com.orangeline.foregroundstudy;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReadRoomDatabase extends Service {     // ReadRoomid에서 가져온 roomid의 date와 location 읽어옴
    String tag = "ReadRoomDatabase";

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

    public ReadRoomDatabase() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        Log.d(tag, "in onCreate()");
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String roomid = intent.getStringExtra("roomid");
        System.out.print(roomid);

        DatabaseReference dateref = mDatabase.getReference("room").child(roomid).child("date");
        DatabaseReference locationref = mDatabase.getReference("room").child(roomid).child("location");

        dateref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String date = snapshot.getValue().toString();
                Log.e(tag, "date: " + date);

                //Intent UI = new Intent(getApplicationContext(), UI.class);
                //UI.putExtra("roomid", snapshot.getvalue().toString);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        locationref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String location = snapshot.getValue().toString();
                Log.e(tag, "location " + location);

                //Intent UI = new Intent(getApplicationContext(), UI.class);
                //UI.putExtra("roomid", snapshot.getvalue().toString);
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
