package com.example.unikaveri;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;

/**
 * AlarmService foreground service for alarm to wake up user.
 *
 * Used https://learntodroid.com/how-to-create-a-simple-alarm-clock-app-in-android/ and
 * Android Documentation for implementing AlarmManager and Foreground service.
 * + numerous StackOverflow conversations.
 *
 * @author Kerttu
 */
public class AlarmService extends Service {

    private static String CHANNEL = "ALARM_CHANNEL";

    private MediaPlayer mediaPlayer;

    /**
     * Create mediaPlayer with system's default alarm sound and set it to loop.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, Settings.System.DEFAULT_ALARM_ALERT_URI);
        mediaPlayer.setLooping(true);
    }

    /**
     * Create notification and start foreground service and mediaPlayer.
     *
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

        // Call AlarmActivity when notification is clicked
        Intent setAlarmIntent = new Intent(getApplication(), AlarmActivity.class);
        setAlarmIntent.putExtra("message", message);

        // Create PendingIntent
        PendingIntent contentIntent = PendingIntent.getActivity(
                getApplication(),
                ID,
                setAlarmIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        // NotificationManager
        NotificationManager notificationManager =
                (NotificationManager) getApplication().getSystemService(Context.NOTIFICATION_SERVICE);

        // For Android SDK 26 and up
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channel_name = "My Notification";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL, channel_name, importance);
            notificationManager.createNotificationChannel(channel);
        }

        // Prepare notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplication(), CHANNEL)
                .setSmallIcon(R.drawable.ic_baseline_nights)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(contentIntent)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true);

        mediaPlayer.start();

        startForeground(1, builder.build());

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