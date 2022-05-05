package com.example.unikaveri;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.time.format.DateTimeFormatter;

/**
 * Activity to show SleepNote's details.
 *
 * @author Kerttu
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class SleepNoteDetailsActivity extends AppCompatActivity {

    private final SleepNoteGlobalModel sleepNoteGM = SleepNoteGlobalModel.getInstance();

    private final String SLEEP_NOTE_DATA = "sleepNoteData";
    private final String EXTRA = "SleepNote";

    private int sleepNoteIndex;

    private SleepNote sleepNote;
    private TextView sleepingTimeTv;
    private TextView sleepTimeTv;
    private TextView wakeTimeTv;
    private TextView interruptionsTv;
    private TextView qualityTv;

    private AlertDialog alertDialog;

    /**
     * Set sleepNoteIndex from intent extras, initialize widgets and update UI.
     *
     * @param savedInstanceState
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_note_details);
        Bundle bundle = getIntent().getExtras();
        sleepNoteIndex = bundle.getInt(EXTRA, 0);
        initWidgets();
        initAlertDialog();
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
     * Initialize menu items from res/menu/sleep_note_details_menu.xml
     *
     * @param menu Menu
     * @return boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sleep_note_details_menu, menu);
        return true;
    }

    /**
     * When user interacts with top action bar's buttons.
     *
     * If user clicks action_edit: Create intent from AddSleepNoteActivity, put the sleepNoteIndex
     * as extra and start activity.
     * If user click action_delete: show alert dialog.
     *
     * @param item The clicked MenuItem.
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case R.id.action_edit:
                Intent intent = new Intent(this, AddSleepNoteActivity.class);
                intent.putExtra(EXTRA, sleepNoteIndex);
                startActivity(intent);
                return true;
            case R.id.action_delete:
                alertDialog.show();
                return true;
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Initialize TextViews.
     */
    private void initWidgets() {
        sleepingTimeTv = findViewById(R.id.sleepNoteDetailsSleepingTimeTextView);
        sleepTimeTv = findViewById(R.id.sleepNoteDetailsSleepTimeTextView);
        wakeTimeTv = findViewById(R.id.sleepNoteDetailsWakeTimeTextView);
        interruptionsTv = findViewById(R.id.sleepNoteDetailsInterruptionsTextView);
        qualityTv = findViewById(R.id.sleepNoteDetailsQualityTextView);
    }

    /**
     * Set data from SleepNote Object to TextViews and set action bar's title to date.
     */
    private void updateUI() {
        sleepNote = sleepNoteGM.getSleepNoteFromMonthlyList(sleepNoteIndex);

        // Get data from SleepNote object
        String date = sleepNote.getDate().format(DateTimeFormatter.ofPattern("d.M.yyyy EE"));
        String sleepTime = sleepNote.getSleepTimeDate().format(DateTimeFormatter.ofPattern("HH:mm"));
        String wakeTime = sleepNote.getWakeTimeDate().format(DateTimeFormatter.ofPattern("HH:mm"));
        String sleepingTime = sleepNote.getSleepingTimeString();
        int interruptions = sleepNote.getInterruptions();
        String quality = sleepNote.getQuality();

        // Set data to TextViews
        sleepingTimeTv.setText(sleepingTime);
        sleepTimeTv.setText(sleepTime);
        wakeTimeTv.setText(wakeTime);
        interruptionsTv.setText(Integer.toString(interruptions));
        qualityTv.setText(quality);

        // Set the action bar's title to date
        getSupportActionBar().setTitle(date);
    }

    /**
     * Initialize alert dialog for when user wants to delete SleepNote.
     * Add onClick listener for positive button to delete SleepNote and finish this activity.
     */
    private void initAlertDialog() {
        // Set alert builder
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("Ilmoitus");
        alertBuilder.setMessage("Haluatko varmasti poistaa merkinnän?");

        // Set positive button
        alertBuilder.setPositiveButton("Poista", new DialogInterface.OnClickListener() {

            // If user clicks positive button, delete the chosen SleepNote
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sleepNoteGM.deleteSleepNote(sleepNote);
                sleepNoteGM.saveData(getApplicationContext());
                Toast.makeText(getApplicationContext(),"Merkintä poistettu.", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        // Set negative button
        alertBuilder.setNegativeButton("Peruuta", null);

        // Create and show the alert dialog
        alertDialog = alertBuilder.create();
    }
}