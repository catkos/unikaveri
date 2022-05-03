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
 * @author Kerttu
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class SleepNoteDetailsActivity extends AppCompatActivity {

    private final SleepNoteGlobalModel sleepNoteGM = SleepNoteGlobalModel.getInstance();
    private final String EXTRA = "SleepNote";
    private int sleepNoteIndex;
    private SleepNote sleepNote;
    private TextView sleepingTimeTv;
    private TextView sleepTimeTv;
    private TextView wakeTimeTv;
    private TextView interruptionsTv;
    private TextView qualityTv;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_note_details);
        Bundle bundle = getIntent().getExtras();
        sleepNoteIndex = bundle.getInt(EXTRA, 0);
        initWidgets();
        updateUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    /**
     * Initialize menu items from res/menu/sleep_note_details_menu.xml
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
     * @param item MenuItem
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
                initAlertDialog();
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
     * Set data from SleepNote Object to TextViews.
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


        getSupportActionBar().setTitle(date);
    }

    /**
     * Initialize alert dialog and onClick listener for positive button when user wants to
     * delete the SleepNote.
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
                Toast.makeText(getApplicationContext(),"Merkintä poistettu.", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        // Set negative button
        alertBuilder.setNegativeButton("Peruuta", null);

        // Create and show the alert dialog
        AlertDialog dialog = alertBuilder.create();
        dialog.show();
    }
}