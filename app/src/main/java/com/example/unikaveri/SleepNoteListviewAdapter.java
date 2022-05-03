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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Custom ArrayAdapter.
 * @author Kerttu
 */
public class SleepNoteListviewAdapter extends ArrayAdapter<SleepNote> {
    private Context mContext;
    private int mResource;

    /**
     * Define SleepNoteAdapter.
     * @param context Context
     * @param resource int
     * @param objects List<SleepNote>
     */
    public SleepNoteListviewAdapter(@NonNull Context context, int resource, @NonNull List<SleepNote> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    /**
     * Returns View for ListView to show data from SleepNote object.
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
        LocalDateTime date = getItem(position).getDate();
        LocalDateTime sleeptime = getItem(position).getSleepTimeDate();
        LocalDateTime waketime = getItem(position).getWakeTimeDate();

        // Set texts to widgets and format LocalDateTime variables
        dateTv.setText(date.format(DateTimeFormatter.ofPattern("d.M.yyyy EE")));
        sleepingTimeTv.setText(getItem(position).getSleepingTimeString());
        sleepTimeTv.setText(sleeptime.format(DateTimeFormatter.ofPattern("HH:mm")));
        wakeTimeTv.setText(waketime.format(DateTimeFormatter.ofPattern("HH:mm")));

        return convertView;
    }
}