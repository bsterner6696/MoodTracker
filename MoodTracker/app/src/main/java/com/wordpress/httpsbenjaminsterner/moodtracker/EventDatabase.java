package com.wordpress.httpsbenjaminsterner.moodtracker;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by Benjamin on 11/22/2016.
 */

public final class EventDatabase extends Activity{

    public static class Events implements BaseColumns {
        public static final String TABLE_NAME = "events";
        public static final String COLUMN_NAME_EVENT = "event";
        public static final String COLUMN_NAME_EFFECT = "effect";
        public static final String COLUMN_NAME_TIME = "time";
    }
    private static final String EVENTS_CREATE_ENTRIES = "CREATE TABLE IF NOT EXISTS " + EventDatabase.Events.TABLE_NAME + " (" + EventDatabase.Events._ID + " INTEGER PRIMARY KEY," + EventDatabase.Events.COLUMN_NAME_EVENT + " VARCHAR ," + Events.COLUMN_NAME_EFFECT + " VARCHAR,"
            + EventDatabase.Events.COLUMN_NAME_TIME + " TIMESTAMP" + " )";

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        SQLiteDatabase eventsDB = null;
        try {
            eventsDB = this.openOrCreateDatabase("events", MODE_PRIVATE, null);
            eventsDB.execSQL(EVENTS_CREATE_ENTRIES);
            Intent intent = getIntent();
            String event = intent.getStringExtra("EXTRA_EVENT");
            String effect = intent.getStringExtra("EXTRA_EFFECT");
            String regex = "'";
            char[] characterArray = regex.toCharArray();
            for (char character : characterArray){
                event = event.replace(""+character, "");
            }
            String EVENTS_ADD_ENTRY = "INSERT INTO " + EventDatabase.Events.TABLE_NAME + " (" + EventDatabase.Events.COLUMN_NAME_EVENT + ", " + EventDatabase.Events.COLUMN_NAME_EFFECT + ", " + EventDatabase.Events.COLUMN_NAME_TIME + ") VALUES ('"
                    + event + "', '" + effect + "', CURRENT_TIMESTAMP );";
            eventsDB.execSQL(EVENTS_ADD_ENTRY);
        } catch (Exception e) {
            Log.e("Error", "Error", e);
        } finally {
            if (eventsDB != null)
                eventsDB.close();
        }

        Intent newIntent = new Intent(this, DisplayDatabaseActivity.class);
        newIntent.putExtra("EXTRA_MESSAGE", "Database Updated");
        startActivity(newIntent);

    }
}
