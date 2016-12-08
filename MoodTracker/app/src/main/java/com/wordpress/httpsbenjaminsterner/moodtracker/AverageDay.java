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

public class AverageDay extends AppCompatActivity {
    private TextView dietText;
    private Button homeButton;
    private Spinner dietSpinner;
    private Button dietButton;
    private TextView activityText;
    private EditText activityEditText;
    private Button activityButton;
    private TextView sleepText;
    private EditText sleepEditText;
    private Button averageDayButton;
    private String diet;
    private String hoursOfActivity;
    private String hoursOfSleep;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_average_day);
        dietText = (TextView)findViewById(R.id.dietText);
        dietText.setMovementMethod(LinkMovementMethod.getInstance());
        homeButton = (Button)findViewById(R.id.homeButton);
        dietSpinner = (Spinner)findViewById(R.id.diet_spinner);
        dietButton = (Button) findViewById(R.id.dietButton);
        activityText=(TextView)findViewById(R.id.activityText);
        activityEditText=(EditText)findViewById(R.id.hours_of_activity);
        activityButton = (Button)findViewById(R.id.activityButton);
        sleepText = (TextView)findViewById(R.id.sleepText);
        sleepEditText = (EditText) findViewById(R.id.hours_of_sleep);
        averageDayButton = (Button)findViewById(R.id.averageDayButton);
        ViewGroup layout = (ViewGroup)findViewById(R.id.activity_average_day);
        layout.removeAllViews();
        layout.addView(homeButton);
        layout.addView(dietText);
        layout.addView(dietSpinner);
        layout.addView(dietButton);
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
        diet = getResources().getStringArray(R.array.diet_values_array)[dietSpinner.getSelectedItemPosition()];
        ViewGroup layout = (ViewGroup)findViewById(R.id.activity_average_day);
        layout.removeAllViews();
        layout.addView(homeButton);
        layout.addView(activityText);
        layout.addView(activityEditText);
        layout.addView(activityButton);

    }
    public void selectActivity(View view){
        hoursOfActivity = activityEditText.getText().toString();
        ViewGroup layout = (ViewGroup)findViewById(R.id.activity_average_day);
        layout.removeAllViews();
        layout.addView(homeButton);
        layout.addView(sleepText);
        layout.addView(sleepEditText);
        layout.addView(averageDayButton);
    }

    public void setAverageDay(View view){
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
