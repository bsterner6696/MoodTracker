package com.wordpress.httpsbenjaminsterner.moodtracker;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

public class addMood extends AppCompatActivity {

    private boolean selectedHasReason = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mood);

    }
    public static class Moods implements BaseColumns {
        public static final String TABLE_NAME = "moods";
        public static final String COLUMN_NAME_MOOD = "mood";
        public static final String COLUMN_NAME_HASREASON = "hasReason";
        public static final String COLUMN_NAME_REASON = "reason";
        public static final String COLUMN_NAME_TIME = "time";
    }
    private static final String MOODS_CREATE_ENTRIES = "CREATE TABLE IF NOT EXISTS " + addMood.Moods.TABLE_NAME + " (" + addMood.Moods._ID + " INTEGER PRIMARY KEY," + addMood.Moods.COLUMN_NAME_MOOD + " VARCHAR ," + addMood.Moods.COLUMN_NAME_HASREASON + " BOOLEAN,"
            + addMood.Moods.COLUMN_NAME_REASON + " VARCHAR," + addMood.Moods.COLUMN_NAME_TIME + " TIMESTAMP" + " )";


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
    public void sendMessage(View view){
        Intent intent = new Intent(this, DisplayDatabaseActivity.class);
        EditText reasonEditText = (EditText) findViewById(R.id.reason);
        String Reason = reasonEditText.getText().toString();
        Spinner moodSpinner = (Spinner) findViewById(R.id.mood_spinner);
        String Mood = String.valueOf(moodSpinner.getSelectedItem());

        SQLiteDatabase moodsDB = null;
        try {
            moodsDB = this.openOrCreateDatabase("moods", MODE_PRIVATE, null);
            moodsDB.execSQL(MOODS_CREATE_ENTRIES);
            String mood = Mood;
            boolean hasReason = selectedHasReason;
            String reason = Reason;
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

        intent.putExtra("EXTRA_MESSAGE", "Database Updated");
        startActivity(intent);
    }

}
