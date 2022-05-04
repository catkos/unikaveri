package com.example.unikaveri;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Calendar;

/**
 * Alarm Object for setting AlarmManager alarms.
 * @author Kerttu
 */
public class Alarm {

    private int ID;
    private int hour;
    private int minute;

    /**
     * Define Alarm Object.
     * @param ID int - Specific ID to be used when setting alarm on AlarmManager. 1 should be used
     *           for wake time alarm to specify foreground service.
     * @param hour int - At what hour to set the alarm off.
     * @param minute int - At what minute to set the alarm off.
     */
    public Alarm(int ID, int hour, int minute) {
        this.ID = ID;
        this.hour = hour;
        this.minute = minute;
    }

    /**
     * Create alarm and set daily repeating notification.
     * @param context Context
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void createAlarm(Context context) {
        // Get reference to AlarmManager
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("ID", ID);

        if (ID == 1) {
            intent.putExtra("title", "Herätys");
            intent.putExtra("message", "On aika herätä!");
        } else {
            intent.putExtra("title", "Nukkumaanmenoaikasi lähestyy");
            intent.putExtra("message", "Valmistaudu nukkumaan tunnin päästä.");
        }

        // Create PendingIntent
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                ID,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        // Get time
        Calendar time = Calendar.getInstance();
        time.set(Calendar.HOUR_OF_DAY, this.hour);
        time.set(Calendar.MINUTE, this.minute);
        time.set(Calendar.SECOND, 0);

        // Check if time has passed and increment by one day
        if (time.getTimeInMillis() <= System.currentTimeMillis()) {
            time.set(Calendar.DAY_OF_MONTH, time.get(Calendar.DAY_OF_MONTH) + 1);
        }

        // Set repeating alarm
        // alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        // TODO: Check if find workaround to set exact alarms on interval
        //  because setRepeating is not exact and will go off on 5min range
        // Set exact alarm
         alarmManager.setExact(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), pendingIntent);
    }

    /**
     * Cancel alarm.
     * @param context Context
     */
    public void cancelAlarm(Context context) {
        // Get reference to AlarmManager
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Create intent using AlarmReceiver
        Intent intent = new Intent(context, AlarmReceiver.class);

        // Create PendingIntent that has the reference to the alarm ID to cancel
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                this.ID,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        // Cancel the PendingIntent
        alarmManager.cancel(pendingIntent);
    }
}
