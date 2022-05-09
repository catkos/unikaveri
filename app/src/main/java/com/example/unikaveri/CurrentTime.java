package com.example.unikaveri;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Current Time activity - Phone's local time and date
 * @author Catrina
 */
public class CurrentTime {

    private Calendar c;
    private String currentTime;

    /**
     * define current time in Hours:minutes, format it into string
     */
    public CurrentTime(){
        this.c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        this.currentTime = sdf.format(c.getTime());
    }

    /**
     * @return phones current time
     */
    public String getCurrentTime(){
        return this.currentTime;
    }

    /**
     * return phones current hour, formats time to hours
     * @return phones current hour in int
     */
    public int getCurrentHour(){
        String hour = new SimpleDateFormat("HH").format(new Date());
        int currentHour = Integer.parseInt(hour);

        return currentHour;
    }

    /**
     *
     * @return weekday
     */
    public String getWeekday(){
        String weekday = new SimpleDateFormat("EEEE").format(new Date());
        return weekday;
    }

    /**
     *
     * @return date in day.Month
     */
    public String getDate(){
        String date = new SimpleDateFormat("d.M").format(new Date());
        return date;
    }

    /**
     *
     * @param hour for appropriate greeting text search
     * @return appropriate greeting text that matches hour in parameter
     */
    public String greetingTextTime(int hour){

        if(hour >= 3 && hour < 10){
            return "huomenta";
        }

        if(hour >= 17){
            return "iltaa";
        }

        if(hour >= 22){
            return "yötä";
        }

        return "päivää";
    }

}