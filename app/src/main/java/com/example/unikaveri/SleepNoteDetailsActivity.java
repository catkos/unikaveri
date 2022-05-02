package com.example.unikaveri;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SleepNoteDetailsActivity extends AppCompatActivity {
    int i;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_note_details);

        Bundle bundle = getIntent().getExtras();
        i = bundle.getInt(CalendarActivity.EXTRA, 0);

        String date = SleepNoteGlobalModel.getInstance().getSleepNoteFromMonthlyList(i).getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy EEEE"));
        String sleepTime = SleepNoteGlobalModel.getInstance().getSleepNoteFromMonthlyList(i).getSleepTimeDate().format(DateTimeFormatter.ofPattern("HH:mm"));
        String wakeTime = SleepNoteGlobalModel.getInstance().getSleepNoteFromMonthlyList(i).getWakeTimeDate().format(DateTimeFormatter.ofPattern("HH:mm"));
        String sleepingTime = SleepNoteGlobalModel.getInstance().getSleepNoteFromMonthlyList(i).getSleepingTimeString();
        int interruptions = SleepNoteGlobalModel.getInstance().getSleepNoteFromMonthlyList(i).getInterruptions();
        String quality = SleepNoteGlobalModel.getInstance().getSleepNoteFromMonthlyList(i).getQuality();

        ((TextView) findViewById(R.id.sleepNoteDetailsSleepingTimeTextView)).setText(sleepingTime);
        ((TextView) findViewById(R.id.sleepNoteDetailsSleepTimeTextView)).setText(sleepTime);
        ((TextView) findViewById(R.id.sleepNoteDetailsWakeTimeTextView)).setText(wakeTime);
        ((TextView) findViewById(R.id.sleepNoteDetailsInterruptionsTextView)).setText(Integer.toString(interruptions));
        ((TextView) findViewById(R.id.sleepNoteDetailsQualityTextView)).setText(quality);

        getSupportActionBar().setTitle(date);
    }

    /**
     * Instantiate menu items from res/menu/sleep_note_details_menu.xml
     * @param menu Menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sleep_note_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_edit) {
            return true;
        } else if (id == R.id.action_delete) {
            // Set alert builder
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
            alertBuilder.setTitle("Ilmoitus");
            alertBuilder.setMessage("Haluatko varmasti poistaa merkinnän?");

            // Add buttons
            alertBuilder.setPositiveButton("Poista", new DialogInterface.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SleepNoteGlobalModel.getInstance().deleteSleepNote(SleepNoteGlobalModel.getInstance().getSleepNoteFromMonthlyList(i));
                    Toast.makeText(getApplicationContext(),"Merkintä poistettu.", Toast.LENGTH_LONG).show();
                    saveSleepNoteData();
                    finish();
                }
            });

            alertBuilder.setNegativeButton("Peruuta", null);

            // Create and show the alert dialog
            AlertDialog dialog = alertBuilder.create();
            dialog.show();

            return true;
        } else if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Save SleepNoteGlobalModel's SleepNote list as Json to shared preferences.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void saveSleepNoteData() {
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.SLEEP_NOTE_DATA, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
        String jsonSleepNotes = gson.toJson(SleepNoteGlobalModel.getInstance().getAllSleepNotesList());

        editor.putString("sleepNotes", jsonSleepNotes);
        editor.apply();
    }
}