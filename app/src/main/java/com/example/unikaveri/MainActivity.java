package com.example.unikaveri;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private CurrentTime time;
    private BroadcastReceiver minuteUpdate;

    private boolean sleepAlarmIsSet = false;
    private boolean wakeAlarmIsSet = false;
    private int sleepTimeHour;
    private int sleepTimeMinute;
    private int wakeTimeHour;
    private int wakeTimeMinute;

    private TextView sleepTimeTv;
    private TextView wakeTimeTv;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sleepTimeTv = findViewById(R.id.sleepTimeText);
        wakeTimeTv = findViewById(R.id.wakeTimeText);

        // Load alarm settings from shared preferences
        loadAlarmSettings();

        initAlarmTextViews();

        // set bottomNavigation item activities
        bottomNavigation();

        // Create time object (will be overridden by clock UI timer)
        time = new CurrentTime();

        // Set greeting text
        editGreetingText();

        // Set clock time
        clockTime(time);

        // Start Clock UI update
        startMinuteUpdater();
    }

    /**
     * bottom navigation function
     * TODO: copy-paste this to other activities
     */
    private void bottomNavigation(){
        BottomNavigationView navi = findViewById(R.id.bottomNavigationView);

        // TODO: set selected activity for menu highlight
        navi.setSelectedItemId(R.id.home);

        //item select listener
        navi.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //loop through menu items
                switch(item.getItemId()){
                    case R.id.calendar:
                        startActivity(new Intent(getApplicationContext(),CalendarActivity.class));
                        return true;
                    case R.id.charts:
                        startActivity(new Intent(getApplicationContext(),ChartsActivity.class));
                        return true;
                    case R.id.settings:
                        Log.d("menu settings",""); //TODO:<- delete, add startActivity
                        return true;
                }
                return false;
            }
        });
    }

    /**
     * edit/set time for clock UI
     * @param time
     */
    private void clockTime(CurrentTime time){
        TextView editClock = (TextView) findViewById(R.id.textClock);
        editClock.setText(time.getCurrentTime());
    }

    /**
     * edit/set greeting & date UI
     */
    private void editGreetingText(){
        TextView editGreeting = (TextView) findViewById(R.id.greetingText);
        // get appropriate greeting based on current hour
        editGreeting.setText("Hyvää "+time.greetingTextTime(time.getCurrentHour())+"!");

        TextView editWeekday = (TextView) findViewById(R.id.weekdayText);
        editWeekday.setText(time.getDate()+" "+time.getWeekday().toUpperCase());
    }

    /**
     * start UI update, refresh every 1 minute
     */
    public void startMinuteUpdater() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        minuteUpdate = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //override/create new object (so it can get current phone time)
                time = new CurrentTime();
                //set new time in UI
                clockTime(time);
            }
        };
        registerReceiver(minuteUpdate, intentFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startMinuteUpdater(); //continue clock UI updater
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(minuteUpdate); //pause clock UI updater
    }

    /**
     * Buttons functionalities.
     * If addNewSleepNoteButton is clicked: open AddSleepNoteActivity.
     * @param v View
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void buttonPressed(View v) {
        int id = v.getId();

        if (id == R.id.addNewSleepNoteButton) {
            Intent intent = new Intent(this, AddSleepNoteActivity.class);
            startActivity(intent);
        } else if (id == R.id.sleepTimeText || id == R.id.wakeTimeText) {
            Intent intent = new Intent(this, SetAlarmsActivity.class);
            startActivity(intent);
        }
    }

    public void loadAlarmSettings() {
        SharedPreferences alarmPrefs = getSharedPreferences(SetAlarmsActivity.ALARMS_PREFS, MODE_PRIVATE);
        sleepTimeHour = alarmPrefs.getInt(SetAlarmsActivity.SLEEP_ALARM_HOUR, 22);
        sleepTimeMinute = alarmPrefs.getInt(SetAlarmsActivity.SLEEP_ALARM_MINUTE, 30);
        sleepAlarmIsSet = alarmPrefs.getBoolean(SetAlarmsActivity.SLEEP_ALARM_IS_SET, false);
        wakeTimeHour = alarmPrefs.getInt(SetAlarmsActivity.WAKE_ALARM_HOUR, 7);
        wakeTimeMinute = alarmPrefs.getInt(SetAlarmsActivity.WAKE_ALARM_MINUTE, 30);
        wakeAlarmIsSet = alarmPrefs.getBoolean(SetAlarmsActivity.WAKE_ALARM_IS_SET, false);
    }

    public void initAlarmTextViews() {
        if (sleepAlarmIsSet) {
            sleepTimeTv.setText(String.format(
                    Locale.getDefault(),
                    "%02d:%02d",
                    sleepTimeHour,
                    sleepTimeMinute));
        } else {
            sleepTimeTv.setText("Pois päältä");
        }

        if (wakeAlarmIsSet) {
            wakeTimeTv.setText(String.format(
                    Locale.getDefault(),
                    "%02d:%02d",
                    wakeTimeHour,
                    wakeTimeMinute));
        } else {
            wakeTimeTv.setText("Pois päältä");
        }
    }
}