package com.orangeline.foregroundstudy;

import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class AutoRun extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Intent serviceIntent = new Intent(context.getApplicationContext(), MyService.class);
            if (Build.VERSION.SDK_INT >= 26) {
                context.startForegroundService(serviceIntent);
            }
            else{
                context.startService(serviceIntent);
            }
        }
    }
}
