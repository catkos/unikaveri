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
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

/**
 * Main activity. Test update
 * @author Catrina, Kerttu and Tino
 */
public class MainActivity extends AppCompatActivity {

    private final String SLEEP_NOTE_DATA = "sleepNoteData";

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

        // Load sleep note data to SleepNoteGlobalModel
        loadSleepNoteData();

        // Load alarm settings from shared preferences
        loadAlarmSettings();

        // Initialise alarm textviews
        alarmTextViews();

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
     */
    private void bottomNavigation(){
        BottomNavigationView navi = findViewById(R.id.bottomNavigationView);

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
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.charts:
                        startActivity(new Intent(getApplicationContext(),ChartsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.settings:
                        startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

    /**
     * edit/set time for clock UI
     * @param time CurrentTime's object current time
     */
    private void clockTime(CurrentTime time){
        TextView editClock = (TextView) findViewById(R.id.textClock);
        editClock.setText(time.getCurrentTime());
    }

    /**
     * edit/set greeting and date UI
     * also handles displaying name
     */
    private void editGreetingText(){
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);

        String username = "" +
                sharedPrefs.getString("pref_username", "");

        TextView editGreeting = (TextView) findViewById(R.id.greetingText);
        // get appropriate greeting based on current hour
        editGreeting.setText("Hyvää "+time.greetingTextTime(time.getCurrentHour())+" "+username+"!");

        TextView editWeekday = (TextView) findViewById(R.id.weekdayText);
        editWeekday.setText(time.getDate()+" "+time.getWeekday());
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
        loadAlarmSettings();
        alarmTextViews();
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
     * If sleepTimeText or wakeTimeText is clicked: open SetAlarmsActivity.
     * @param v View
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void buttonPressed(View v) {
        int id = v.getId();

        if (id == R.id.addNewSleepNoteButton) {
            Intent intent = new Intent(this, AddSleepNoteActivity.class);
            startActivity(intent);
        } else if (id == R.id.sleepTimeText) {
            Intent intent = new Intent(this, SetAlarmsActivity.class);
            startActivity(intent);
        } else if (id == R.id.wakeTimeText) {
            Intent intent = new Intent(this, SetAlarmsActivity.class);
            startActivity(intent);
        }
    }

    /**
     * Load alarm settings from shared preferences and set the values to: sleepTimeHour, sleepTimeMinute,
     * sleepAlarmIsSet, wakeTimeHour, wakeTimeMinute and wakeAlarmIsSet.
     */
    public void loadAlarmSettings() {
        SharedPreferences alarmPrefs = getSharedPreferences(SetAlarmsActivity.ALARMS_PREFS, MODE_PRIVATE);
        sleepTimeHour = alarmPrefs.getInt(SetAlarmsActivity.SLEEP_ALARM_HOUR, 22);
        sleepTimeMinute = alarmPrefs.getInt(SetAlarmsActivity.SLEEP_ALARM_MINUTE, 30);
        sleepAlarmIsSet = alarmPrefs.getBoolean(SetAlarmsActivity.SLEEP_ALARM_IS_SET, false);
        wakeTimeHour = alarmPrefs.getInt(SetAlarmsActivity.WAKE_ALARM_HOUR, 7);
        wakeTimeMinute = alarmPrefs.getInt(SetAlarmsActivity.WAKE_ALARM_MINUTE, 30);
        wakeAlarmIsSet = alarmPrefs.getBoolean(SetAlarmsActivity.WAKE_ALARM_IS_SET, false);
    }

    /**
     * Initialise TextViews based on sleepAlarmIsSet/wakeAlarmIsSet: if their value is true, set text
     * according to sleepTimeHour/wakeTimeHour and sleepTimeMinute/wakeTimeMinute.
     * Or if value is false: set text to "Pois päältä".
     */
    public void alarmTextViews() {
        sleepTimeTv = findViewById(R.id.sleepTimeText);
        wakeTimeTv = findViewById(R.id.wakeTimeText);

        if (sleepAlarmIsSet) {
            SpannableString content = new SpannableString(String.format(
                    Locale.getDefault(),
                    "%02d:%02d",
                    sleepTimeHour,
                    sleepTimeMinute));
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            sleepTimeTv.setText(content);
        } else {
            SpannableString content = new SpannableString("Pois päältä");
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            sleepTimeTv.setText(content);
        }

        if (wakeAlarmIsSet) {
            SpannableString content = new SpannableString(String.format(
                    Locale.getDefault(),
                    "%02d:%02d",
                    wakeTimeHour,
                    wakeTimeMinute));
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            wakeTimeTv.setText(content);
        } else {
            SpannableString content = new SpannableString("Pois päältä");
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            wakeTimeTv.setText(content);
        }
    }

    /**
     * Load SleepNote Objects from Shared preferences to SleepNoteGlobalModel's SleepNotes List.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loadSleepNoteData() {
        // Clear SleepNotes List before putting data from shared preferences
        SleepNoteGlobalModel.getInstance().getAllSleepNotesList().clear();

        // Create new GsonBuilder to deserialize date strings to LocalDateTime
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();

        SharedPreferences sharedPreferences = getSharedPreferences(SLEEP_NOTE_DATA, MODE_PRIVATE);
        String sleepNotesString = sharedPreferences.getString("sleepNotes", "");

        // Check that sleepNoteString is not empty before adding data to SleepNotes list.
        if (!sleepNotesString.isEmpty()) {
            TypeToken<List<SleepNote>> token = new TypeToken<List<SleepNote>>() {};
            List <SleepNote> listTmp = gson.fromJson(sleepNotesString, token.getType());
            SleepNoteGlobalModel.getInstance().getAllSleepNotesList().addAll(listTmp);
        }
    }
}