package com.wordpress.httpsbenjaminsterner.moodtracker;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by Benjamin on 11/21/2016.
 */

public final class MoodDatabase extends Activity {

    public static class Moods implements BaseColumns {
        public static final String TABLE_NAME = "moods";
        public static final String COLUMN_NAME_MOOD = "mood";
        public static final String COLUMN_NAME_HASREASON = "hasReason";
        public static final String COLUMN_NAME_REASON = "reason";
        public static final String COLUMN_NAME_TIME = "time";
    }
    private static final String MOODS_CREATE_ENTRIES = "CREATE TABLE IF NOT EXISTS " + Moods.TABLE_NAME + " (" + Moods._ID + " INTEGER PRIMARY KEY," + Moods.COLUMN_NAME_MOOD + " VARCHAR ," + Moods.COLUMN_NAME_HASREASON + " BOOLEAN,"
            + Moods.COLUMN_NAME_REASON + " VARCHAR," + Moods.COLUMN_NAME_TIME + " TIMESTAMP" + " )";
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        SQLiteDatabase moodsDB = null;
        try {
            moodsDB = this.openOrCreateDatabase("moods", MODE_PRIVATE, null);
            moodsDB.execSQL(MOODS_CREATE_ENTRIES);
            Intent intent = getIntent();
            String mood = intent.getStringExtra("EXTRA_MOOD");
            boolean hasReason = intent.getBooleanExtra("EXTRA_HAS_REASON",false);
            String reason = intent.getStringExtra("EXTRA_REASON");
            String regx = "'";
            char[] characterArray = regx.toCharArray();
            for (char character : characterArray){
                reason = reason.replace(""+character, "");
            }
            String MOODS_ADD_ENTRY = "INSERT INTO " + Moods.TABLE_NAME + " (" + Moods.COLUMN_NAME_MOOD + ", " + Moods.COLUMN_NAME_HASREASON + ", " + Moods.COLUMN_NAME_REASON + ", " + Moods.COLUMN_NAME_TIME + ") VALUES ('"
                    + mood + "', '" + hasReason + "', '" + reason + "', " + "CURRENT_TIMESTAMP );";
            moodsDB.execSQL(MOODS_ADD_ENTRY);
        } catch (Exception e) {
            Log.e("Error", "Error", e);
        } finally {
            if (moodsDB != null)
                moodsDB.close();
        }

        Intent newIntent = new Intent(this, DisplayDatabaseActivity.class);
        newIntent.putExtra("EXTRA_MESSAGE", "Database Updated");
        startActivity(newIntent);

    }

}
