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
    private Button homeButton;
    private TextView checkInDayText;
    private DatePicker checkInDatePicker;
    private Button checkInDateButton;
    private TextView dietText;
    private Spinner dietSpinner;
    private Button dietButton;
    private TextView activityText;
    private EditText activityEditText;
    private Button activityButton;
    private TextView sleepText;
    private EditText sleepEditText;
    private Button checkInButton;
    private String checkInDate;
    private String diet;
    private String hoursOfActivity;
    private String hoursOfSleep;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_of_day_check_in);
        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_end_of_day_check_in);
        checkInDatePicker = (DatePicker) findViewById(R.id.datePicker);
        homeButton = (Button)findViewById(R.id.homeButton);
        checkInDayText = (TextView)findViewById(R.id.checkInDayText);
        checkInDateButton = (Button)findViewById(R.id.selectCheckInDate);
        dietText = (TextView)findViewById(R.id.dietText);
        dietText.setMovementMethod(LinkMovementMethod.getInstance());
        dietSpinner = (Spinner)findViewById(R.id.diet_spinner);
        dietButton = (Button)findViewById(R.id.selectDiet);
        activityText = (TextView)findViewById(R.id.activityText);
        activityEditText = (EditText)findViewById(R.id.hours_of_activity);
        activityButton = (Button)findViewById(R.id.selectActivity);
        sleepText = (TextView)findViewById(R.id.sleepText);
        sleepEditText = (EditText) findViewById(R.id.hours_of_sleep);
        checkInButton = (Button)findViewById(R.id.addCheckIn);
        layout.removeAllViews();
        layout.addView(homeButton);
        layout.addView(checkInDayText);
        layout.addView(checkInDatePicker);
        layout.addView(checkInDateButton);
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
        int day = checkInDatePicker.getDayOfMonth();
        int month = checkInDatePicker.getMonth();
        int year = checkInDatePicker.getYear();
        month++;
        checkInDate = year + "-" + month +"-" + day;
        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_end_of_day_check_in);
        layout.removeAllViews();
        layout.addView(homeButton);
        layout.addView(dietText);
        layout.addView(dietSpinner);
        layout.addView(dietButton);
    }
    public void SelectDiet(View view){
        diet = getResources().getStringArray(R.array.diet_values_array)[dietSpinner.getSelectedItemPosition()];
        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_end_of_day_check_in);
        layout.removeAllViews();
        layout.addView(homeButton);
        layout.addView(activityText);
        layout.addView(activityEditText);
        layout.addView(activityButton);
    }
    public void SelectActivity(View view){
        hoursOfActivity = activityEditText.getText().toString();
        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_end_of_day_check_in);
        layout.removeAllViews();
        layout.addView(homeButton);
        layout.addView(sleepText);
        layout.addView(sleepEditText);
        layout.addView(checkInButton);
    }
    public void addCheckIn(View view){
        Intent intent = new Intent(this, DisplayDatabaseActivity.class);
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
