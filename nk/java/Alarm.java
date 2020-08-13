package com.orangeline.foregroundstudy;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Alarm extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "알람~!!", Toast.LENGTH_SHORT).show();    // AVD 확인용
        Log.e("Alarm", "알람입니다.");    // 로그 확인용
        Intent addalarm = new Intent(context.getApplicationContext(), AddAlarm.class);
        context.startService(addalarm);
        /*Intent serviceIntent = new Intent(context.getApplicationContext(), MyService.class);
        if (Build.VERSION.SDK_INT >= 26) {
            context.startForegroundService(serviceIntent);
        }
        else{
            context.startService(serviceIntent);
        }*/

    }

}
