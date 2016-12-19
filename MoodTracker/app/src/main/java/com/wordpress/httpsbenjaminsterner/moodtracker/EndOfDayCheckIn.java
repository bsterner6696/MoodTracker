package com.wordpress.httpsbenjaminsterner.moodtracker;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Button;

import java.util.Calendar;
import java.util.Date;

public class EndOfDayCheckIn extends AppCompatActivity {
    private String checkInDate;
    private String diet;
    private String hoursOfActivity;
    private String hoursOfSleep;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_of_day_check_in);
        TextView dietText = (TextView)findViewById(R.id.dietText);
        dietText.setMovementMethod(LinkMovementMethod.getInstance());
    }
    public static class CheckIns implements BaseColumns {
        public static final String TABLE_NAME = "checkIns";
        public static final String COLUMN_NAME_DIET = "diet";
        public static final String COLUMN_NAME_ACTIVITY = "activity";
        public static final String COLUMN_NAME_DATE = "checkInDate";
        public static final String COLUMN_NAME_SLEEP = "sleep";
    }
    private static final String CHECKINS_CREATE_ENTRIES = "CREATE TABLE IF NOT EXISTS " + CheckIns.TABLE_NAME + " (" + CheckIns._ID + " INTEGER PRIMARY KEY," + CheckIns.COLUMN_NAME_DIET + " VARCHAR ," + CheckIns.COLUMN_NAME_ACTIVITY + " DECIMAL,"
            +CheckIns.COLUMN_NAME_SLEEP + " DECIMAL, "+ CheckIns.COLUMN_NAME_DATE + " DATE" + " )";

    public static String CreateDatabase(){
        return CHECKINS_CREATE_ENTRIES;
    }

    public void SelectCheckInDate(View view){
        DatePicker checkInDatePicker = (DatePicker) findViewById(R.id.datePicker);
        TextView checkInDayText = (TextView)findViewById(R.id.checkInDayText);
        Button checkInDateButton = (Button)findViewById(R.id.selectCheckInDate);
        int day = checkInDatePicker.getDayOfMonth();
        int month = checkInDatePicker.getMonth();
        int year = checkInDatePicker.getYear();
        month++;
        checkInDate = year + "-" + month +"-" + day;
        checkInDatePicker.setVisibility(view.GONE);
        checkInDayText.setVisibility(view.GONE);
        checkInDateButton.setVisibility(view.GONE);
        TextView dietText = (TextView)findViewById(R.id.dietText);
        Spinner dietSpinner = (Spinner)findViewById(R.id.diet_spinner);
        Button dietButton = (Button)findViewById(R.id.selectDiet);
        dietText.setVisibility(view.VISIBLE);
        dietSpinner.setVisibility(view.VISIBLE);
        dietButton.setVisibility(view.VISIBLE);

    }
    public void SelectDiet(View view){
        TextView dietText = (TextView)findViewById(R.id.dietText);
        Spinner dietSpinner = (Spinner)findViewById(R.id.diet_spinner);
        Button dietButton = (Button)findViewById(R.id.selectDiet);
        diet = getResources().getStringArray(R.array.diet_values_array)[dietSpinner.getSelectedItemPosition()];
        dietText.setVisibility(view.GONE);
        dietSpinner.setVisibility(view.GONE);
        dietButton.setVisibility(view.GONE);
        TextView activityText = (TextView)findViewById(R.id.activityText);
        EditText activityEditText = (EditText)findViewById(R.id.hours_of_activity);
        Button activityButton = (Button)findViewById(R.id.selectActivity);
        activityText.setVisibility(view.VISIBLE);
        activityEditText.setVisibility(view.VISIBLE);
        activityButton.setVisibility(view.VISIBLE);
    }
    public void SelectActivity(View view){
        TextView activityText = (TextView)findViewById(R.id.activityText);
        EditText activityEditText = (EditText)findViewById(R.id.hours_of_activity);
        Button activityButton = (Button)findViewById(R.id.selectActivity);
        hoursOfActivity = activityEditText.getText().toString();
        activityText.setVisibility(view.GONE);
        activityEditText.setVisibility(view.GONE);
        activityButton.setVisibility(view.GONE);
        TextView sleepText = (TextView)findViewById(R.id.sleepText);
        EditText sleepEditText = (EditText) findViewById(R.id.hours_of_sleep);
        Button checkInButton = (Button)findViewById(R.id.addCheckIn);
        sleepText.setVisibility(view.VISIBLE);
        sleepEditText.setVisibility(view.VISIBLE);
        checkInButton.setVisibility(view.VISIBLE);

    }
    public void addCheckIn(View view){
        Intent intent = new Intent(this, DisplayDatabaseActivity.class);
        EditText sleepEditText = (EditText) findViewById(R.id.hours_of_sleep);
        hoursOfSleep = sleepEditText.getText().toString();
        String regex = "'";
        char[] characterArray = regex.toCharArray();
        for (char character : characterArray){
            hoursOfActivity = hoursOfActivity.replace(""+character, "");
            hoursOfSleep = hoursOfSleep.replace(""+character, "");
        }

        SQLiteDatabase checkInsDB = null;
        try {
            checkInsDB = this.openOrCreateDatabase("checkIns", MODE_PRIVATE, null);
            checkInsDB.execSQL(CHECKINS_CREATE_ENTRIES);

            checkInsDB.delete("checkIns", "checkInDate=?", new String[] {checkInDate});


            String CHECKINS_ADD_ENTRY = "INSERT INTO " + CheckIns.TABLE_NAME + " (" + CheckIns.COLUMN_NAME_DIET + ", " + CheckIns.COLUMN_NAME_ACTIVITY + ", " + CheckIns.COLUMN_NAME_DATE +","+CheckIns.COLUMN_NAME_SLEEP+") VALUES ('"
                    + diet + "', '" + hoursOfActivity + "', '"+ checkInDate +"', '"+hoursOfSleep+"');";
            checkInsDB.execSQL(CHECKINS_ADD_ENTRY);
        } catch (Exception e) {
            Log.e("Error", "Error", e);
        } finally {
            if (checkInsDB != null)
                checkInsDB.close();
        }
        intent.putExtra("EXTRA_MESSAGE", "Database Updated");
        startActivity(intent);

    }

    public void GoHome(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
