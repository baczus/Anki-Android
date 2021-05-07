package com.ichi2.anki.services.reviewer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import com.ichi2.anki.DeckPicker;
import com.ichi2.anki.R;

import androidx.core.app.NotificationCompat;

public class AutoRunReviewerService extends Service {

    BroadcastReceiver mReceiver;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        mReceiver = new ScreenOnEventReceiver();
        registerReceiver(mReceiver, filter);
        startServiceForeground(intent, flags, startId);

        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }


    private void startServiceForeground(Intent intent, int flags, int startId) {
        Intent notificationIntent = new Intent(this, DeckPicker.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        initChannels(this);

        Notification notification = new NotificationCompat.Builder(this, "ANKI_CHANNEL_ID")
                .setContentTitle("AnkiDroid")
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .setSmallIcon(R.drawable.ankidroid_logo)
                .build();

        startForeground(300, notification);
    }


    private void initChannels(Context context) {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("ANKI_CHANNEL_ID",
                "AutoRunReviewerService Notification Channel",
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("Channel description");
        notificationManager.createNotificationChannel(channel);
    }


    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);
        stopForeground(true);
        super.onDestroy();
    }


    public class LocalBinder extends Binder {
        AutoRunReviewerService getService() {
            return AutoRunReviewerService.this;
        }
    }

}