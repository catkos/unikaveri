package com.example.unikaveri;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import java.time.LocalTime;
import java.util.Locale;

/** Activity for setting alarms.
 *
 * @author Kerttu
 */
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

    private AlertDialog alertDialog;

    private boolean sleepAlarmIsSet = false;
    private boolean wakeAlarmIsSet = false;
    private int sleepTimeHour;
    private int sleepTimeMinute;
    private int wakeTimeHour;
    private int wakeTimeMinute;

    /**
     * Initialize widgets, alert dialog, get alarm settings, set switches according to if alarms
     * are set on or off and initialize time picker dialogs.
     *
     * @param savedInstanceState savedInstanceState
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarms);
        initWidgets();
        initAlertDialog();
        loadAlarmSettings();
        setSwitches();
        initSleepTimePickerDialog(LocalTime.of(sleepTimeHour,sleepTimeMinute));
        initWakeTimePickerDialog(LocalTime.of(wakeTimeHour,wakeTimeMinute));
    }

    /**
     * When user interacts with back key, open alert dialog to ask if user wants to continue without saving.
     */
    @Override
    public void onBackPressed() {
        alertDialog.show();
    }

    /**
     * When user interacts with top action bar's arrow button, open alert dialog.
     *
     * @param item The MenuItem user interacted with.
     * @return boolean
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            alertDialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Initialize alert dialog for when user wants to go back without saving.
     */
    private void initAlertDialog() {
        // Set alert builder
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("Ilmoitus");
        alertBuilder.setMessage("Haluatko varmasti poistua tallentamatta?");

        // Set positive button and add onClick listener to it:
        // if user clicks positive button, finish activity
        alertBuilder.setPositiveButton("Jatka tallentamatta", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        // Set negative button
        alertBuilder.setNegativeButton("Peruuta", null);

        // Create alert dialog
        alertDialog = alertBuilder.create();
    }

    /**
     * Initialize EditTexts, Switches and Button.
     */
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

    /**
     * Set sleepAlarmSwitch and wakeAlarmSwitch depending on sleepAlarmIsSet's and wakeAlarmIsSet's
     * values (true, false).
     */
    private void setSwitches() {
        sleepAlarmSwitch.setChecked(sleepAlarmIsSet);
        wakeAlarmSwitch.setChecked(wakeAlarmIsSet);
    }

    /**
     * Set alarm if wakeAlarmSwitch/sleepAlarmSwitch is checked. If not, cancel the alarm.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setAlarms() {
        int sleepTimeHourTmp = sleepTimeHour - 1;

        if (sleepTimeHourTmp < 0) {
            sleepTimeHourTmp = 23;
        }

        // Create alarms for wake up and sleep times
        Alarm wakeAlarm = new Alarm(1, wakeTimeHour, wakeTimeMinute);
        Alarm sleepAlarm = new Alarm(2, sleepTimeHourTmp, sleepTimeMinute);

        // If wakeAlarmSwitch is checked
        if (wakeAlarmSwitch.isChecked()) {
            wakeAlarm.createAlarm(SetAlarmsActivity.this);
            wakeAlarmIsSet = true;
        } else {
            wakeAlarm.cancelAlarm(SetAlarmsActivity.this);
            wakeAlarmIsSet = false;
        }

        // If sleepAlarmSwitch is checked
        if (sleepAlarmSwitch.isChecked()) {
            sleepAlarm.createAlarm(SetAlarmsActivity.this);
            sleepAlarmIsSet = true;
        } else {
            sleepAlarm.cancelAlarm(SetAlarmsActivity.this);
            sleepAlarmIsSet = false;
        }

        saveAlarmSettings();
    }

    /**
     * Button functionalities.
     *
     * If wakeTimePickerEt is clicked: open wakeTimePickerDialog.
     * If sleepTimePickedEt is clicked: open sleepTimePickerDialog.
     * If saveBtn is clicked: set alarms and finish the activity.
     *
     * @param v Clicked Button View.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void buttonPressed(View v) {
        int id = v.getId();

        if (id == wakeTimePickerEt.getId()) {
            wakeTimePickerDialog.show();
        } else if (id == sleepTimePickerEt.getId()) {
            sleepTimePickerDialog.show();
        } else if (id == saveBtn.getId()) {
            setAlarms();

            String message;

            if (wakeAlarmIsSet || sleepAlarmIsSet) {
                message = "Hälytykset asetettu.";
            } else {
                message = "Hälytykset otettu pois päältä.";
            }

            Toast.makeText(getApplicationContext(),message, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    /**
     * Load alarm settings from shared preferences and set the values to:
     * sleepTimeHour, sleepTimeMinute, sleepAlarmIsSet, wakeTimeHour, wakeTimeMinute and wakeAlarmIsSet.
     */
    public void loadAlarmSettings() {
        SharedPreferences alarmPreferences = getSharedPreferences(ALARMS_PREFS, MODE_PRIVATE);
        sleepTimeHour = alarmPreferences.getInt(SLEEP_ALARM_HOUR, 22);
        sleepTimeMinute = alarmPreferences.getInt(SLEEP_ALARM_MINUTE, 30);
        sleepAlarmIsSet = alarmPreferences.getBoolean(SLEEP_ALARM_IS_SET, false);
        wakeTimeHour = alarmPreferences.getInt(WAKE_ALARM_HOUR, 7);
        wakeTimeMinute = alarmPreferences.getInt(WAKE_ALARM_MINUTE, 30);
        wakeAlarmIsSet = alarmPreferences.getBoolean(WAKE_ALARM_IS_SET, false);
    }

    /**
     * Save alarm settings to shared preferences.
     */
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