package com.wordpress.httpsbenjaminsterner.moodtracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

public class addMood extends AppCompatActivity {

    private boolean hasReason = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mood);

    }
    public void onRadioButtonClicked(View view){
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()){
            case R.id.radio_has_reason:
                if(checked){
                    hasReason = true;
                }
                break;
            case R.id.radio_no_reason:
                if(checked){
                    hasReason = false;
                }
                break;
        }
    }
    public void sendMessage(View view){
        Intent intent = new Intent(this, MoodDatabase.class);
        EditText editText = (EditText) findViewById(R.id.reason);
        String reason = editText.getText().toString();
        Spinner spinner = (Spinner) findViewById(R.id.mood_spinner);
        String mood = String.valueOf(spinner.getSelectedItem());
        intent.putExtra("EXTRA_REASON", reason);
        intent.putExtra("EXTRA_HAS_REASON", hasReason);
        intent.putExtra("EXTRA_MOOD", mood);
        startActivity(intent);
    }

}
