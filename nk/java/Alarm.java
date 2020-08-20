package com.orangeline.foregroundstudy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class Alarm extends BroadcastReceiver {
    private String tag = "in Alarm";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Log.d(tag, "in onReceive");
        throw new UnsupportedOperationException("Not yet implemented");

    }
}
