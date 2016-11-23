package com.wordpress.httpsbenjaminsterner.moodtracker;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

public class EndOfDayCheckIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_of_day_check_in);
    }
    public static class CheckIns implements BaseColumns {
        public static final String TABLE_NAME = "checkIns";
        public static final String COLUMN_NAME_DIET = "diet";
        public static final String COLUMN_NAME_ACTIVITY = "activity";
        public static final String COLUMN_NAME_DATE = "checkInDate";
    }
    private static final String CHECKINS_CREATE_ENTRIES = "CREATE TABLE IF NOT EXISTS " + CheckIns.TABLE_NAME + " (" + CheckIns._ID + " INTEGER PRIMARY KEY," + CheckIns.COLUMN_NAME_DIET + " VARCHAR ," + CheckIns.COLUMN_NAME_ACTIVITY + " DECIMAL,"
            + CheckIns.COLUMN_NAME_DATE + " DATE" + " )";

    public static String CreateDatabase(){
        return CHECKINS_CREATE_ENTRIES;
    }
    public void addCheckIn(View view){
        Intent intent = new Intent(this, DisplayDatabaseActivity.class);
        DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        month++;
        String checkInDate = year + "-" + month +"-" + day;


        Spinner dietSpinner = (Spinner) findViewById(R.id.diet_spinner);
        String diet = getResources().getStringArray(R.array.diet_values_array)[dietSpinner.getSelectedItemPosition()];

        EditText hoursOfActivityView = (EditText) findViewById(R.id.hours_of_activity);
        String hoursOfActivity = hoursOfActivityView.getText().toString();
        String regex = "'";
        char[] characterArray = regex.toCharArray();
        for (char character : characterArray){
            hoursOfActivity = hoursOfActivity.replace(""+character, "");
        }

        SQLiteDatabase checkInsDB = null;
        try {
            checkInsDB = this.openOrCreateDatabase("checkIns", MODE_PRIVATE, null);
            checkInsDB.execSQL(CHECKINS_CREATE_ENTRIES);

            checkInsDB.delete("checkIns", "checkInDate=?", new String[] {checkInDate});


            String CHECKINS_ADD_ENTRY = "INSERT INTO " + CheckIns.TABLE_NAME + " (" + CheckIns.COLUMN_NAME_DIET + ", " + CheckIns.COLUMN_NAME_ACTIVITY + ", " + CheckIns.COLUMN_NAME_DATE + ") VALUES ('"
                    + diet + "', '" + hoursOfActivity + "', '"+ checkInDate +"');";
            checkInsDB.execSQL(CHECKINS_ADD_ENTRY);
        } catch (Exception e) {
            Log.e("Error", "Error", e);
        } finally {
            if (checkInsDB != null)
                checkInsDB.close();
        }

        startActivity(intent);

    }

}
