package com.example.unikaveri;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class SettingsActivity extends AppCompatActivity {

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if(findViewById(R.id.fragment_container)!=null)
        {
            if(savedInstanceState !=null)
                return;

            getFragmentManager().beginTransaction().add(R.id.fragment_container,new SettingsFragment()).commit();
        }
        bottomNavigation();

        updateUI();
    }


    /**
     * bottom navigation
     * TODO: copy-paste this function to other activities
     */
    private void bottomNavigation(){
        BottomNavigationView navi = findViewById(R.id.bottomNavigationView);

        // TODO: set selected activity for menu highlight
        navi.setSelectedItemId(R.id.settings);

        //item select listener
        //my implementation of this is absolute aids
        //sorry for anyone who has to look at this
        navi.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //loop through menu items
                switch(item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        return true;
                    case R.id.calendar:
                        startActivity(new Intent(getApplicationContext(),CalendarActivity.class));
                        return true;
                    case R.id.charts:
                        startActivity(new Intent(getApplicationContext(),ChartsActivity.class));
                        return true;
                }
                return false;
            }
        });
    }


    private void updateUI() {

    }

}
