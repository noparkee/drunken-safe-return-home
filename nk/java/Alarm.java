package com.orangeline.foregroundstudy;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Alarm extends BroadcastReceiver {      // 알람 울리도록, AddAlarm에서 만든 알람 시간이 되면 Alarm에서 알람 울림.
    String userid = "123";
    String tag = "in Alarm class";
    @Override
    public void onReceive(final Context context, Intent intent) {
        if(intent.getAction().equals("android.intent.action.ALARM_START")) {
            Log.e(tag, "in onReceive");
        }

    }
}
