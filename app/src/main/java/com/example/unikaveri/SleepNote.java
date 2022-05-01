package com.example.unikaveri;

import android.os.Build;
import androidx.annotation.RequiresApi;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * SleepNote object, with variables: LocalDateTime date, LocalDateTime sleepTimeDate,
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
     * @param quality String
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public SleepNote(LocalDateTime sleepTimeDate, LocalDateTime wakeTimeDate, int interruptions, String quality) {
        this.date = wakeTimeDate;
        this.sleepTimeDate = sleepTimeDate;
        this.wakeUpTimeDate = wakeTimeDate;
        this.sleepingTime = getSleepingTimeInMinutes();
        this.interruptions = interruptions;
        this.quality = quality;
    }

    /**
     * Return SleepNote object's date variable.
     * @return LocalDateTime
     */
    public LocalDateTime getDate() {
        return this.date;
    }

    /**
     * Return SleepNote object's bedTimeDate variable.
     * @return LocalDate
     */
    public LocalDateTime getSleepTimeDate() {
        return this.sleepTimeDate;
    }

    /**
     * Return SleepNote object's wakeUpTimeDate variable.
     * @return LocalDate
     */
    public LocalDateTime getWakeTimeDate() {
        return this.wakeUpTimeDate;
    }

    /**
     * Return SleepNote object's sleepingTime variable.
     * @return long
     */
    public long getSleepingTime() {
        return this.sleepingTime;
    }

    public String getSleepingTimeString() {
        long hours = this.sleepingTime / 60 % 24;
        long mins = this.sleepingTime - ( hours * 60 );
        return hours + "h " + mins + "min";
    }

    /**
     * Return SleepNote object's sleeping time in minutes.
     * @return long
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private long getSleepingTimeInMinutes() {
        Duration duration = Duration.between(sleepTimeDate, wakeUpTimeDate);
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
     * @return String
     */
    public String getQuality() {
        return this.quality;
    }

    /**
     * Return SleepNote object in String format.
     * @return String
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public String toString() {
        return sleepTimeDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy EE")) + ": " + this.getSleepingTimeString();
    }
}
