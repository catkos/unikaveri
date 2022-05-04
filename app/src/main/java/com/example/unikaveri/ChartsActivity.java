package com.example.unikaveri;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Charts/stats activity.
 * @author Catrina
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class ChartsActivity extends AppCompatActivity {

    private GetSleepNoteData GetSleepNoteData;

    private LocalDateTime currentDate = LocalDateTime.now();
    private final LocalDateTime maxDate = LocalDateTime.now();

    private int avgWaking=0;
    private int avgWakingMin=00;
    private int avgSleeping=0;
    private int avgSleepingMin=00;
    private double avgSleepHoursSum=0;
    private int totalInterruptions=0;
    private int counter=0;
    private List<Integer> sleepingHours = new ArrayList<>();

    private List<Integer> wakingTimeHours = new ArrayList<>();
    private List<Integer> sleepingTimeHours = new ArrayList<>();

    private TextView wakingText;
    private TextView sleepingText;
    private TextView sleepHoursSumText;
    private TextView interruptionsText;
    private TextView frequentWakingTime;
    private TextView frequentSleepTime;

    private TextView monthYearUI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);

        GetSleepNoteData = GetSleepNoteData.getInstance();

        bottomNavigation();

        initWidgets();
        updateUI();

        getData();
        setTextData();

    }

    /**
     * set progress bar to current day
     * @param currentDay used for progress bar progress calculation
     */
    private void progressBar(int currentDay){
        ProgressBar pb = findViewById(R.id.progressBar2);

        TextView pbPercent = findViewById(R.id.pbPercentageText);

        pb.setProgress(currentDay);

        pbPercent.setText(currentDay*100/pb.getMax()+"%");
    }

    /**
     * initialize widgets
     */
    private void initWidgets() {
        wakingText = findViewById(R.id.avgWakingTime);
        sleepingText = findViewById(R.id.avgSleepingTime);
        sleepHoursSumText = findViewById(R.id.avgSleepHour);
        interruptionsText = findViewById(R.id.interruptionsCounter);
        frequentWakingTime = findViewById(R.id.frequentWakeHour);
        frequentSleepTime = findViewById(R.id.frequentSleepHour);

        monthYearUI = findViewById(R.id.textMonth);
    }

    /**
     * bottom navigation
     */
    private void bottomNavigation(){
        BottomNavigationView navi = findViewById(R.id.bottomNavigationView);

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
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.calendar:
                        startActivity(new Intent(getApplicationContext(),CalendarActivity.class));
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
     * button functionalities
     */
    public void buttonPressed(View v) {
        if(v.getId()==R.id.previousMonthButton){
            currentDate = currentDate.minusMonths(1);
            getData();
            updateUI();
        }
        if(v.getId()==R.id.nextMonthButton){
            LocalDateTime tmp = currentDate.plusMonths(1);

            if (!tmp.isAfter(maxDate)) {
                currentDate = currentDate.plusMonths(1);
                getData();
                updateUI();
            }
        }
    }

    /**
     * loop data from sleep note data json using GetSleepNoteData singleton and add data to variables and lists
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getData(){

        if(!GetSleepNoteData.getInstance().isEmpty()){

            for(SleepNote s: GetSleepNoteData.getSleepNotes(GetSleepNoteData.getPrefs(this).getString("sleepNotes", ""))){
                // Calculate ONLY if month and year matches UI month and year
                if(s.getDate().format(DateTimeFormatter.ofPattern("MM.yyyy")).equals(currentDate.format(DateTimeFormatter.ofPattern("MM.yyyy")))){

                    //add waking up time
                    avgWaking+=Integer.parseInt(s.getWakeTimeDate().format(DateTimeFormatter.ofPattern("HH")));
                    avgWakingMin+=Integer.parseInt(s.getWakeTimeDate().format(DateTimeFormatter.ofPattern("mm")));
                    //add wake time in arraylist
                    wakingTimeHours.add(Integer.parseInt(s.getWakeTimeDate().format(DateTimeFormatter.ofPattern("HH"))));

                    //add going to sleep time
                    avgSleeping+=Integer.parseInt(s.getSleepTimeDate().format(DateTimeFormatter.ofPattern("HH")));
                    avgSleepingMin+=Integer.parseInt(s.getSleepTimeDate().format(DateTimeFormatter.ofPattern("mm")));
                    //add going to sleep time in arraylist
                    sleepingTimeHours.add(Integer.parseInt(s.getSleepTimeDate().format(DateTimeFormatter.ofPattern("HH"))));

                    //add sleeping time hours into list
                    sleepingHours.add(Integer.parseInt(s.getSleepingTimeHourString()));

                    //add total interruptions
                    totalInterruptions+=s.getInterruptions();

                    //counter + after every loop
                    counter++;

                }
            }

        }
    }

    /**
     * calculate average sleeping hour from list
     * @param hoursList list of hours saved from data
     * @return the average hour from list
     */
    public double avgSleepingHour(List<Integer> hoursList){
        if(!hoursList.isEmpty()){
            int sleepingHoursTemp=0;
            //add values from list to a temp variable
            for(int i=0;i<sleepingHours.size();i++){ sleepingHoursTemp+=sleepingHours.get(i); }
            //calculate avg
            avgSleepHoursSum = sleepingHoursTemp / counter;
            return avgSleepHoursSum;
        }
        return 0;
    }

    /**
     * get most frequent/common hour from list
     * @param hoursList list of hours saved from data
     * @return the most frequent/common time from list
     */
    public int mostFrequentTime(List<Integer> hoursList){

        int hourValueTemp=0;
        int hourValueTempFrequency=0;

        //add hours from list to hashmap and assign their frequency value
        Map<Integer,Integer> hm = new HashMap();

        for(int i:hoursList){
            if(!hm.containsKey(i)){
                hm.put(i,1);
            }else{
                //increase frequency
                hm.put(i, hm.get(i)+1);
            }
        }

        //invoke keySet() on hashmap to get keys as a set
        Set<Integer> keys = hm.keySet();
        for(int key : keys){

            //if current freq vari is lower than new freq, assign value temp to key
            if(hourValueTempFrequency < hm.get(key)){
                hourValueTemp = key;
            }

        }

        return hourValueTemp;

    }

    /**
     * format avgMin variable correctly for UI
     * @param avgMin sum of minutes from data
     * @return the average value of avgMin sum
     */
    public String avgMinuteFormat(int avgMin){
        if((double)avgMin/counter==0){
            return "00";
        }
        return ""+String.format("%.0f", (double) avgMin / counter);
    }

    /**
     * reset all variables for re- data/stat calculations
     */
    public void resetVariables(){
        avgWaking=0;
        avgWakingMin=00;
        avgSleeping=0;
        avgSleepingMin=00;
        avgSleepHoursSum=0;
        totalInterruptions=0;
        counter=0;
        sleepingHours.clear();
        wakingTimeHours.clear();
        sleepingTimeHours.clear();
    }

    /**
     * set data/stats to textview widgets
     */
    public void setTextData(){
        //if current UI month data is empty, set default
        if(avgSleeping==0){
            wakingText.setText("00:00");
            sleepingText.setText("00:00");
            sleepHoursSumText.setText("0h");
            frequentWakingTime.setText("-");
            frequentSleepTime.setText("-");
        }else{
            wakingText.setText(String.format("%.0f", (double) avgWaking / counter) + ":"+avgMinuteFormat(avgWakingMin));
            sleepingText.setText(String.format("%.0f", (double) avgSleeping / counter) + ":"+avgMinuteFormat(avgSleepingMin));
            sleepHoursSumText.setText(String.format("%.0f", (double) avgSleepingHour(sleepingHours)) + "h");
            interruptionsText.setText("" + totalInterruptions);
            frequentWakingTime.setText(mostFrequentTime(wakingTimeHours)+":00");
            frequentSleepTime.setText(mostFrequentTime(sleepingTimeHours)+":00");
        }
    }

    /**
     * update UI widgets
     */
    public void updateUI(){
        monthYearUI.setText(currentDate.format(DateTimeFormatter.ofPattern("MMMM yyyy")).toUpperCase());

        //set new calculated data into text widgets
        setTextData();

        //update progress bar
        int currentDay = 30;
        //check if UI month equals current month
        if(currentDate.format(DateTimeFormatter.ofPattern("MMMM yyyy")).equals(LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMMM yyyy")))){
            currentDay = Integer.parseInt(currentDate.format(DateTimeFormatter.ofPattern("d")));
        }
        progressBar(currentDay);

        //reset variables after UI update
        resetVariables();
    }
}