package com.wordpress.httpsbenjaminsterner.moodtracker;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

public class AverageDay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_average_day);
    }
    public class averageDay implements BaseColumns {
        public static final String TABLE_NAME = "averageDay";
        public static final String COLUMN_NAME_DIET = "diet";
        public static final String COLUMN_NAME_ACTIVITY = "activity";
        public static final String COLUMN_NAME_SLEEP = "sleep";
    }

    private static final String AVERAGE_DAY_CREATE_ENTRIES = "CREATE TABLE IF NOT EXISTS " + averageDay.TABLE_NAME + " (" + averageDay._ID + " INTEGER PRIMARY KEY," + averageDay.COLUMN_NAME_DIET + " VARCHAR ," + averageDay.COLUMN_NAME_ACTIVITY + " DECIMAL,"
            + averageDay.COLUMN_NAME_SLEEP + " DECIMAL" + " )";

    private void setAverageDay(View view){
        Spinner dietSpinner = (Spinner) findViewById(R.id.diet_spinner);
        String diet = getResources().getStringArray(R.array.diet_values_array)[dietSpinner.getSelectedItemPosition()];

        EditText hoursOfActivityView = (EditText) findViewById(R.id.hours_of_activity);
        String hoursOfActivity = hoursOfActivityView.getText().toString();
        EditText hoursOfSleepView = (EditText) findViewById(R.id.hours_of_sleep);
        String hoursOfSleep = hoursOfSleepView.getText().toString();
        String regex = "'";
        char[] characterArray = regex.toCharArray();
        for (char character : characterArray){
            hoursOfSleep = hoursOfSleep.replace(""+character, "");
            hoursOfActivity = hoursOfActivity.replace(""+character, "");
        }
        String AVERAGE_DAY_ADD_ENTRY = "INSERT INTO " + averageDay.TABLE_NAME + " (" + averageDay.COLUMN_NAME_DIET + ", " + averageDay.COLUMN_NAME_ACTIVITY + ", " + averageDay.COLUMN_NAME_SLEEP + ") VALUES ('"
                + diet + "', '" + hoursOfActivity + "', '"+ hoursOfSleep +"');";
        SQLiteDatabase averageDayDB = null;
        try {
            averageDayDB = this.openOrCreateDatabase("averageDay", MODE_PRIVATE, null);
            averageDayDB.execSQL("DROP TABLE IF EXISTS averageDay");
            averageDayDB = this.openOrCreateDatabase("averageDay", MODE_PRIVATE, null);
            averageDayDB.execSQL(AVERAGE_DAY_CREATE_ENTRIES);
            averageDayDB.execSQL(AVERAGE_DAY_ADD_ENTRY);
        } catch (Throwable T) {
            System.out.print(T);
        } finally {
            if (averageDayDB != null)
                averageDayDB.close();
        }
    }
}
