package com.example.cmpsc475project1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Room;

import com.google.android.material.color.utilities.Score;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private int currentScore;
    private ScoreViewModel scoreViewModel;
    private SwitchCompat soundSwitch;
    private RadioGroup radioGroup;
    private SharedPreferences sharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View view = LayoutInflater.from(this).inflate(R.layout.settings, null);
        soundSwitch = view.findViewById(R.id.soundSwitch);
        radioGroup = view.findViewById(R.id.radioGroup);

        SharedPreferences sharedPreferences = getSharedPreferences("MySettings", MODE_PRIVATE);

        // Check if shared prefs exist first
        if (!sharedPreferences.contains("switch_setting") && !sharedPreferences.contains("radio_button")) {
            SharedPreferences.Editor editor = sharedPreferences.edit();

            // Setting default values
            editor.putBoolean("switch_setting", true);
            editor.putInt("radio_button", R.id.ninjaRadioButton);
            editor.apply();
        }

        ScoreDatabase db = Room.databaseBuilder(getApplicationContext(),
                ScoreDatabase.class, "score-database").allowMainThreadQueries().build();

    }

    @Override
    public void onPause() {
        super.onPause();
        saveSettings();
    }

    public void saveSettingsOnClick(View view) {
        saveSettings();
    }

    public void saveSettings() {
        SharedPreferences sharedPreferences = getSharedPreferences("MySettings", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("switch_setting", soundSwitch.isChecked());
        editor.putInt("radio_button", radioGroup.getCheckedRadioButtonId());
        editor.apply();
    }

    public class ScoreListAdapter extends RecyclerView.Adapter<ScoreListAdapter.ScoreViewHolder> {
        class ScoreViewHolder extends RecyclerView.ViewHolder {
            private final TextView nameView;
            private final TextView scoreView;
            private ScoreEntity score;

            private ScoreViewHolder(View itemView) {
                super(itemView);
                nameView = itemView.findViewById(R.id.listItemName);
                scoreView = itemView.findViewById(R.id.listItemNumber);
            }
        }


        private LayoutInflater layoutInflater;
        private List<ScoreEntity> scores;

        ScoreListAdapter(Context context) {
            layoutInflater = layoutInflater.from(context);
        }

        @Override
        public ScoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.list_item, parent, false);
            return new ScoreViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ScoreViewHolder holder, int position) {
            if (scores != null) {
                ScoreEntity current = scores.get(position);
                holder.score = current;
                holder.nameView.setText(current.name);
                holder.scoreView.setText(String.valueOf(current.score));
            } else {
                // Covers the case of data not being ready yet.
                holder.nameView.setText("...loading...");
                holder.scoreView.setText("???");

            }
        }

        void setScores(List<ScoreEntity> scores) {
            this.scores = scores;
            notifyDataSetChanged();;
        }

        @Override
        public int getItemCount() {
            if (scores != null)
                return scores.size();
            else
                return 0;
        }
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentScore", currentScore);
    }

    public void mainMenuOnClick(View view) {
        setContentView(R.layout.activity_main);
    }

    public void startOnClick(View view) {
        // TODO: actual gameplay layout
    }

    public void howToOnClick(View view) {
        setContentView(R.layout.how_to_play);
    }

    public void settingsOnClick(View view) {
        View settingsView = LayoutInflater.from(this).inflate(R.layout.settings, null);

        soundSwitch = settingsView.findViewById(R.id.soundSwitch);
        radioGroup = settingsView.findViewById(R.id.radioGroup);

        SharedPreferences sharedPreferences = getSharedPreferences("MySettings", MODE_PRIVATE);
        boolean switchSetting = sharedPreferences.getBoolean("switch_setting", true);
        int radioButtonId = sharedPreferences.getInt("radio_button", radioGroup.getCheckedRadioButtonId());

        soundSwitch.setChecked(switchSetting);
        radioGroup.check(radioButtonId);

        setContentView(settingsView);

    }

    public void leaderboardOnClick(View view) {
        setContentView(R.layout.leaderboard);

        View scoresListView = LayoutInflater.from(this).inflate(R.layout.leaderboard, null);

        RecyclerView recyclerView = scoresListView.findViewById(R.id.lstScores);
        ScoreListAdapter adapter = new ScoreListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        scoreViewModel = new ViewModelProvider(this).get(ScoreViewModel.class);
        scoreViewModel.getScores().observe(this, adapter::setScores);

        setContentView(scoresListView);
    }

    public void quitOnClick(View view) {
        // Quit game
        finish();
        System.exit(0);
    }
}