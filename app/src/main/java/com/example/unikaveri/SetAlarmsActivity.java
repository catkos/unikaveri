package com.example.unikaveri;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.Locale;

public class SetAlarmsActivity extends AppCompatActivity {

    public static final String ALARMS_PREFS = "alarms";
    public static String SLEEP_ALARM_HOUR = "sleepAlarmHour";
    public static String SLEEP_ALARM_MINUTE = "sleepAlarmMinute";
    public static String SLEEP_ALARM_IS_SET = "sleepAlarmIsSet";
    public static String WAKE_ALARM_HOUR = "wakeAlarmHour";
    public static String WAKE_ALARM_MINUTE = "wakeAlarmMinute";
    public static String WAKE_ALARM_IS_SET = "wakeAlarmIsSet";

    private TimePickerDialog sleepTimePickerDialog;
    private TimePickerDialog wakeTimePickerDialog;
    private EditText sleepTimePickerEt;
    private EditText wakeTimePickerEt;
    private Switch sleepAlarmSwitch;
    private Switch wakeAlarmSwitch;
    private Button saveBtn;

    private boolean sleepAlarmIsSet = false;
    private boolean wakeAlarmIsSet = false;
    private int sleepTimeHour;
    private int sleepTimeMinute;
    private int wakeTimeHour;
    private int wakeTimeMinute;
    private final int notificationID = 1;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarms);
        initWidgets();
        loadAlarmSettings();
        setSwitches();
        initSleepTimePickerDialog(LocalTime.of(sleepTimeHour,sleepTimeMinute));
        initWakeTimePickerDialog(LocalTime.of(wakeTimeHour,wakeTimeMinute));
    }

    private void initWidgets() {
        sleepTimePickerEt = findViewById(R.id.sleepTimePickerEditText);
        wakeTimePickerEt = findViewById(R.id.wakeTimePickerEditText);
        sleepAlarmSwitch = findViewById(R.id.sleepAlarmSwitch);
        wakeAlarmSwitch = findViewById(R.id.wakeAlarmSwitch);
        saveBtn = findViewById(R.id.saveButton);
    }

    /**
     * Initialize TimePickerDialog for choosing the time when user went to sleep.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initSleepTimePickerDialog(LocalTime time) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                sleepTimeHour = hour;
                sleepTimeMinute = minute;
                sleepTimePickerEt.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
            }
        };

        sleepTimePickerDialog = new TimePickerDialog(this, onTimeSetListener, time.getHour(), time.getMinute(),true);
        sleepTimePickerEt.setText(String.format(Locale.getDefault(), "%02d:%02d", time.getHour(), time.getMinute()));
    }

    /**
     * Initialize TimePickerDialog for choosing the time when user woke up.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initWakeTimePickerDialog(LocalTime time) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                wakeTimeHour = hour;
                wakeTimeMinute = minute;
                wakeTimePickerEt.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
            }
        };

        wakeTimePickerDialog = new TimePickerDialog(this, onTimeSetListener, time.getHour(), time.getMinute(),true);
        wakeTimePickerEt.setText(String.format(Locale.getDefault(), "%02d:%02d", time.getHour(), time.getMinute()));
    }

    private void setSwitches() {
        sleepAlarmSwitch.setChecked(sleepAlarmIsSet);
        wakeAlarmSwitch.setChecked(wakeAlarmIsSet);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setNotifications() {
        Intent intent = new Intent(SetAlarmsActivity.this, AlarmReceiver.class);
        intent.putExtra("id", 1);
        intent.putExtra("title", "Nukkumaanmenoaikasi lähestyy");
        intent.putExtra("message", "Valmistaudu nukkumaan tunnin päästä");

        // PendingIntent
        PendingIntent alarmSleepTimeIntent = PendingIntent.getBroadcast(SetAlarmsActivity.this, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Calendar sleepTime = Calendar.getInstance();
        sleepTime.set(Calendar.HOUR_OF_DAY, (sleepTimeHour - 1));
        sleepTime.set(Calendar.MINUTE, sleepTimeMinute);
        sleepTime.set(Calendar.SECOND, 0);

        // AlarmManager
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        if (sleepAlarmSwitch.isChecked()) {
            // Set repeating notification
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, sleepTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmSleepTimeIntent);
            sleepAlarmIsSet = true;
            saveAlarmSettings();
        } else {
            alarmManager.cancel(alarmSleepTimeIntent);
            sleepAlarmIsSet = false;
        }

        if (wakeAlarmSwitch.isChecked()) {
            Intent wakeIntent = new Intent(SetAlarmsActivity.this, AlarmReceiver.class);
            wakeIntent.putExtra("ID", 2);
            wakeIntent.putExtra("title", "Herätys!");
            wakeIntent.putExtra("message", "On aika herätä!");
            PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(SetAlarmsActivity.this, 2, wakeIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            Calendar wakeTime = Calendar.getInstance();
            wakeTime.set(Calendar.HOUR_OF_DAY, wakeTimeHour);
            wakeTime.set(Calendar.MINUTE, wakeTimeMinute);
            wakeTime.set(Calendar.SECOND, 0);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, wakeTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmPendingIntent);
            wakeAlarmIsSet = true;
        } else {
            wakeAlarmIsSet = false;
        }

        saveAlarmSettings();
    }

    /**
     * Buttons functionalities.
     * @param v View
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void buttonPressed(View v) {
        if (v.getId() == wakeTimePickerEt.getId()) {
            wakeTimePickerDialog.show();
        } else if (v.getId() == sleepTimePickerEt.getId()) {
            sleepTimePickerDialog.show();
        } else if (v.getId() == saveBtn.getId()) {
            setNotifications();
            finish();
        }
    }

    public void loadAlarmSettings() {
        SharedPreferences alarmPreferences = getSharedPreferences(ALARMS_PREFS, MODE_PRIVATE);
        sleepTimeHour = alarmPreferences.getInt(SLEEP_ALARM_HOUR, 22);
        sleepTimeMinute = alarmPreferences.getInt(SLEEP_ALARM_MINUTE, 30);
        sleepAlarmIsSet = alarmPreferences.getBoolean(SLEEP_ALARM_IS_SET, false);
        wakeTimeHour = alarmPreferences.getInt(WAKE_ALARM_HOUR, 7);
        wakeTimeMinute = alarmPreferences.getInt(WAKE_ALARM_MINUTE, 30);
        wakeAlarmIsSet = alarmPreferences.getBoolean(WAKE_ALARM_IS_SET, false);
    }

    public void saveAlarmSettings() {
        SharedPreferences alarmPreferences = getSharedPreferences(ALARMS_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = alarmPreferences.edit();
        editor.putInt(SLEEP_ALARM_HOUR, sleepTimeHour);
        editor.putInt(SLEEP_ALARM_MINUTE, sleepTimeMinute);
        editor.putBoolean(SLEEP_ALARM_IS_SET, sleepAlarmIsSet);
        editor.putInt(WAKE_ALARM_HOUR, wakeTimeHour);
        editor.putInt(WAKE_ALARM_MINUTE, wakeTimeMinute);
        editor.putBoolean(WAKE_ALARM_IS_SET, wakeAlarmIsSet);
        editor.apply();
    }
}