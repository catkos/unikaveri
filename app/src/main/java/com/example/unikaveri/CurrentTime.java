package com.example.unikaveri;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CurrentTime {

    private Calendar c;
    private String currentTime;

    /* get current time */
    public CurrentTime(){
        this.c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        this.currentTime = sdf.format(c.getTime());
    }

    /* return current time */
    public String getCurrentTime(){
        return this.currentTime;
    }

    /* return current time in hours */
    public int getCurrentHour(){
        String hour = new SimpleDateFormat("HH").format(new Date());
        int currentHour = Integer.parseInt(hour);

        return currentHour;
    }

    /* return current weekday */
    public String getWeekday(){
        String weekday = new SimpleDateFormat("EEEE").format(new Date());
        return weekday;
    }

    /* return current date */
    public String getDate(){
        String date = new SimpleDateFormat("d.M").format(new Date());
        return date;
    }

    /* return appropriate greetingText string TODO: add more greetings? */
    public String greetingTextTime(int hour){
        if(hour >= 18 || hour <= 3){
            return "iltaa";
        }

        if(hour >= 3 && hour <= 9){
            return "huomenta";
        }

        if(hour >= 10 && hour <= 17){
            return "päivää";
        }

        return "(time not added yet)";
    }

}