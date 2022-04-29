package com.example.unikaveri;

import android.os.Build;
import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * SleepNote singleton.
 * @author Kerttu
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class SleepNoteGlobalModel {
    private final List<SleepNote> sleepNotesList;
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
        sortList();
    }

    /**
     * Return List<SleepNote> with all SleepNote object's in list.
     *
     * @return List<SleepNote>
     */
    List<SleepNote> getAllSleepNotesList() { return this.sleepNotesList; }

    /**
     * Return List<SleepNote> of SleepNote object's with same month and year as the dateToCompare param has.
     *
     * @param dateToCompare LocalDateTime
     * @return List<SleepNote>
     */
    List<SleepNote> getListByMonthAndYear(LocalDateTime dateToCompare) {
        sortList();
        List<SleepNote> monthlySleepNotesList = new ArrayList<>();
        String dateToCompareStr = dateToCompare.format(DateTimeFormatter.ofPattern("MM yyyy"));

        // Loop through allSleepNotesList and compare SleepNote object's date in MM yyyy format to dateToCompareStr.
        for (int i = 0; i < this.sleepNotesList.size(); i++) {
            String dateStr = this.sleepNotesList.get(i).getDate().format(DateTimeFormatter.ofPattern("MM yyyy"));

            if (dateStr.equals(dateToCompareStr)) {
                monthlySleepNotesList.add(this.sleepNotesList.get(i));
            }
        }

        return monthlySleepNotesList;
    }

    /**
     * Sort sleepsList chronologically as the newest record as first on list.
     */
    private void sortList() {
        Comparator<SleepNote> comparator = (c1, c2) -> {
            return c1.getDate().compareTo(c2.getDate());
        };
        Collections.sort(this.sleepNotesList, comparator);
        Collections.reverse(this.sleepNotesList);
    }
}
