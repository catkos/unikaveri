package com.example.unikaveri;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Activity to add sleep note
 *
 * @author Kerttu
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class AddSleepNoteActivity extends AppCompatActivity {

    private final SleepNoteGlobalModel sleepNoteGM = SleepNoteGlobalModel.getInstance();

    private final String SLEEP_NOTE_DATA = "sleepNoteData";
    private final String EXTRA = "SleepNote";

    private int editSleepNote = -1;

    private EditText sleepDatePickerEt;
    private EditText sleepTimePickerEt;
    private EditText wakeDatePickerEt;
    private EditText wakeTimePickerEt;
    private EditText interruptionsEt;
    private RadioGroup sleepQualityRg;
    private Button saveSleepNoteBtn;

    private DatePickerDialog sleepDatePickerDialog;
    private TimePickerDialog sleepTimePickerDialog;
    private DatePickerDialog wakeDatePickerDialog;
    private TimePickerDialog wakeTimePickerDialog;

    private AlertDialog alertDialog;

    /**
     * Initialize alert dialogs, widgets and check if editing Sleep Note and change inputs according to it.
     * @param savedInstanceState
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sleep_note);
        Bundle bundle = getIntent().getExtras();
        initAlertDialog();
        initWidgets();

        // Set editSleepNote value if bundle is not null
        if (!(bundle == null)) {
            editSleepNote = bundle.getInt(EXTRA, -1);
        }

        // Check if editing SleepNote
        if (editSleepNote == -1) {
            initSleepDatePickerDialog(LocalDate.now().minusDays(1));
            initSleepTimePickerDialog(LocalTime.of(22, 30));
            initWakeDatePickerDialog(LocalDate.now());
            initWakeTimePickerDialog(LocalTime.of(7,30));
        } else {
            getSupportActionBar().setTitle("Muokkaa merkintää");
            SleepNote sleepNote = sleepNoteGM.getSleepNoteFromMonthlyList(editSleepNote);
            LocalDateTime sleepDateTime = sleepNote.getSleepTimeDate();
            LocalDateTime wakeDateTime = sleepNote.getWakeTimeDate();
            int interruptions = sleepNote.getInterruptions();
            String quality = sleepNote.getQuality();
            initSleepDatePickerDialog(LocalDate.of(sleepDateTime.getYear(), sleepDateTime.getMonthValue(), sleepDateTime.getDayOfMonth()));
            initSleepTimePickerDialog(LocalTime.of(sleepDateTime.getHour(), sleepDateTime.getMinute()));
            initWakeDatePickerDialog(LocalDate.of(wakeDateTime.getYear(), wakeDateTime.getMonthValue(), wakeDateTime.getDayOfMonth()));
            initWakeTimePickerDialog(LocalTime.of(wakeDateTime.getHour(), wakeDateTime.getMinute()));
            setInterruptionsEt(interruptions);
            setQualityRadioButton(quality);
        }
    }

    /**
     * Set quality radio button to checked depending on quality param.
     *
     * @param quality "Erittäin huonosti", "Huonosti", "Normaalisti", "Hyvin" or "Erittäin hyvin"
     */
    private void setQualityRadioButton(String quality) {
        if (quality.equals("Erittäin huonosti")) {
            ((RadioButton) findViewById(R.id.veryDissatisfiedRadioButton)).setChecked(true);
        } else if (quality.equals("Huonosti")) {
            ((RadioButton) findViewById(R.id.dissatisfiedRadioButton)).setChecked(true);
        } else if (quality.equals("Normaalisti")) {
            ((RadioButton) findViewById(R.id.neutralRadioButton)).setChecked(true);
        } else if (quality.equals("Hyvin")) {
            ((RadioButton) findViewById(R.id.satisfiedRadioButton)).setChecked(true);
        } else if (quality.equals("Erittäin hyvin")) {
            ((RadioButton) findViewById(R.id.verySatisfiedRadioButton)).setChecked(true);
        } else {
            // Default shouldn't ever happen.
            ((RadioButton) findViewById(R.id.neutralRadioButton)).setChecked(true);
        }
    }

    /**
     * Set interruptionsEt input's text to interruptions param.
     *
     * @param interruptions int
     */
    private void setInterruptionsEt(int interruptions) {
        interruptionsEt.setText(Integer.toString(interruptions));
    }

    /**
     * When user interacts with back key, open alert dialog.
     */
    @Override
    public void onBackPressed() {
        alertDialog.show();
    }

    /**
     * When user interacts with top action bar's arrow button, open alert dialog.
     *
     * @param item MenuItem user interacted with
     * @return boolean
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            alertDialog.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Initialize alert dialog for when user wants to go back without saving.
     */
    private void initAlertDialog() {
        // Set alert builder
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("Ilmoitus");
        alertBuilder.setMessage("Haluatko varmasti poistua tallentamatta?");

        // Set positive button and add onClick listener to it:
        // if user clicks positive button, finish activity
        alertBuilder.setPositiveButton("Jatka tallentamatta", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        // Set negative button
        alertBuilder.setNegativeButton("Peruuta", null);

        // Create alert dialog
        alertDialog = alertBuilder.create();
    }

    /**
     * Initialize EditText and Button views.
     */
    private void initWidgets() {
        sleepDatePickerEt = findViewById(R.id.sleepDatePickerEditText);
        sleepTimePickerEt = findViewById(R.id.sleepTimePickerEditText);
        wakeDatePickerEt = findViewById(R.id.wakeDatePickerEditText);
        wakeTimePickerEt = findViewById(R.id.wakeTimePickerEditText);
        interruptionsEt = findViewById(R.id.interuptionsPickerEditText);
        sleepQualityRg = findViewById(R.id.sleepQualityRadioGroup);
        saveSleepNoteBtn = findViewById(R.id.saveSleepNoteButton);
    }

    /**
     * Initialize DatePickerDialog for choosing the date when user went to sleep.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initSleepDatePickerDialog(LocalDate date) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                LocalDate date = LocalDate.of(year, month+1, day);
                sleepDatePickerEt.setText(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy EE")));
            }
        };

        sleepDatePickerDialog = new DatePickerDialog(
                this,
                dateSetListener,
                date.getYear(),
                date.getMonthValue()-1,
                date.getDayOfMonth());

        //sleepDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        sleepDatePickerEt.setText(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy EE")));
    }

    /**
     * Initialize TimePickerDialog for choosing the time when user went to sleep.
     */
    private void initSleepTimePickerDialog(LocalTime time) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                sleepTimePickerEt.setText(String.format(
                        Locale.getDefault(),
                        "%02d:%02d",
                        hour,
                        minute));
            }
        };

        sleepTimePickerDialog = new TimePickerDialog(
                this,
                onTimeSetListener,
                time.getHour(),
                time.getMinute(),
                true);

        sleepTimePickerEt.setText(String.format(
                Locale.getDefault(),
                "%02d:%02d",
                time.getHour(),
                time.getMinute()));
    }

    /**
     * Initialize DatePickerDialog for choosing the date when user woke up.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initWakeDatePickerDialog(LocalDate date) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                LocalDate date = LocalDate.of(year, month+1, day);
                wakeDatePickerEt.setText(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy EE")));
            }
        };

        wakeDatePickerDialog = new DatePickerDialog(
                this,
                dateSetListener,
                date.getYear(),
                date.getMonthValue()-1,
                date.getDayOfMonth());

        wakeDatePickerEt.setText(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy EE")));
    }

    /**
     * Initialize TimePickerDialog for choosing the time when user woke up.
     */
    private void initWakeTimePickerDialog(LocalTime time) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                wakeTimePickerEt.setText(String.format(
                        Locale.getDefault(),
                        "%02d:%02d",
                        hour,
                        minute));
            }
        };

        wakeTimePickerDialog = new TimePickerDialog(
                this,
                onTimeSetListener,
                time.getHour(),
                time.getMinute(),
                true);

        wakeTimePickerEt.setText(String.format(Locale.getDefault(),
                "%02d:%02d",
                time.getHour(),
                time.getMinute()));
    }

    /**
     * Button functionalities.
     *
     * If wakeDatePickerEt is clicked: open dialog to choose the date when user woke up.
     * If wakeTimePickerEt is clicked: open dialog to choose at what time user woke up.
     * If sleepTimePickerEt is clicked: open dialog to choose at what time user went to sleep.
     * If sleepDatePickerEt is clicked: open dialog to choose the date when user went to sleep.
     * If saveSleepNoteBtn is clicked: save sleep note.
     *
     * @param v The button interacted with.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void buttonPressed(View v) {
        int id = v.getId();

        if (id == wakeDatePickerEt.getId()) {
            wakeDatePickerDialog.show();
        } else if (id == wakeTimePickerEt.getId()) {
            wakeTimePickerDialog.show();
        } else if (id == sleepTimePickerEt.getId()) {
            sleepTimePickerDialog.show();
        } else if(id == sleepDatePickerEt.getId()) {
            sleepDatePickerDialog.show();
        } else if (id == saveSleepNoteBtn.getId()) {

            // Check if SleepNote Object was added/edited to SleepNoteGlobalModel SleepNote list
            boolean isAdded = addSleepNoteToSleepNoteList();

            if (isAdded) {
                // Save SleepNoteGlobalModel SleepNote list locally to shared preferences.
                sleepNoteGM.saveData(this);

                String toastMsg;

                if (editSleepNote == -1) {
                    toastMsg = "Merkintä tallennettu päivälle " + wakeDatePickerEt.getText().toString() + ".";
                } else {
                    toastMsg = "Merkintä muokattu.";
                }

                // Create toast to inform user that the note was added/edited
                Toast.makeText(getApplicationContext(),toastMsg, Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    /**
     * Validate inputs and add SleepNote Object to SleepNoteGlobalModel SleepNote list. Returns true
     * if everything was ok, or false if some of the inputs weren't correctly submitted.
     *
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
        if (interruptionsEt.getText().toString().isEmpty()) {
            interruptions = 0;
        } else {
            interruptions = Integer.parseInt(interruptionsEt.getText().toString());
        }

        // Check if none of the RadioGroup's RadioButtons is checked
        if (sleepQualityRg.getCheckedRadioButtonId() == -1) {
            ((RadioButton) findViewById(R.id.verySatisfiedRadioButton)).setError("");
            Toast.makeText(getApplicationContext(),"Valitse miten nukuit.", Toast.LENGTH_LONG).show();
            return false;
        } else {

            // Check which radiobutton is checked and set it's value to quality variable.
            if (sleepQualityRg.getCheckedRadioButtonId() == R.id.veryDissatisfiedRadioButton) {
                quality = "Erittäin huonosti";
            } else if (sleepQualityRg.getCheckedRadioButtonId() == R.id.dissatisfiedRadioButton) {
                quality = "Huonosti";
            } else if (sleepQualityRg.getCheckedRadioButtonId() == R.id.neutralRadioButton) {
                quality = "Normaalisti";
            } else if (sleepQualityRg.getCheckedRadioButtonId() == R.id.satisfiedRadioButton) {
                quality = "Hyvin";
            } else if (sleepQualityRg.getCheckedRadioButtonId() == R.id.verySatisfiedRadioButton) {
                quality = "Erittäin hyvin";
            } else {
                // Default, shouldn't ever happen
                quality = "Normaalisti";
            }
        }

        // Check if editing SleepNote
        if (editSleepNote == -1) {
            // Add SleepNote to SleepNoteGlobalModel's SleepNote list.
            sleepNoteGM.getAllSleepNotesList().add(new SleepNote(sleepTimeDate, wakeTimeDate, interruptions, quality));
        } else {
            // Edit SleepNote
            sleepNoteGM.editSleepNote(sleepNoteGM.getSleepNoteFromMonthlyList(editSleepNote), sleepTimeDate, wakeTimeDate, interruptions, quality);
        }
        return true;
    }
}