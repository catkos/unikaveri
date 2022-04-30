package com.example.unikaveri;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Calendar activity.
 * @author Kerttu
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class CalendarActivity extends AppCompatActivity {
    private TextView monthYearTv;
    private ListView sleepNotesLv;
    private LocalDateTime currentDate = LocalDateTime.now();
    private final LocalDateTime maxDate = LocalDateTime.now();
    public static SleepNoteListviewAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        bottomNavigation();
        initWidgets();
        updateUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    /**
     * Initialize text and list views.
     */
    private void initWidgets() {
        monthYearTv = findViewById(R.id.monthYearTextView);
        sleepNotesLv = findViewById(R.id.sleepNotesListView);
        sleepNotesLv.setEmptyView((TextView) findViewById(R.id.emptyListTextView));
    }

    private SleepNoteListviewAdapter initListAdapter() {
        return new SleepNoteListviewAdapter(
                this,
                R.layout.sleep_note_list_item_layout,
                SleepNoteGlobalModel.getInstance().getListByMonthAndYear(currentDate));
    }

    /**
     * Update UI and set listAdapter.
     */
    private void updateUI() {
        monthYearTv.setText(currentDate.format(DateTimeFormatter.ofPattern("MMMM yyyy")).toUpperCase());

        this.listAdapter = new SleepNoteListviewAdapter(
                this,
                R.layout.sleep_note_list_item_layout,
                SleepNoteGlobalModel.getInstance().getListByMonthAndYear(currentDate));

        sleepNotesLv.setAdapter(this.listAdapter);
    }

    /**
     * Buttons functionalities.
     *
     * If addNewSleepNoteButton is clicked: open AddSleepNoteActivity.
     * Else if previousMonthButton is clicked: subtract one month from currentDate and call updateUI().
     * Else if nextMonthButton is clicked: add one month to currentDate (if it's not after maxDate)
     * and call updateUI().
     * @param v View
     */
    public void buttonPressed(View v) {

        if (v.getId() == R.id.addNewSleepNoteButton) {
            Intent intent = new Intent(this, AddSleepNoteActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.previousMonthButton) {
            currentDate = currentDate.minusMonths(1);
            updateUI();
        } else if (v.getId() == R.id.nextMonthButton) {
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
}