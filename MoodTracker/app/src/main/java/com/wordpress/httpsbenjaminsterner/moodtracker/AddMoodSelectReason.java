package com.wordpress.httpsbenjaminsterner.moodtracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AddMoodSelectReason extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mood_select_reason);
    }
    public void GoHome(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void SelectReason(View view){
        Intent moodIntent = new Intent(this, addMood.class);
        Intent thisIntent = getIntent();
        Integer selectedSeverity = thisIntent.getIntExtra("EXTRA_SEVERITY", 1);
        String selectedMood = thisIntent.getStringExtra("EXTRA_MOOD");
        Boolean selectedHasReason = thisIntent.getBooleanExtra("EXTRA_HAS_REASON",true);
        EditText reasonEditText = (EditText)findViewById(R.id.reason);
        String reason = reasonEditText.getText().toString();
        moodIntent.putExtra("EXTRA_SEVERITY", selectedSeverity);
        moodIntent.putExtra("EXTRA_MOOD", selectedMood);
        moodIntent.putExtra("EXTRA_HAS_REASON", selectedHasReason);
        moodIntent.putExtra("EXTRA_REASON", reason);
        startActivity(moodIntent);

    }
}
