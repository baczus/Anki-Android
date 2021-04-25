package com.ichi2.anki.services.reviewer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.ichi2.anki.AnkiDroidApp;

public class BootEventReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        if (AnkiDroidApp.getSharedPrefs(context).getBoolean("autoRunReviewer", false)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(new Intent(context, AutoRunReviewerService.class));
            } else {
                context.startService(new Intent(context, AutoRunReviewerService.class));
            }

        }
    }
}