package com.example.myapplication;

import static com.example.myapplication.DarkModeManager.IS_DARK_MODE_ON;
import static com.example.myapplication.DarkModeManager.setDarkMode;
import static com.example.myapplication.SharedPref.DARK_MODE;

import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    private TextView textView;
    private Switch darkSwitch;

    SharedPref sharedPref;


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        sharedPref = new SharedPref(this);

        textView = (TextView) findViewById(R.id.textView2);
        darkSwitch = (Switch) findViewById(R.id.dark_switch);


        darkSwitch.setChecked(sharedPref.read(DARK_MODE, true));

        darkSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initViews();
            }
        });
    }

    private void initViews() {
        if (darkSwitch.isChecked()) {
            setDarkMode(true, sharedPref);
        } else {
            setDarkMode(false, sharedPref);
        }
        recreate();
    }
}
