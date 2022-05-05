package com.example.unikaveri;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * GlobalModel for List of SleepNote Objects.
 *
 * @author Kerttu
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class SleepNoteGlobalModel {

    private static String SLEEP_NOTE_DATA = "sleepNoteData";
    private static String SLEEP_NOTE_DATA_KEY = "sleepNotes";

    private final List<SleepNote> sleepNotesList;
    private final List<SleepNote> monthlySleepNotesList;
    private static final SleepNoteGlobalModel ourInstance = new SleepNoteGlobalModel();

    public static SleepNoteGlobalModel getInstance() {
        return ourInstance;
    }

    /**
     * Initialise SleepNoteGlobalModel.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private SleepNoteGlobalModel() {
        this.sleepNotesList = new ArrayList<>();
        this.monthlySleepNotesList = new ArrayList<>();
        sortList();
    }

    /**
     * Return List with SleepNote Objects.
     * @return List List consist of SleepNote Objects.
     */
    List<SleepNote> getAllSleepNotesList() { return this.sleepNotesList; }

    /**
     * Return List with SleepNote Objects that has same month and year as the date param.
     *
     * @param date LocalDateTime to compare SleepNote Object's dates.
     * @return List. List consists of SleepNote Object's with same month and year
     * as the date param.
     */
    List<SleepNote> getListByMonthAndYear(LocalDateTime date) {
        sortList();
        this.monthlySleepNotesList.clear();
        String dateToCompareStr = date.format(DateTimeFormatter.ofPattern("MM yyyy"));

        // Loop through allSleepNotesList and compare SleepNote Object's date in MM yyyy format to dateToCompareStr.
        for (int i = 0; i < this.sleepNotesList.size(); i++) {
            String dateStr = this.sleepNotesList.get(i).getDate().format(DateTimeFormatter.ofPattern("MM yyyy"));

            if (dateStr.equals(dateToCompareStr)) {
                this.monthlySleepNotesList.add(this.sleepNotesList.get(i));
            }
        }

        return this.monthlySleepNotesList;
    }


    /**
     * Load dates from Shared preferences to SleepNoteGlobalModel's SleepNotes List.
     *
     * @param context Global information of application environment.
     */
    public void loadData(Context context) {
        // Clear SleepNotes List before putting data from shared preferences
        this.sleepNotesList.clear();

        // Create new GsonBuilder to deserialize date strings to LocalDateTime
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();

        SharedPreferences sharedPreferences = context.getSharedPreferences(SLEEP_NOTE_DATA, Context.MODE_PRIVATE);
        String sleepNotesString = sharedPreferences.getString(SLEEP_NOTE_DATA_KEY , "");

        // Check that sleepNoteString is not empty before adding data to SleepNotes list.
        if (!sleepNotesString.isEmpty()) {
            TypeToken<List<SleepNote>> token = new TypeToken<List<SleepNote>>() {};
            List <SleepNote> listTmp = gson.fromJson(sleepNotesString, token.getType());
            SleepNoteGlobalModel.getInstance().getAllSleepNotesList().addAll(listTmp);
        }
    }

    /**
     * Save SleepNoteGlobalModel's SleepNotes List to Shared preferences.
     *
     * @param context Global information of application environment.
     */
    public void saveData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SLEEP_NOTE_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
        String jsonSleepNotes = gson.toJson(SleepNoteGlobalModel.getInstance().getAllSleepNotesList());

        editor.putString(SLEEP_NOTE_DATA_KEY, jsonSleepNotes);
        editor.apply();
    }

    /**
     * Return SleepNote Object from monthlySleepNoteList. monthlySleepNoteList consists of SleepNote
     * Objects with same month and year in SleepNote Object's date variable.
     *
     * @param i Integer to get a specific SleepNote Object from monthlySleepNotesList.
     * @return SleepNote
     */
    SleepNote getSleepNoteFromMonthlyList(int i) {
        return monthlySleepNotesList.get(i);
    }

    /**
     * Delete SleepNote Object from sleepNotesList that equals sleepNote param.
     *
     * @param sleepNote SleepNote Object to delete from sleepNotesList.
     */
    public void deleteSleepNote(SleepNote sleepNote) {
        for (int i = 0; i < this.sleepNotesList.size(); i++) {

            if (sleepNote.equals(this.sleepNotesList.get(i))) {
                this.sleepNotesList.remove(i);
            }
        }

        sortList();
    }

    /**
     * Edit the given SleepNote Object.
     *
     * @param sleepNote SleepNote Object to edit.
     * @param sleepTimeDate LocalDateTime to set the SleepNote Object's new sleepTimeDate variable.
     * @param wakeTimeDate LocalDateTime to set the SleepNote Object's new date and wakeUpTimeDate variable.
     * @param interruptions int to set the SleepNote Object's new interruptions.
     * @param quality String to set the SleepNote Object's new quality. Has to be one of these:
     *                "Erittäin huonosti", "Huonosti", "Normaalisti", "Hyvin" or "Erittäin hyvin"
     */
    public void editSleepNote(SleepNote sleepNote, LocalDateTime sleepTimeDate, LocalDateTime wakeTimeDate, int interruptions, String quality) {
        for (int i = 0; i < this.sleepNotesList.size(); i++) {

            if (sleepNote.equals(this.sleepNotesList.get(i))) {
                this.sleepNotesList.get(i).setSleepTimeDate(sleepTimeDate);
                this.sleepNotesList.get(i).setWakeUpTimeDate(wakeTimeDate);
                this.sleepNotesList.get(i).setInterruptions(interruptions);
                this.sleepNotesList.get(i).setQuality(quality);
                this.sleepNotesList.get(i).updateSleepingTime();
            }
        }
    }

    /**
     * Sort sleepNotesList chronologically so that the SleepNote Object with newest date is first.
     */
    private void sortList() {
        // Create Comparator to compare SleepNote Object's date variables together
        Comparator<SleepNote> comparator = (sleepNote1, sleepNote2) -> {
            return sleepNote1.getDate().compareTo(sleepNote2.getDate());
        };

        // Sort the list with comparator and reverse the list
        Collections.sort(this.sleepNotesList, comparator);
        Collections.reverse(this.sleepNotesList);
    }
}
