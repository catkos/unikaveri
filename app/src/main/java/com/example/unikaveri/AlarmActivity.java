package com.example.unikaveri;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * AlarmActivity to start when user clicks alarm notification.
 * @author Kerttu
 */
public class AlarmActivity extends AppCompatActivity {
    private BroadcastReceiver minuteUpdate;

    private CurrentTime time;

    private View clockIcon;
    private TextView clock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        time = new CurrentTime();
        initWidgets();
        animateAlarmIcon();
        setClockTextView(time);
        startMinuteUpdater();
    }

    /**
     * Initialize clockIcon View and clock TextView.
     */
    private void initWidgets() {
        clockIcon = findViewById(R.id.clockIcon);
        clock = findViewById(R.id.clockTextView);
    }

    /**
     * Set given time to clock TextView.
     * @param time CurrentTime - The time to set to clock TextView.
     */
    private void setClockTextView(CurrentTime time) {
        clock.setText(time.getCurrentTime());
    }

    /**
     * Start UI update, refresh every 1 minute
     */
    public void startMinuteUpdater() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        minuteUpdate = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //override/create new object (so it can get current phone time)
                time = new CurrentTime();
                //set new time in UI
                setClockTextView(time);
            }
        };
        registerReceiver(minuteUpdate, intentFilter);
    }

    /**
     * Button functionalities.
     * Is stopAlarmButton is clicked: Stop AlarmService and finish this activity.
     * @param v View - The clicked button.
     */
    public void buttonPressed(View v) {
        int id = v.getId();

        if (id == R.id.stopAlarmButton) {
            Intent intentService = new Intent(getApplicationContext(), AlarmService.class);
            getApplicationContext().stopService(intentService);
            finish();
        }
    }

    /**
     * Rotation animation for clockIcon.
     */
    public void animateAlarmIcon() {
        ObjectAnimator rotate = ObjectAnimator.ofFloat(clockIcon, "rotation", 0f, 10f, 0f, -10f, 0f);
        rotate.setRepeatCount(ValueAnimator.INFINITE);
        rotate.setDuration(1000);
        rotate.start();
    }
}