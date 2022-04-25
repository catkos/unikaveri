package com.example.unikaveri;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.unikaveri.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private CurrentTime time;
    private BroadcastReceiver minuteUpdate;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* create binding for easier variable search */
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /* navigation item clicked  TODO/TOFIX: not ideal, needs fixing after more activities added(?) */
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch(item.getItemId()){

                case R.id.home:
                    //this is not really a good solution, as the prev activies are still in the background open..?
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    break;
            }
            return true;
        });

        // Create time object (will be overridden by clock UI timer)
        time = new CurrentTime();
        /* * for time testing, TODO: delete this comment later
        * Log.d("date",time);
        * Log.d("date","hour: "+time.getCurrentHour());
        * */

        // Set greeting text
        editGreetingText();

        // Set clock time
        clockTime(time);

        // Start Clock UI update
        startMinuteUpdater();

    }

    /* edit/set time for clock UI */
    private void clockTime(CurrentTime time){
        TextView editClock = (TextView) findViewById(R.id.textClock);
        editClock.setText(time.getCurrentTime());
    }

    /* edit/set greeting & date UI */
    private void editGreetingText(){
        TextView editGreeting = (TextView) findViewById(R.id.greetingText);
        // get appropriate greeting based on current hour
        editGreeting.setText("Hyvää "+time.greetingTextTime(time.getCurrentHour())+"!");

        TextView editWeekday = (TextView) findViewById(R.id.weekdayText);
        editWeekday.setText(time.getDate()+" "+time.getWeekday());
    }

    /* start UI update by minute */
    public void startMinuteUpdater() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK); // = sent every minute
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
}