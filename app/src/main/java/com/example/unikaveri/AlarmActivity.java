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
 *
 * @author Kerttu
 */
public class AlarmActivity extends AppCompatActivity {

    private BroadcastReceiver minuteUpdate;

    private CurrentTime time;

    private View clockIcon;
    private TextView clockTv;
    private TextView alarmMessageTv;

    private String message;

    /**
     * Get extras from intent, initialize widgets, animate alarm icon and start clock updater.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        Bundle bundle = getIntent().getExtras();

        // Get intent extras
        message = bundle.getString("message");

        // Get currentTime to show in clock TextView
        time = new CurrentTime();

        initWidgets();
        initClockTextView(time);
        animateAlarmIcon();
        startMinuteUpdater();
    }

    /**
     * Initialize clockIcon View, clockTv TextView and alarmMessageTv TextView.
     * Set message to alarmMessageTv.
     */
    private void initWidgets() {
        clockIcon = findViewById(R.id.clockIcon);
        clockTv = findViewById(R.id.clockTextView);
        alarmMessageTv = findViewById(R.id.alarmMessageTextView);
        alarmMessageTv.setText(message);
    }

    /**
     * Set given time to clock TextView.
     *
     * @param time The time to set to clock TextView.
     */
    private void initClockTextView(CurrentTime time) {
        clockTv.setText(time.getCurrentTime());
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
                initClockTextView(time);
            }
        };
        registerReceiver(minuteUpdate, intentFilter);
    }

    /**
     * Button functionalities.
     *
     * Is stopAlarmButton is clicked: Stop AlarmService and finish this activity.
     *
     * @param v The clicked button.
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
     * From here: https://learntodroid.com/how-to-create-a-simple-alarm-clock-app-in-android/
     */
    public void animateAlarmIcon() {
        ObjectAnimator rotate = ObjectAnimator.ofFloat(clockIcon, "rotation", 0f, 10f, 0f, -10f, 0f);
        rotate.setRepeatCount(ValueAnimator.INFINITE);
        rotate.setDuration(1000);
        rotate.start();
    }
}