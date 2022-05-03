package com.example.unikaveri;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Calendar activity.
 * @author Kerttu
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class CalendarActivity extends AppCompatActivity {

    private final SleepNoteGlobalModel sleepNoteGM = SleepNoteGlobalModel.getInstance();
    private final String EXTRA = "SleepNote";
    private final String SLEEP_NOTE_DATA = "sleepNoteData";
    private final LocalDateTime maxDate = LocalDateTime.now();
    private LocalDateTime currentDate = LocalDateTime.now();
    private TextView monthYearTv;
    private ListView sleepNotesLv;
    private SleepNoteListviewAdapter listAdapter;
    private int clickedListViewItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        loadSleepNoteData();
        bottomNavigation();
        initWidgets();
        updateUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateUI();
    }

    /**
     * Initialize TextViews and ListView.
     */
    private void initWidgets() {
        monthYearTv = findViewById(R.id.monthYearTextView);
        sleepNotesLv = findViewById(R.id.sleepNotesListView);
        sleepNotesLv.setEmptyView((TextView) findViewById(R.id.emptyListTextView));
    }

    /**
     * Update UI and set listAdapter.
     */
    private void updateUI() {
        monthYearTv.setText(currentDate.format(DateTimeFormatter.ofPattern("MMMM yyyy")).toUpperCase());

        this.listAdapter = new SleepNoteListviewAdapter(
                this,
                R.layout.sleep_note_list_item_layout,
                sleepNoteGM.getListByMonthAndYear(currentDate));

        sleepNotesLv.setAdapter(this.listAdapter);

        sleepNotesLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                clickedListViewItem = i;
                Intent intent = new Intent(CalendarActivity.this, SleepNoteDetailsActivity.class);
                intent.putExtra(EXTRA, clickedListViewItem);
                startActivity(intent);
            }
        });
    }

    /**
     * Buttons functionalities.
     * If addNewSleepNoteButton is clicked: open AddSleepNoteActivity.
     * Else if previousMonthButton is clicked: subtract one month from currentDate and call updateUI().
     * Else if nextMonthButton is clicked: add one month to currentDate (if it's not after maxDate)
     * and call updateUI().
     * @param v View - The View user interacted with.
     */
    public void buttonPressed(View v) {
        int id = v.getId();

        if (id == R.id.addNewSleepNoteButton) {
            Intent intent = new Intent(this, AddSleepNoteActivity.class);
            startActivity(intent);
        } else if (id == R.id.previousMonthButton) {
            currentDate = currentDate.minusMonths(1);
            updateUI();
        } else if (id == R.id.nextMonthButton) {
            LocalDateTime tmp = currentDate.plusMonths(1);

            if (!tmp.isAfter(maxDate)) {
                currentDate = currentDate.plusMonths(1);
                updateUI();
            }
        }
    }

    /**
     * Bottom navigation's functionality.
     */
    private void bottomNavigation(){
        BottomNavigationView navi = findViewById(R.id.bottomNavigationView);

        // TODO: set selected activity for menu highlight
        navi.setSelectedItemId(R.id.calendar);

        //item select listener
        navi.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // loop through menu items
                switch(item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
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