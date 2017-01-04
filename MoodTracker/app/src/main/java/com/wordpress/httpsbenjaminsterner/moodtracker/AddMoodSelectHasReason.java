package com.wordpress.httpsbenjaminsterner.moodtracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Spinner;

import static java.lang.String.valueOf;

public class AddMoodSelectHasReason extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mood_select_has_reason);
    }

    public boolean selectedHasReason;

    public void onRadioButtonClicked(View view){
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()){
            case R.id.radio_has_reason:
                if(checked){
                    selectedHasReason = true;
                }
                break;
            case R.id.radio_no_reason:
                if(checked){
                    selectedHasReason = false;
                }
                break;
        }
    }
    public void GoHome(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void SelectHasReason(View view){
        Intent hasReasonIntent = new Intent(this, AddMoodSelectHasReason.class);
        Intent thisIntent = getIntent();
        Integer selectedSeverity = thisIntent.getIntExtra("EXTRA_SEVERITY", 1);
        String selectedMood = thisIntent.getStringExtra("EXTRA_MOOD");
        if (selectedHasReason){
            Intent reasonIntent = new Intent(this, AddMoodSelectReason.class);
            reasonIntent.putExtra("EXTRA_SEVERITY", selectedSeverity);
            reasonIntent.putExtra("EXTRA_MOOD", selectedMood);
            reasonIntent.putExtra("EXTRA_HAS_REASON", selectedHasReason);
            startActivity(reasonIntent);
        } else{
            Intent moodIntent = new Intent(this, addMood.class);
            moodIntent.putExtra("EXTRA_SEVERITY", selectedSeverity);
            moodIntent.putExtra("EXTRA_MOOD", selectedMood);
            moodIntent.putExtra("EXTRA_HAS_REASON", selectedHasReason);
            moodIntent.putExtra("EXTRA_REASON", "");
            startActivity(moodIntent);
        }

    }
}
