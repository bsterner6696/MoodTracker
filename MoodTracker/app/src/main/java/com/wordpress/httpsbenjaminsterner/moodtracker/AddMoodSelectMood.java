package com.wordpress.httpsbenjaminsterner.moodtracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;

public class AddMoodSelectMood extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mood_select_mood);
    }
    public void GoHome(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void SelectMood(View view){
        Intent severityIntent = new Intent(this, AddMoodSelectSeverity.class);
        Spinner moodSpinner = (Spinner)findViewById(R.id.mood_spinner);
        String selectedMood = String.valueOf(moodSpinner.getSelectedItem());
        severityIntent.putExtra("EXTRA_MOOD",selectedMood);
        startActivity(severityIntent);
    }
}
