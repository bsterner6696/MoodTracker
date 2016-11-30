package com.wordpress.httpsbenjaminsterner.moodtracker;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

public class addEvent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
    }

    public static class Events implements BaseColumns {
        public static final String TABLE_NAME = "events";
        public static final String COLUMN_NAME_EVENT = "event";
        public static final String COLUMN_NAME_EFFECT = "effect";
        public static final String COLUMN_NAME_TIME = "time";
    }
    private static final String EVENTS_CREATE_ENTRIES = "CREATE TABLE IF NOT EXISTS " + Events.TABLE_NAME + " (" + Events._ID + " INTEGER PRIMARY KEY," + Events.COLUMN_NAME_EVENT + " VARCHAR ," + Events.COLUMN_NAME_EFFECT + " VARCHAR,"
            + Events.COLUMN_NAME_TIME + " TIMESTAMP" + " )";

    public void sendMessage(View view){
        Intent intent = new Intent(this, DisplayDatabaseActivity.class);
        EditText editText = (EditText) findViewById(R.id.event);
        String Event = editText.getText().toString();
        Spinner spinner = (Spinner) findViewById(R.id.effect_spinner);
        String Effect = String.valueOf(spinner.getSelectedItem());

        SQLiteDatabase eventsDB = null;
        try {
            eventsDB = this.openOrCreateDatabase("events", MODE_PRIVATE, null);
            eventsDB.execSQL(EVENTS_CREATE_ENTRIES);
            String event = Event;
            String effect = Effect;
            String regex = "'";
            char[] characterArray = regex.toCharArray();
            for (char character : characterArray){
                event = event.replace(""+character, "");
            }
            String EVENTS_ADD_ENTRY = "INSERT INTO " + Events.TABLE_NAME + " (" + Events.COLUMN_NAME_EVENT + ", " + Events.COLUMN_NAME_EFFECT + ", " + Events.COLUMN_NAME_TIME + ") VALUES ('"
                    + event + "', '" + effect + "', CURRENT_TIMESTAMP );";
            eventsDB.execSQL(EVENTS_ADD_ENTRY);
        } catch (Exception e) {
            Log.e("Error", "Error", e);
        } finally {
            if (eventsDB != null)
                eventsDB.close();
        }


        intent.putExtra("EXTRA_MESSAGE", "Database Updated");
        startActivity(intent);
    }
}
