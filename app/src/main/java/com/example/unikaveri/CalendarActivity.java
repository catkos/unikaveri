package com.example.unikaveri;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Calendar activity for showing user's sleep notes in ListView.
 *
 * @author Kerttu
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class CalendarActivity extends AppCompatActivity {

    private final String EXTRA = "SleepNote";

    private final SleepNoteGlobalModel sleepNoteGM = SleepNoteGlobalModel.getInstance();

    private final LocalDateTime maxDate = LocalDateTime.now();
    private LocalDateTime currentDate = LocalDateTime.now();

    private TextView monthYearTv;
    private ListView sleepNotesLv;
    private SleepNoteListviewAdapter listAdapter;

    private int clickedListViewItem;

    /**
     * Load SleepNoteData, set bottom navigation, initialize widgets and update UI.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        bottomNavigation();
        initWidgets();
        updateUI();
    }

    /**
     * Update UI.
     */
    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    /**
     * Update UI.
     */
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
     * Button functionalities.
     *
     * If addNewSleepNoteButton is clicked: open AddSleepNoteActivity.
     * If previousMonthButton is clicked: subtract one month from currentDate and call updateUI().
     * If nextMonthButton is clicked: add one month to currentDate (if it's not after maxDate)
     * and call updateUI().
     *
     * @param v The button user interacted with.
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

        navi.setSelectedItemId(R.id.calendar);

        //item select listener
        navi.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // loop through menu items
                switch(item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
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
}