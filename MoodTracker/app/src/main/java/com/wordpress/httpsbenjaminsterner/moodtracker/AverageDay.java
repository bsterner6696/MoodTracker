package com.wordpress.httpsbenjaminsterner.moodtracker;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

public class AverageDay extends AppCompatActivity {
    private String diet;
    private String hoursOfActivity;
    private String hoursOfSleep;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_average_day);
        TextView dietText = (TextView)findViewById(R.id.dietText);
        dietText.setMovementMethod(LinkMovementMethod.getInstance());
    }
    public class averageDay implements BaseColumns {
        public static final String TABLE_NAME = "averageDay";
        public static final String COLUMN_NAME_DIET = "diet";
        public static final String COLUMN_NAME_ACTIVITY = "activity";
        public static final String COLUMN_NAME_SLEEP = "sleep";
    }

    private static final String AVERAGE_DAY_CREATE_ENTRIES = "CREATE TABLE IF NOT EXISTS " + averageDay.TABLE_NAME + " (" + averageDay._ID + " INTEGER PRIMARY KEY," + averageDay.COLUMN_NAME_DIET + " VARCHAR ," + averageDay.COLUMN_NAME_ACTIVITY + " DECIMAL,"
            + averageDay.COLUMN_NAME_SLEEP + " DECIMAL" + " )";

    public void selectDiet(View view){
        TextView dietText = (TextView)findViewById(R.id.dietText);
        Spinner dietSpinner = (Spinner)findViewById(R.id.diet_spinner);
        Button dietButton = (Button) findViewById(R.id.dietButton);
        diet = getResources().getStringArray(R.array.diet_values_array)[dietSpinner.getSelectedItemPosition()];
        dietText.setVisibility(view.GONE);
        dietSpinner.setVisibility(view.GONE);
        dietButton.setVisibility(view.GONE);
        TextView activityText=(TextView)findViewById(R.id.activityText);
        EditText activityEditText =(EditText)findViewById(R.id.hours_of_activity);
        Button activityButton = (Button)findViewById(R.id.activityButton);
        activityText.setVisibility(view.VISIBLE);
        activityEditText.setVisibility(view.VISIBLE);
        activityButton.setVisibility(view.VISIBLE);
    }
    public void selectActivity(View view){
        TextView activityText=(TextView)findViewById(R.id.activityText);
        EditText activityEditText =(EditText)findViewById(R.id.hours_of_activity);
        Button activityButton = (Button)findViewById(R.id.activityButton);
        hoursOfActivity = activityEditText.getText().toString();
        activityText.setVisibility(view.GONE);
        activityEditText.setVisibility(view.GONE);
        activityButton.setVisibility(view.GONE);
        TextView sleepText = (TextView)findViewById(R.id.sleepText);
        EditText sleepEditText = (EditText) findViewById(R.id.hours_of_sleep);
        Button averageDayButton = (Button)findViewById(R.id.averageDayButton);
        sleepText.setVisibility(view.VISIBLE);
        sleepEditText.setVisibility(view.VISIBLE);
        averageDayButton.setVisibility(view.VISIBLE);
    }

    public void setAverageDay(View view){
        EditText sleepEditText = (EditText) findViewById(R.id.hours_of_sleep);
        hoursOfSleep = sleepEditText.getText().toString();
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
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void GoHome(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
