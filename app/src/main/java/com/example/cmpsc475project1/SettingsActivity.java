package com.example.cmpsc475project1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

public class SettingsActivity extends AppCompatActivity {
    private SwitchCompat soundSwitch;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        soundSwitch = findViewById(R.id.soundSwitch);
        radioGroup = findViewById(R.id.radioGroup);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences("MySettings", MODE_PRIVATE);
        boolean switchSetting = sharedPreferences.getBoolean("switch_setting", true);
        int radioButtonId = sharedPreferences.getInt("radio_button", R.id.ninjaRadioButton);

        soundSwitch.setChecked(switchSetting);
        radioGroup.check(radioButtonId);
    }

    public void mainMenuOnClick(View view) {
        finish();
    }

    public void saveSettingsOnClick(View view) {
        saveSettings();
    }

    private void saveSettings() {
        SharedPreferences sharedPreferences = getSharedPreferences("MySettings", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("switch_setting", soundSwitch.isChecked());
        editor.putInt("radio_button", radioGroup.getCheckedRadioButtonId());
        editor.apply();
    }
}
