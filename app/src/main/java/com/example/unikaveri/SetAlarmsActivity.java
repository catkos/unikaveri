package com.example.unikaveri;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import java.time.LocalTime;
import java.util.Locale;

public class SetAlarmsActivity extends AppCompatActivity {

    private TimePickerDialog sleepTimePickerDialog;
    private TimePickerDialog wakeTimePickerDialog;
    private EditText sleepTimePickerEt;
    private EditText wakeTimePickerEt;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarms);
        initWidgets();
        initSleepTimePickerDialog(LocalTime.of(22,30));
        initWakeTimePickerDialog(LocalTime.of(7,30));
    }

    private void initWidgets() {
        sleepTimePickerEt = findViewById(R.id.sleepTimePickerEditText);
        wakeTimePickerEt = findViewById(R.id.wakeTimePickerEditText);
    }

    /**
     * Initialize TimePickerDialog for choosing the time when user went to sleep.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initSleepTimePickerDialog(LocalTime time) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                sleepTimePickerEt.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
            }
        };

        sleepTimePickerDialog = new TimePickerDialog(this, onTimeSetListener, time.getHour(), time.getMinute(),true);
        sleepTimePickerEt.setText(String.format(Locale.getDefault(), "%02d:%02d", time.getHour(), time.getMinute()));
    }

    /**
     * Initialize TimePickerDialog for choosing the time when user woke up.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initWakeTimePickerDialog(LocalTime time) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                wakeTimePickerEt.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
            }
        };

        wakeTimePickerDialog = new TimePickerDialog(this, onTimeSetListener, time.getHour(), time.getMinute(),true);
        wakeTimePickerEt.setText(String.format(Locale.getDefault(), "%02d:%02d", time.getHour(), time.getMinute()));
    }

    /**
     * Buttons functionalities.
     * @param v View
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void buttonPressed(View v) {
        if (v.getId() == wakeTimePickerEt.getId()) {
            wakeTimePickerDialog.show();
        } else if (v.getId() == sleepTimePickerEt.getId()) {
            sleepTimePickerDialog.show();
        }
    }
}