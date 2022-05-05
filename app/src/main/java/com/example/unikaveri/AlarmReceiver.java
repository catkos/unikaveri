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
 *
 * Used https://learntodroid.com/how-to-create-a-simple-alarm-clock-app-in-android/ and
 * Android Documentation for implementing AlarmManager and Foreground service.
 * + numerous StackOverflow conversations.
 *
 * @author Kerttu
 */
public class AlarmReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "SLEEP_ALARM_CHANNEL";

    /**
     * Create notification or foreground service depending on the Intent's "ID" intExtra.
     *
     * @param context Context
     * @param intent Intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        // Get extras from intent
        int ID = intent.getIntExtra("ID",0);
        String title = intent.getStringExtra("title");
        String message = intent.getStringExtra("message");

        // If the ID is 1, start foreground service for alarm
        if (ID == 1) {
            // Create intent from AlarmService and add extras
            Intent intentService = new Intent(context, AlarmService.class);
            intentService.putExtra("ID", ID);
            intentService.putExtra("title", title);
            intentService.putExtra("message", message);

            // Start foreground service
            // For Android SDK 26 and up
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intentService);
            } else {
                context.startService(intentService);
            }
        } else {
            // Call MainActivity when notification is clicked
            Intent setAlarmIntent = new Intent(context, MainActivity.class);

            // Create PendingIntent
            PendingIntent contentIntent = PendingIntent.getActivity(
                    context,
                    ID,
                    setAlarmIntent,
                    PendingIntent.FLAG_CANCEL_CURRENT);

            // NotificationManager
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            // For Android SDK 26 and up
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
            context.startService(intent);
        }
    }
}
