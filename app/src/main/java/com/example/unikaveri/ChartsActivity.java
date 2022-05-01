package com.example.unikaveri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Charts/stats activity.
 * @author Catrina
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class ChartsActivity extends AppCompatActivity {

    private LocalDateTime currentDate = LocalDateTime.now();
    private final LocalDateTime maxDate = LocalDateTime.now();

    // Create variables
    private int avgWaking = 0;
    private int avgSleeping=0;
    private double avgSleepHoursSum=0;
    private int totalInterruptions=0;
    private int counter=0;

    private TextView wakingText;
    private TextView sleepingText;
    private TextView sleepHoursSumText;
    private TextView interruptionsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);

        // set bottomNavigation item activities
        bottomNavigation();

        initWidgets();

        updateUI();

        insetData();

    }

    /**
     * initialize widgets
     */
    private void initWidgets() {
        wakingText = findViewById(R.id.avgWakingTime);
        sleepingText = findViewById(R.id.avgSleepingTime);
        sleepHoursSumText = findViewById(R.id.avgSleepHour);
        interruptionsText = (TextView) findViewById(R.id.interruptionsCounter);
    }

    /**
     * bottom navigation
     * TODO: copy-paste this function to other activities
     */
    private void bottomNavigation(){
        BottomNavigationView navi = findViewById(R.id.bottomNavigationView);

        // TODO: set selected activity for menu highlight
        navi.setSelectedItemId(R.id.charts);

        //item select listener
        navi.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //loop through menu items
                switch(item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        return true;
                    case R.id.calendar:
                        startActivity(new Intent(getApplicationContext(),CalendarActivity.class));
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
     * button functionalities
     */
    public void buttonPressed(View v) {
        if(v.getId()==R.id.previousMonthButton){
            currentDate = currentDate.minusMonths(1);
            updateUI();
        }
        if(v.getId()==R.id.nextMonthButton){
            LocalDateTime tmp = currentDate.plusMonths(1);

            if (!tmp.isAfter(maxDate)) {
                currentDate = currentDate.plusMonths(1);
                updateUI();
            }
        }
    }

    /**
     * insert data from sP sleep note data json & make necessary calculations
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void insetData(){

        // Create new GsonBuilder to deserialize date strings to LocalDateTime
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();

        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.SLEEP_NOTE_DATA, MODE_PRIVATE);
        String sleepNotesString = sharedPreferences.getString("sleepNotes", "");

        // Check that sleepNoteString is not empty before adding data to SleepNotes list.
        if (!sleepNotesString.isEmpty()) {
            TypeToken<List<SleepNote>> token = new TypeToken<List<SleepNote>>() {};
            List <SleepNote> listTmp = gson.fromJson(sleepNotesString, token.getType());

            // Create a list where it saves sleep time for later average calculation purposes
            List<Integer> sleepingHours = new ArrayList<Integer>();

            // Calculate TODO: fix the calculations
            for(SleepNote s : listTmp){

                //average going to sleep time
                avgSleeping+=Integer.parseInt(s.getSleepTimeDate().format(DateTimeFormatter.ofPattern("HH")));
                Log.d("test","avgsleep: "+avgSleeping);

                //average waking up time
                avgWaking+=Integer.parseInt(s.getWakeTimeDate().format(DateTimeFormatter.ofPattern("HH")));
                Log.d("test","avgwaking: "+avgWaking);

                //add sleeping time hours into list
                sleepingHours.add(Integer.parseInt(s.getSleepingTimeHourString()));

                //total interruptions
                totalInterruptions+=s.getInterruptions();

                //add after every loop
                counter++;
            }

            //calculate average of sleepingHours
            int sleepingHoursTemp=0;
            for(int i=0;i<sleepingHours.size();i++){ sleepingHoursTemp+=sleepingHours.get(i); }
            avgSleepHoursSum = sleepingHoursTemp / counter;
        }
        //update UI with data
        updateUI();

    }

    /**
     * update UI widgets
     */
    public void updateUI(){
        TextView monthYearUI = findViewById(R.id.textMonth);
        monthYearUI.setText(currentDate.format(DateTimeFormatter.ofPattern("MMMM yyyy")).toUpperCase());

        //TODO: fix these calculations
        sleepingText.setText(String.format("%.0f",(double) avgSleeping / counter)+":00");

        wakingText.setText(String.format("%.0f",(double) avgWaking / counter)+":00");

        sleepHoursSumText.setText(String.format("%.0f",(double) avgSleepHoursSum)+"h");

        interruptionsText.setText(""+totalInterruptions);

    }
}