package com.example.unikaveri;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;

/**
 * AlarmService foreground service for alarm to wake up user.
 * @author Kerttu
 */
public class AlarmService extends Service {

    private static String CHANNEL;

    private MediaPlayer mediaPlayer;

    /**
     * On create: Create mediaPlayer with system's default alarm sound and set it looping.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, Settings.System.DEFAULT_ALARM_ALERT_URI);
        mediaPlayer.setLooping(true);
    }

    /**
     * Create notification and start foreground and mediaplayer.
     * @param intent Intent
     * @param flags int
     * @param startId StartId
     * @return START_STICKY
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Get intent extras
        int ID = intent.getIntExtra("ID",0);
        String title = intent.getStringExtra("title");
        String message = intent.getStringExtra("message");

        if (ID == 1) {
            CHANNEL = "ALARM_CHANNEL";
        } else {
            CHANNEL = "SLEEP_ALARM_CHANNEL";
        }

        // Call AlarmActivity when notification is clicked
        Intent notificationIntent = new Intent(this, AlarmActivity.class);
        notificationIntent.putExtra("message", message);

        // Create PendingIntent
        PendingIntent pendingIntent = PendingIntent.getActivity(this, ID, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        // Prepare notification
        Notification notification = new NotificationCompat.Builder(this, CHANNEL)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_baseline_alarm)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);
        mediaPlayer.start();

        return START_STICKY;
    }

    /**
     * Stop mediaPlayer.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}