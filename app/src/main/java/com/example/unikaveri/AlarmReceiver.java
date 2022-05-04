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

    private static final String CHANNEL_ID = "SLEEP_ALARM_CHANNEL";

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

        if (ID == 1) {
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
        } else {
            // Call MainActivity when notification is clicked
            Intent setAlarmIntent = new Intent(context, MainActivity.class);

            // Create PendingIntent
            PendingIntent contentIntent = PendingIntent.getActivity(context, ID, setAlarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            // NotificationManager
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            // For API 26 and above
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence channel_name = "My Notification";
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channel_name, importance);
                notificationManager.createNotificationChannel(channel);
            }

            // Prepare notification
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_baseline_nights)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setContentIntent(contentIntent)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setAutoCancel(true);

            // Notify
            notificationManager.notify(ID, builder.build());
        }
    }
}
