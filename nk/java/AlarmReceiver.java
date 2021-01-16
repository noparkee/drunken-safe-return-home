package com.orangeline.foregroundstudy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        System.out.println("Wowwwwwwwwwww Alarm!!!!!!!!!!!!!!!!");
        //throw new UnsupportedOperationException("Not yet implemented");
    }
}
