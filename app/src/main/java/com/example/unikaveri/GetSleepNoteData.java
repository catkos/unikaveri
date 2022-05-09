package com.example.unikaveri;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresApi;

import android.content.Context;

import android.os.Build;

/**
 * Get data from SharedPreferences SleepNote local file
 * @author Catrina
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class GetSleepNoteData {

    private static String SLEEP_NOTE_DATA = "sleepNoteData";

    private List<SleepNote> sleepNotes;

    private TypeToken<List<SleepNote>> token;

    private Gson gson;

    /**
     * initialize sleepNotes array for later data adding, init token.
     * deserialize LocalDateTime.
     */
    public GetSleepNoteData(){
        sleepNotes = new ArrayList<>();
        token = new TypeToken<List<SleepNote>>() {};

        // Create new GsonBuilder to deserialize date strings to LocalDateTime
        gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
    }

    /**
     * add data to list.
     * if list is empty, do not add data from shared preference
     * @param sleepNotesString string for data search
     */
    public void addSleepNoteData(String sleepNotesString){
        if(!(gson.fromJson(sleepNotesString, token.getType())==null)){
            sleepNotes  = gson.fromJson(sleepNotesString, token.getType());
        }
    }

    /**
     * return SharedPreferences
     * @param context context used for sharedpreference
     * @return SharedPreference with assigned context
     */
    public SharedPreferences getPrefs(Context context){
        return context.getSharedPreferences(SLEEP_NOTE_DATA, Context.MODE_PRIVATE);
    }

    /**
     * checks if list is empty
     * @return true if empty, false if not
     */
    public boolean isEmpty(){
        return sleepNotes.isEmpty();
    }

    /**
     * checks if sleepNotes list is empty from the month and year in parameter by looping through data
     * @param currentDate that tells what month and year to look at
     * @return false or true depending if sleepNotes has at least one value that has the same month and year like in given parameter
     */
    public boolean isEmptyInSpecificMonth(LocalDateTime currentDate){
        for(SleepNote s: sleepNotes){
            if(s.getDate().format(DateTimeFormatter.ofPattern("MM.yyyy")).equals(currentDate.format(DateTimeFormatter.ofPattern("MM.yyyy")))){
                return false;
            }
        }
        return true;
    }

    /**
     * returns sleepNotes list, return null if list empty
     * @return sleepNotes list
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<SleepNote> getSleepNotes(){
        return sleepNotes;
    }
}
