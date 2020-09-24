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

public class AddnewUserDB extends Service {
    String newID = "365";
    String tag = "AddnewUserDB";
    String home;
    String phone_num;
    private FirebaseDatabase newuserdb = FirebaseDatabase.getInstance();
    private DatabaseReference newuser = newuserdb.getReference().child("users").child(newID);

    public AddnewUserDB() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(tag, "in onCreate");
        home = "jeju";
        phone_num = "01088502505";
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        newuser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                newuser.child("address").setValue(home);
                newuser.child("contact").setValue(phone_num);
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
