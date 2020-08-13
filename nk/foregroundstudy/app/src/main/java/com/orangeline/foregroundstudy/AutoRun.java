package com.orangeline.foregroundstudy;

import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

public class AutoRun extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Intent serviceIntent = new Intent(context, MyService.class);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startService(serviceIntent);

            } else {
                context.startService(serviceIntent);
            }


        }
    }
}
