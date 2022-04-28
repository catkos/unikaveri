package com.example.unikaveri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class ChartsActivity extends AppCompatActivity {

    private CurrentTime time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);

        // set bottomNavigation item activities
        bottomNavigation();

        // create time object (for month get)
        time = new CurrentTime();
        // set month and year in UI
        setMonthYearUI();

    }

    /* bottom navigation */
    private void bottomNavigation(){
        BottomNavigationView navi = findViewById(R.id.bottomNavigationView);

        // TODO: set selected activity for menu highlight
        navi.setSelectedItemId(R.id.charts);

        //item select listener
        navi.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //loop through menu items
                switch(item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        return true;
                    case R.id.calendar:
                        Log.d("menu calendar",""); //TODO:<- delete, add startActivity
                        return true;
                    case R.id.settings:
                        Log.d("menu settings",""); //TODO:<- delete, add startActivity
                        return true;
                }
                return false;
            }
        });
    }

    /* set/edit current month and year UI textview */
    private void setMonthYearUI(){
        TextView editText = (TextView) findViewById(R.id.textMonth);
        editText.setText(time.getMonthYear().toUpperCase());
    }
}