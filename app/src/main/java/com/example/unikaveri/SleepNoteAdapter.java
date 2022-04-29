package com.example.unikaveri;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Custom ArrayAdapter.
 * @author Kerttu
 */
public class SleepNoteAdapter extends ArrayAdapter<SleepNote> {
    private Context mContext;
    private int mResource;

    /**
     * Define SleepNoteAdapter.
     * @param context Context
     * @param resource int
     * @param objects List<SleepNote>
     */
    public SleepNoteAdapter(@NonNull Context context, int resource, @NonNull List<SleepNote> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    /**
     * Returns view for listview.
     * @param position int
     * @param convertView View
     * @param parent ViewGroup
     * @return View
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        convertView = layoutInflater.inflate(mResource, parent, false);

        // Initialize widgets
        TextView dateTv = convertView.findViewById(R.id.dateTextView);
        TextView sleepingTimeTv = convertView.findViewById(R.id.sleepingTimeHoursAndMinutesTextView);
        TextView sleepTimeTv = convertView.findViewById(R.id.sleepTimeTextView);
        TextView wakeTimeTv = convertView.findViewById(R.id.wakeTimeTextView);

        // Get dates
        LocalDate date = getItem(position).getDate();
        LocalDateTime sleeptime = getItem(position).getBedTimeDate();
        LocalDateTime waketime = getItem(position).getWakeUpTimeDate();

        // Set texts to widgets and format LocalDateTime variables
        dateTv.setText(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy EE")));
        sleepingTimeTv.setText(getItem(position).getSleepingTimeString());
        sleepTimeTv.setText(sleeptime.format(DateTimeFormatter.ofPattern("HH:mm")));
        wakeTimeTv.setText(waketime.format(DateTimeFormatter.ofPattern("HH:mm")));

        return convertView;
    }
}