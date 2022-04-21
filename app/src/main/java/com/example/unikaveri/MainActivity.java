package com.example.unikaveri;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private CurrentTime time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        time = new CurrentTime();
        //test
        //Log.d("date",time);

        Log.d("date","hour: "+time.getCurrentHour());

        /* Edit greeting text */
        TextView editGreeting = (TextView) findViewById(R.id.greetingText);
        // get appropriate greeting based on current hour
        editGreeting.setText("Hyvää "+time.greetingTextTime(time.getCurrentHour())+"!");

        /* Edit clock time */
        TextView editClock = (TextView) findViewById(R.id.textClock);
        editClock.setText(time.getCurrentTime());

        /* Edit date & weekday */
        TextView editWeekday = (TextView) findViewById(R.id.weekdayText);
        editWeekday.setText(time.getDate()+" "+time.getWeekday());

    }
}