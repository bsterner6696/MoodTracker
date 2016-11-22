package com.wordpress.httpsbenjaminsterner.moodtracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

public class addEvent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
    }

    public void sendMessage(View view){
        Intent intent = new Intent(this, EventDatabase.class);
        EditText editText = (EditText) findViewById(R.id.event);
        String event = editText.getText().toString();
        Spinner spinner = (Spinner) findViewById(R.id.effect_spinner);
        String effect = String.valueOf(spinner.getSelectedItem());
        intent.putExtra("EXTRA_EVENT", event);
        intent.putExtra("EXTRA_EFFECT", effect);
        startActivity(intent);
    }
}
