package com.wordpress.httpsbenjaminsterner.moodtracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;

import static java.lang.String.valueOf;

public class AddMoodSelectSeverity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mood_select_severity);
    }
    public void GoHome(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void SelectSeverity(View view){
        Intent hasReasonIntent = new Intent(this, AddMoodSelectHasReason.class);
        Spinner severitySpinner = (Spinner)findViewById(R.id.severity_spinner);
        Integer selectedSeverity = Integer.parseInt(valueOf(severitySpinner.getSelectedItem()));
        Intent thisIntent = getIntent();
        String selectedMood = thisIntent.getStringExtra("EXTRA_MOOD");
        hasReasonIntent.putExtra("EXTRA_SEVERITY",selectedSeverity);
        hasReasonIntent.putExtra("EXTRA_MOOD", selectedMood);
        startActivity(hasReasonIntent);
    }
}
