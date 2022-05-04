package com.example.unikaveri;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import java.time.format.DateTimeFormatter;

public class SleepNoteDetailsActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_note_details);

        Bundle bundle = getIntent().getExtras();
        int i = bundle.getInt(CalendarActivity.EXTRA, 0);

        String date = SleepNoteGlobalModel.getInstance().getSleepNote(i).getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy EEEE"));
        String sleepTime = SleepNoteGlobalModel.getInstance().getSleepNote(i).getSleepTimeDate().format(DateTimeFormatter.ofPattern("HH:mm"));
        String wakeTime = SleepNoteGlobalModel.getInstance().getSleepNote(i).getWakeTimeDate().format(DateTimeFormatter.ofPattern("HH:mm"));
        String sleepingTime = SleepNoteGlobalModel.getInstance().getSleepNote(i).getSleepingTimeString();
        int interruptions = SleepNoteGlobalModel.getInstance().getSleepNote(i).getInterruptions();
        String quality = SleepNoteGlobalModel.getInstance().getSleepNote(i).getQuality();

        ((TextView) findViewById(R.id.sleepNoteDetailsSleepingTimeTextView)).setText(sleepingTime);
        ((TextView) findViewById(R.id.sleepNoteDetailsSleepTimeTextView)).setText(sleepTime);
        ((TextView) findViewById(R.id.sleepNoteDetailsWakeTimeTextView)).setText(wakeTime);
        ((TextView) findViewById(R.id.sleepNoteDetailsInterruptionsTextView)).setText(Integer.toString(interruptions));
        ((TextView) findViewById(R.id.sleepNoteDetailsQualityTextView)).setText(quality);

        getSupportActionBar().setTitle(date);
    }
}