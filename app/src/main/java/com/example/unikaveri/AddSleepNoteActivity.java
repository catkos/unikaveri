package com.example.unikaveri;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

/**
 * Add Sleep note activity.
 * @author Kerttu
 */
public class AddSleepNoteActivity extends AppCompatActivity {
    private EditText sleepDatePickerEt;
    private EditText sleepTimePickerEt;
    private EditText wakeDatePickerEt;
    private EditText wakeTimePickerEt;
    private EditText interuptionsPickerEt;
    private RadioGroup sleepQualityRg;
    private Button saveSleepNoteBtn;
    private DatePickerDialog sleepDatePickerDialog;
    private TimePickerDialog sleepTimePickerDialog;
    private DatePickerDialog wakeDatePickerDialog;
    private TimePickerDialog wakeTimePickerDialog;
    private int currentYear = Calendar.getInstance().get(Calendar.YEAR);
    private int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
    private int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sleep_note);
        initWidgets();
        initSleepDatePickerDialog();
        initSleepTimePickerDialog();
        initWakeDatePickerDialog();
        initWakeTimePickerDialog();
    }

    /**
     * When user interacts with action bar's arrow button: open dialog to ask if user wants to
     * continue without saving, and set click listener to dialog's positive button to open CalendarActivity.
     * @param item Menuitem
     * @return
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Set alert builder
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
            alertBuilder.setTitle("Ilmoitus");
            alertBuilder.setMessage("Haluatko varmasti poistua tallentamatta?");

            // Add buttons
            alertBuilder.setPositiveButton("Jatka tallentamatta", new DialogInterface.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    openCalendarActivity();
                }
            });

            alertBuilder.setNegativeButton("Peruuta", null);

            // Create and show the alert dialog
            AlertDialog dialog = alertBuilder.create();
            dialog.show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Initialize EditText and Button views.
     */
    private void initWidgets() {
        sleepDatePickerEt = findViewById(R.id.sleepDatePickerEditText);
        sleepTimePickerEt = findViewById(R.id.sleepTimePickerEditText);
        wakeDatePickerEt = findViewById(R.id.wakeDatePickerEditText);
        wakeTimePickerEt = findViewById(R.id.wakeTimePickerEditText);
        interuptionsPickerEt = findViewById(R.id.interuptionsPickerEditText);
        sleepQualityRg = findViewById(R.id.sleepQualityRadioGroup);
        saveSleepNoteBtn = findViewById(R.id.saveSleepNoteButton);
    }

    /**
     * Initialize DatePickerDialog for choosing the date when user went to sleep.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initSleepDatePickerDialog() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                LocalDate date = LocalDate.of(year, month+1, day);
                sleepDatePickerEt.setText(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy EE")));
            }
        };

        sleepDatePickerDialog = new DatePickerDialog(this, dateSetListener, currentYear, currentMonth+1, currentDay-1);
        sleepDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        LocalDate date = LocalDate.of(currentYear, currentMonth+1, currentDay-1);
        sleepDatePickerEt.setText(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy EE")));
    }

    /**
     * Initialize TimePickerDialog for choosing the time when user went to sleep.
     */
    private void initSleepTimePickerDialog() {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                sleepTimePickerEt.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
            }
        };

        // TODO: Set default value to the time user has saved to preferred wake up time.
        sleepTimePickerDialog = new TimePickerDialog(this, onTimeSetListener, 22, 30,true);
        sleepTimePickerEt.setText(String.format(Locale.getDefault(), "%02d:%02d", 22, 30));
    }

    /**
     * Initialize DatePickerDialog for choosing the date when user woke up.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initWakeDatePickerDialog() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                LocalDate date = LocalDate.of(year, month+1, day);
                wakeDatePickerEt.setText(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy EE")));
            }
        };

        wakeDatePickerDialog = new DatePickerDialog(this, dateSetListener, currentYear, currentMonth+1, currentDay);
        wakeDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        LocalDate date = LocalDate.of(currentYear, currentMonth+1, currentDay);
        wakeDatePickerEt.setText(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy EE")));
    }

    /**
     * Initialize TimePickerDialog for choosing the time when user woke up.
     */
    private void initWakeTimePickerDialog() {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                wakeTimePickerEt.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
            }
        };

        // TODO: Set default value to the time user has saved to preferred wake up time.
        wakeTimePickerDialog = new TimePickerDialog(this, onTimeSetListener, 7, 30,true);
        wakeTimePickerEt.setText(String.format(Locale.getDefault(), "%02d:%02d", 7, 30));
    }

    /**
     * Buttons functionalities.
     * @param v View
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void buttonPressed(View v) {
        if (v.getId() == wakeDatePickerEt.getId()) {
            wakeDatePickerDialog.show();
        } else if (v.getId() == wakeTimePickerEt.getId()) {
            wakeTimePickerDialog.show();
        } else if (v.getId() == sleepTimePickerEt.getId()) {
            sleepTimePickerDialog.show();
        } else if(v.getId() == sleepDatePickerEt.getId()) {
            sleepDatePickerDialog.show();
        } else if (v.getId() == saveSleepNoteBtn.getId()) {

            // Check if SleepNote Object was added to SleepNoteGlobalModel SleepNote list
            boolean isAdded = addSleepNoteToSleepNoteList();

            if (isAdded) {
                // Save SleepNoteGlobalModel SleepNote list locally to shared preferences.
                saveSleepNoteData();
                // Create toast to inform user that the note was added
                Toast.makeText(getApplicationContext(),"Merkintä tallennettu päivälle " + wakeDatePickerEt.getText().toString(), Toast.LENGTH_LONG).show();
                openCalendarActivity();
            }
        }
    }

    /**
     * Checks inputs and adds SleepNote Object to SleepNoteGlobalModel SleepNote list. Returns true
     * if everything was ok, or false if some of the inputs weren't correctly submitted.
     * @return boolean
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean addSleepNoteToSleepNoteList() {
        LocalDateTime sleepTimeDate;
        LocalDateTime wakeTimeDate;
        int interruptions;
        String quality;

        // Check if dates or times are empty
        if (sleepDatePickerEt.getText().toString().isEmpty() ||
                sleepTimePickerEt.getText().toString().isEmpty() ||
                wakeDatePickerEt.getText().toString().isEmpty() ||
                wakeTimePickerEt.getText().toString().isEmpty()) {
            sleepDatePickerEt.setError("");
            Toast.makeText(getApplicationContext(),"Päivät tai kellonajat eivät voi olla tyhjiä.", Toast.LENGTH_LONG).show();
            return false;
        } else {
            String sleepTimeDateStr = sleepDatePickerEt.getText().toString() + " " + sleepTimePickerEt.getText().toString();
            String wakeTimeDateStr = wakeDatePickerEt.getText().toString() + " " + wakeTimePickerEt.getText().toString();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy EE HH:mm");
            sleepTimeDate = LocalDateTime.parse(sleepTimeDateStr, dtf);
            wakeTimeDate = LocalDateTime.parse(wakeTimeDateStr, dtf);
        }

        // Check if sleepTimeDate is after wakeTimeDate
        if (sleepTimeDate.isAfter(wakeTimeDate)) {
            sleepDatePickerEt.setError("");
            Toast.makeText(getApplicationContext(),"Nukkumaanmeno aika ei voi olla heräämisajan jälkeen.", Toast.LENGTH_LONG).show();
            return false;
        }

        // Check if interruptions input is empty
        if (interuptionsPickerEt.getText().toString().isEmpty()) {
            interruptions = 0;
        } else {
            interruptions = Integer.parseInt(interuptionsPickerEt.getText().toString());
        }

        // Check if none of the RadioGroup's RadioButtons is checked
        if (sleepQualityRg.getCheckedRadioButtonId() == -1) {
            ((RadioButton) findViewById(R.id.verySatisfiedRadioButton)).setError("");
            Toast.makeText(getApplicationContext(),"Valitse miten nukuit.", Toast.LENGTH_LONG).show();
            return false;
        } else {

            // Check which radiobutton is checked and set it's value to quality variable.
            if (sleepQualityRg.getCheckedRadioButtonId() == R.id.veryDissatisfiedRadioButton) {
                quality = "Erittäin huono";
            } else if (sleepQualityRg.getCheckedRadioButtonId() == R.id.dissatisfiedRadioButton) {
                quality = "Huono";
            } else if (sleepQualityRg.getCheckedRadioButtonId() == R.id.neutralRadioButton) {
                quality = "Normaali";
            } else if (sleepQualityRg.getCheckedRadioButtonId() == R.id.satisfiedRadioButton) {
                quality = "Hyvä";
            } else if (sleepQualityRg.getCheckedRadioButtonId() == R.id.verySatisfiedRadioButton) {
                quality = "Erittäin hyvä";
            } else {
                // Default, shouldn't ever happen
                quality = "Normaali";
            }
        }

        // Add SleepNote to SleepNoteGlobalModel's SleepNote list.
        SleepNoteGlobalModel.getInstance().getAllSleepNotesList().add(new SleepNote(sleepTimeDate, wakeTimeDate, interruptions, quality));
        return true;
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

    /**
     * Opens CalendarActivity.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void openCalendarActivity() {
        // Open CalendarActivity
        Intent intent = new Intent(this, CalendarActivity.class);
        startActivity(intent);
        // Finish this activity
        finish();
    }
}