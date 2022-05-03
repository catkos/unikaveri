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

    private int avgWaking=0;
    private int avgSleeping=0;
    private double avgSleepHoursSum=0;
    private int totalInterruptions=0;
    private int counter=0;
    private List<Integer> sleepingHours = new ArrayList<>();

    private TextView wakingText;
    private TextView sleepingText;
    private TextView sleepHoursSumText;
    private TextView interruptionsText;

    private TextView monthYearUI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);

        // set bottomNavigation item activities
        bottomNavigation();

        initWidgets();

        updateUI();

        insertData();

    }

    /**
     * initialize widgets
     */
    private void initWidgets() {
        wakingText = findViewById(R.id.avgWakingTime);
        sleepingText = findViewById(R.id.avgSleepingTime);
        sleepHoursSumText = findViewById(R.id.avgSleepHour);
        interruptionsText = findViewById(R.id.interruptionsCounter);

        monthYearUI = findViewById(R.id.textMonth);
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
            insertData();
            updateUI();
        }
        if(v.getId()==R.id.nextMonthButton){
            LocalDateTime tmp = currentDate.plusMonths(1);

            if (!tmp.isAfter(maxDate)) {
                currentDate = currentDate.plusMonths(1);
                insertData();
                updateUI();
            }
        }
    }

    /**
     * insert data from sP sleep note data json & make necessary calculations
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void insertData(){

        // Create new GsonBuilder to deserialize date strings to LocalDateTime
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();

        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.SLEEP_NOTE_DATA, MODE_PRIVATE);
        String sleepNotesString = sharedPreferences.getString("sleepNotes", "");

        // Check that sleepNoteString is not empty
        if (!sleepNotesString.isEmpty()) {
            TypeToken<List<SleepNote>> token = new TypeToken<List<SleepNote>>() {};
            List <SleepNote> listTmp = gson.fromJson(sleepNotesString, token.getType());

            // Calculate TODO: fix the calculations
            for(SleepNote s : listTmp){
                // Calculate ONLY if month & year matches UI month & year
                if(s.getDate().format(DateTimeFormatter.ofPattern("MM.yyyy")).equals(currentDate.format(DateTimeFormatter.ofPattern("MM.yyyy")))){

                    //add going to sleep time
                    avgSleeping+=Integer.parseInt(s.getSleepTimeDate().format(DateTimeFormatter.ofPattern("HH")));

                    //add waking up time
                    avgWaking+=Integer.parseInt(s.getWakeTimeDate().format(DateTimeFormatter.ofPattern("HH")));

                    //add sleeping time hours into list
                    sleepingHours.add(Integer.parseInt(s.getSleepingTimeHourString()));

                    //add total interruptions
                    totalInterruptions+=s.getInterruptions();

                    //add after every loop
                    counter++;

                }

            }

            //check if sleepingHours list is NOT empty before average calculations
            if(!sleepingHours.isEmpty()){
                int sleepingHoursTemp=0;
                for(int i=0;i<sleepingHours.size();i++){ sleepingHoursTemp+=sleepingHours.get(i); }
                avgSleepHoursSum = sleepingHoursTemp / counter;
            }

            //set data to widgets
            setTextData();

        }

    }

    /**
     * reset all variables for re- data/stat calculations
     */
    public void resetVariables(){
        avgWaking=0;
        avgSleeping=0;
        avgSleepHoursSum=0;
        totalInterruptions=0;
        counter=0;
        sleepingHours.clear();
    }

    /**
     * set data/stats to textview widgets
     */
    public void setTextData(){
        //if current chosen month data is empty, set default
        if(avgSleeping==0){
            sleepingText.setText("00:00");
            wakingText.setText("00:00");
            sleepHoursSumText.setText("0h");
        }else {
            sleepingText.setText(String.format("%.0f", (double) avgSleeping / counter) + ":00");
            wakingText.setText(String.format("%.0f", (double) avgWaking / counter) + ":00");
            sleepHoursSumText.setText(String.format("%.0f", (double) avgSleepHoursSum) + "h");
            interruptionsText.setText("" + totalInterruptions);
        }
    }

    /**
     * update UI widgets
     */
    public void updateUI(){
        monthYearUI.setText(currentDate.format(DateTimeFormatter.ofPattern("MMMM yyyy")).toUpperCase());

        resetVariables();
    }
}