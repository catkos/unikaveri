package com.example.unikaveri;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

/**
 * BroadcastReceiver for app's notifications and foreground service.
 * @author Kerttu
 */
public class AlarmReceiver extends BroadcastReceiver {
    /**
     * Create notification or foreground service depending on the Intent's "ID" intExtra.
     * If it's 1: create foreground service, or else create notification.
     * @param context Context
     * @param intent Intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        // Get extras from intent
        int ID = intent.getIntExtra("ID",0);
        String title = intent.getStringExtra("title");
        String message = intent.getStringExtra("message");

        // Create intent from AlarmService and add extras
        Intent intentService = new Intent(context, AlarmService.class);
        intentService.putExtra("ID", ID);
        intentService.putExtra("title", title);
        intentService.putExtra("message", message);

        // Start foreground service
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService);
        } else {
            context.startService(intent);
        }
    }
}
