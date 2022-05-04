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

    public GetSleepNoteData(){
        sleepNotes = new ArrayList<>();
        token = new TypeToken<List<SleepNote>>() {};

        // Create new GsonBuilder to deserialize date strings to LocalDateTime
        gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
    }

    public void addSleepNoteData(String sleepNotesString){
        sleepNotes  = gson.fromJson(sleepNotesString, token.getType());
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
     * checks if sleepNotes list is empty in the month assigned
     * @param currentDate variable that tells assigned given month
     * @return false or true depending if sleepNotes has a value that's added in the assigned month
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
