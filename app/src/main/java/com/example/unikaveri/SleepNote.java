package com.example.unikaveri;

import android.os.Build;
import androidx.annotation.RequiresApi;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * SleepNote Object. Variables: LocalDateTime date, LocalDateTime sleepTimeDate,
 * LocalDateTime wakeUpTimeDate, long sleepingTime, int interruptions, String quality.
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
     * Define SleepNote object.
     * @param sleepTimeDate LocalDateTime
     * @param wakeTimeDate LocalDateTime
     * @param interruptions int
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
            quality.equals("Erittäin huonosti")) {
            this.quality = quality;
        } else {
            // Default, shouldn't ever happen
            this.quality = "Normaalisti";
        }
    }

    /**
     * Return SleepNote Object's date variable.
     * @return LocalDateTime
     */
    public LocalDateTime getDate() {
        return this.date;
    }

    /**
     * Return SleepNote Object's bedTimeDate variable.
     * @return LocalDate
     */
    public LocalDateTime getSleepTimeDate() {
        return this.sleepTimeDate;
    }

    /**
     * Return SleepNote Object's wakeUpTimeDate variable.
     * @return LocalDate
     */
    public LocalDateTime getWakeTimeDate() {
        return this.wakeUpTimeDate;
    }

    /**
     * Return SleepNote Object's sleepingTime variable.
     * @return long
     */
    public long getSleepingTime() {
        return this.sleepingTime;
    }

    /**
     * Return SleepNote Object's sleepingTime in String format.
     * @return String example: "7h 35min"
     */
    public String getSleepingTimeString() {
        long hours = this.sleepingTime / 60 % 24;
        long mins = this.sleepingTime - ( hours * 60 );
        return hours + "h " + mins + "min";
    }

    /**
     * Return SleepNote Object's sleeping time in minutes.
     * @return long - The time in minutes between sleepTimeDate and wakeUpTimeDate.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private long getSleepingTimeInMinutes() {
        Duration duration = Duration.between(this.sleepTimeDate, this.wakeUpTimeDate);
        long minutes = duration.toMinutes();
        return minutes;
    }

    /**
     * Return SleepNote object's interruptions variable.
     * @return int
     */
    public int getInterruptions() {
        return this.interruptions;
    }

    /**
     * Return SleepNote object's quality variable.
     * @return String - "Erittäin huonosti", "Huonosti", "Normaalisti", "Hyvin" or "Erittäin hyvin"
     */
    public String getQuality() {
        return this.quality;
    }

    /**
     * Set SleepNote Object's date.
     * @param date LocalDateTime - LocalDateTime to set date variable.
     */
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    /**
     * Set SleepNote Object's sleepTimeDate.
     * @param date LocalDateTime - LocalDateTime to set sleepTimeDate variable.
     */
    public void setSleepTimeDate(LocalDateTime date) {
        this.sleepTimeDate = date;
    }

    /**
     * Set SleepNote Object's wakeUpTimeDate.
     * @param date LocalDateTime - LocalDateTime to set wakeUpTimeDate variable.
     */
    public void setWakeUpTimeDate(LocalDateTime date) {
        this.wakeUpTimeDate = date;
    }

    /**
     * Set SleepNote Object's interruptions.
     * @param interruptions int - Integer to set interruptions variable.
     */
    public void setInterruptions(int interruptions) {
        this.interruptions = interruptions;
    }

    /**
     * Set SleepNote Object's quality.
     * @param quality String - Has to be one of these: "Erittäin huonosti", "Huonosti", "Normaalisti",
     *                "Hyvin" or "Erittäin hyvin"
     */
    public void setQuality(String quality) {
        if (quality.equals("Erittäin huonosti") ||
                quality.equals("Huonosti") ||
                quality.equals("Normaalisti") ||
                quality.equals("Hyvin") ||
                quality.equals("Erittäin huonosti")) {
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
     * @return String - Example: "3.5.2022 keskiviikko: 8h 35min"
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public String toString() {
        return sleepTimeDate.format(DateTimeFormatter.ofPattern("d.M.yyyy EEEE")) + ": " + this.getSleepingTimeString();
    }
}
