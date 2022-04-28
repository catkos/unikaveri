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
    private final List<SleepNote> allSleepNotesList;
    private final List<SleepNote> monthlySleepNotesList;
    private static final SleepNoteGlobalModel ourInstance = new SleepNoteGlobalModel();

    public static SleepNoteGlobalModel getInstance() {
        return ourInstance;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private SleepNoteGlobalModel() {
        this.allSleepNotesList = new ArrayList<>();
        this.monthlySleepNotesList = new ArrayList<>();

        // MOCKUP DATA. TODO: Delete!
        LocalDateTime sleepTimeDateTmp = LocalDateTime.of(2022,4,25,23,55);
        LocalDateTime wakeTimeDateTmp = LocalDateTime.of(2022,4,26,6,55);
        allSleepNotesList.add(new SleepNote(sleepTimeDateTmp, wakeTimeDateTmp, 2, "Tyydyttävä"));

        sleepTimeDateTmp = LocalDateTime.of(2022,4,11,23,55);
        wakeTimeDateTmp = LocalDateTime.of(2022,4,12,7,55);
        allSleepNotesList.add(new SleepNote(sleepTimeDateTmp, wakeTimeDateTmp, 2, "Tyydyttävä"));

        sleepTimeDateTmp = LocalDateTime.of(2022,4,24,22,55);
        wakeTimeDateTmp = LocalDateTime.of(2022,4,25,7,55);
        allSleepNotesList.add(new SleepNote(sleepTimeDateTmp, wakeTimeDateTmp, 2, "Tyydyttävä"));

        sleepTimeDateTmp = LocalDateTime.of(2022,3,24,22,55);
        wakeTimeDateTmp = LocalDateTime.of(2022,3,25,7,55);
        allSleepNotesList.add(new SleepNote(sleepTimeDateTmp, wakeTimeDateTmp, 2, "Hyvä"));

        sortList();
    }

    /**
     * Return List<SleepNote> with all SleepNote object's in list.
     *
     * @return List<SleepNote>
     */
    List<SleepNote> getAllSleepNotesList() { return this.allSleepNotesList; }

    /**
     * Return List<SleepNote> of SleepNote object's with same month and year as the dateToCompare param has.
     *
     * @param dateToCompare LocalDateTime
     * @return List<SleepNote>
     */
    List<SleepNote> getListByMonthAndYear(LocalDateTime dateToCompare) {
        this.monthlySleepNotesList.clear();
        String dateToCompareStr = dateToCompare.format(DateTimeFormatter.ofPattern("MM yyyy"));

        // Loop through allSleepNotesList and compare SleepNote object's date in MM yyyy format to dateToCompareStr.
        for (int i = 0; i < this.allSleepNotesList.size(); i++) {
            String dateStr = this.allSleepNotesList.get(i).getDate().format(DateTimeFormatter.ofPattern("MM yyyy"));

            if (dateStr.equals(dateToCompareStr)) {
                this.monthlySleepNotesList.add(this.allSleepNotesList.get(i));
            }
        }

        return this.monthlySleepNotesList;
    }

    /**
     * Sort sleepsList chronologically as the newest record as first on list.
     */
    private void sortList() {
        Comparator<SleepNote> comparator = (c1, c2) -> {
            return c1.getDate().compareTo(c2.getDate());
        };
        Collections.sort(this.allSleepNotesList, comparator);
        Collections.reverse(this.allSleepNotesList);
    }
}
