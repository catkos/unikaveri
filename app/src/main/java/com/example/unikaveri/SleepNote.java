package com.example.unikaveri;

import android.os.Build;
import androidx.annotation.RequiresApi;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * SleepNote Object.
 *
 * Variables: LocalDateTime date, LocalDateTime sleepTimeDate, LocalDateTime wakeUpTimeDate,
 * long sleepingTime, int interruptions, String quality.
 *
 * @author Kerttu
 */
public class SleepNote {

    private LocalDateTime date;
    private LocalDateTime sleepTimeDate;
    private LocalDateTime wakeUpTimeDate;

    private long sleepingTime;
    private int interruptions;

    private String quality;

    /**
     * Define SleepNote Object.
     *
     * @param sleepTimeDate The date and time when user went to sleep.
     * @param wakeTimeDate The date and time when user woke up.
     * @param interruptions How many times user woke up during sleeping.
     * @param quality String. Has to be one of these: "Erittäin huonosti", "Huonosti", "Normaalisti",
     *                "Hyvin" or "Erittäin hyvin"
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public SleepNote(LocalDateTime sleepTimeDate, LocalDateTime wakeTimeDate, int interruptions, String quality) {
        this.date = wakeTimeDate;
        this.sleepTimeDate = sleepTimeDate;
        this.wakeUpTimeDate = wakeTimeDate;
        this.sleepingTime = getSleepingTimeInMinutes();
        this.interruptions = interruptions;

        if (quality.equals("Erittäin huonosti") ||
            quality.equals("Huonosti") ||
            quality.equals("Normaalisti") ||
            quality.equals("Hyvin") ||
            quality.equals("Erittäin hyvin")) {
            this.quality = quality;
        } else {
            // Default, shouldn't ever happen
            this.quality = "Normaalisti";
        }
    }

    /**
     * Return the date and time when user woke up.
     *
     * @return SleepNote Object's date variable.
     */
    public LocalDateTime getDate() {
        return this.date;
    }

    /**
     * Return the date and time when user went to sleep.
     *
     * @return SleepNote Object's bedTimeDate variable.
     */
    public LocalDateTime getSleepTimeDate() {
        return this.sleepTimeDate;
    }

    /**
     * Return the date and time when user woke up.
     *
     * @return SleepNote Object's wakeUpTimeDate variable.
     */
    public LocalDateTime getWakeTimeDate() {
        return this.wakeUpTimeDate;
    }

    /**
     * Return the total sleeping time in hours and minutes.
     *
     * @return String example: "7h 35min"
     */
    public String getSleepingTimeString() {
        long hours = this.sleepingTime / 60 % 24;
        long mins = this.sleepingTime - ( hours * 60 );
        return hours + "h " + mins + "min";
    }

    /**
     * Return the total sleeping time in hours.
     *
     * @return String example: "8"
     */
    public String getSleepingTimeHourString() {
        long hours = this.sleepingTime / 60 % 24;
        return hours+"";
    }

    /**
     * Return the time in minutes between sleepTimeDate and wakeUpTimeDate.
     *
     * @return SleepNote Object's sleeping time in minutes.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private long getSleepingTimeInMinutes() {
        Duration duration = Duration.between(this.sleepTimeDate, this.wakeUpTimeDate);
        long minutes = duration.toMinutes();
        return minutes;
    }

    /**
     * Return how many times user woke up during night.
     *
     * @return SleepNote Object's interruptions variable.
     */
    public int getInterruptions() {
        return this.interruptions;
    }

    /**
     * Return SleepNote object's quality variable.
     *
     * @return SleepNote Object's quality variable: "Erittäin huonosti", "Huonosti", "Normaalisti",
     *          "Hyvin" or "Erittäin hyvin"
     */
    public String getQuality() {
        return this.quality;
    }

    /**
     * Set the date and time when user went to sleep.
     *
     * @param date LocalDateTime to set to sleepTimeDate variable.
     */
    public void setSleepTimeDate(LocalDateTime date) {
        this.sleepTimeDate = date;
    }

    /**
     * Set the date and time when user woke up.
     *
     * @param date LocalDateTime to set to date and wakeUpTimeDate variables.
     */
    public void setWakeUpTimeDate(LocalDateTime date) {
        this.date = date;
        this.wakeUpTimeDate = date;
    }

    /**
     * Set how many times user woke up during night.
     *
     * @param interruptions Integer to set to interruptions variable.
     */
    public void setInterruptions(int interruptions) {
        this.interruptions = interruptions;
    }

    /**
     * Set how user slept.
     *
     * @param quality String to set to quality variable.
     *                Has to be one of these: "Erittäin huonosti", "Huonosti", "Normaalisti",
     *                "Hyvin" or "Erittäin hyvin"
     */
    public void setQuality(String quality) {
        if (quality.equals("Erittäin huonosti") ||
                quality.equals("Huonosti") ||
                quality.equals("Normaalisti") ||
                quality.equals("Hyvin") ||
                quality.equals("Erittäin hyvin")) {
            this.quality = quality;
        } else {
            // Default, shouldn't ever happen
            this.quality = "Normaalisti";
        }
    }

    /**
     * Updates SleepNote Object's sleepingTime variable from getSleepingTimeInMinutes() method.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateSleepingTime() {
        this.sleepingTime = getSleepingTimeInMinutes();
    }

    /**
     * Return SleepNote Object in String format.
     *
     * @return Example: "3.5.2022 keskiviikko: 8h 35min"
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public String toString() {
        return sleepTimeDate.format(DateTimeFormatter.ofPattern("d.M.yyyy EEEE")) + ": " + this.getSleepingTimeString();
    }
}
