package com.example.unikaveri;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private CurrentTime time;
    private BroadcastReceiver minuteUpdate;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}