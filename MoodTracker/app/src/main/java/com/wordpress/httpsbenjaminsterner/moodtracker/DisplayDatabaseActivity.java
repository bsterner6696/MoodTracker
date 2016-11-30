package com.wordpress.httpsbenjaminsterner.moodtracker;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DisplayDatabaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_database);

        Intent intent = getIntent();
        String message = intent.getStringExtra("EXTRA_MESSAGE");

        SQLiteDatabase moodsDB = null;
        moodsDB = this.openOrCreateDatabase("moods", MODE_PRIVATE, null);
        Cursor c = moodsDB.rawQuery("SELECT * FROM moods", null);
        int ColumnMood = c.getColumnIndex("mood");
        int ColumnHasReason = c.getColumnIndex("hasReason");
        int ColumnReason = c.getColumnIndex("reason");
        int ColumnTime = c.getColumnIndex("time");
        int ColumnWeather = c.getColumnIndex("weather");

        c.moveToFirst();
        if (c.moveToFirst()) {
            do {
                String Mood = c.getString(ColumnMood);
                String HasReason = c.getString(ColumnHasReason);
                String Reason = c.getString(ColumnReason);
                String Time = c.getString(ColumnTime);
                String Weather = c.getString(ColumnWeather);
                message = message +"\n" + Mood + "/" + HasReason + "/" + Reason + "/" + Time + "/" + Weather;

            } while (c.moveToNext());
        }
        TextView textView = new TextView(this);
        textView.setTextSize(10);
        textView.setText(message);

        String checkInsList = intent.getStringExtra("EXTRA_MESSAGE");
        SQLiteDatabase checkInsDB = null;
        checkInsDB = this.openOrCreateDatabase("checkIns", MODE_PRIVATE, null);
        Cursor checkInsCursor = checkInsDB.rawQuery("SELECT * FROM checkIns", null);
        int ColumnDiet = checkInsCursor.getColumnIndex("diet");
        int ColumnActivity = checkInsCursor.getColumnIndex("activity");
        int ColumnCheckInDate = checkInsCursor.getColumnIndex("checkInDate" +
                "");

        checkInsCursor.moveToFirst();
        if (checkInsCursor.moveToFirst()) {
            do {
                String Diet = checkInsCursor.getString(ColumnDiet);
                Double Activity = checkInsCursor.getDouble(ColumnActivity);
                String CheckInDate = checkInsCursor.getString(ColumnCheckInDate);
                checkInsList = checkInsList +"\n" + Diet + "/" + Activity + "/" + CheckInDate;

            } while (checkInsCursor.moveToNext());
        }
        TextView checkInsTextView = new TextView(this);
        checkInsTextView.setTextSize(10);
        checkInsTextView.setText(checkInsList);

        String eventsList = intent.getStringExtra("EXTRA_MESSAGE");
        SQLiteDatabase eventsDB = null;
        eventsDB = this.openOrCreateDatabase("events", MODE_PRIVATE, null);
        Cursor eventsCursor = eventsDB.rawQuery("SELECT * FROM events", null);
        int ColumnEvent = eventsCursor.getColumnIndex("event");
        int ColumnEffect = eventsCursor.getColumnIndex("effect");
        int ColumnEventTime = eventsCursor.getColumnIndex("time");

        eventsCursor.moveToFirst();
        if (eventsCursor.moveToFirst()) {
            do {
                String Event = eventsCursor.getString(ColumnEvent);
                String Effect = eventsCursor.getString(ColumnEffect);
                String EventTime = eventsCursor.getString(ColumnEventTime);
                eventsList = eventsList +"\n" + Event + "/" + Effect + "/" + EventTime;

            } while (eventsCursor.moveToNext());
        }
        TextView eventsTextView = new TextView(this);
        eventsTextView.setTextSize(10);
        eventsTextView.setText(eventsList);

        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_display_database);
        layout.addView(textView);
        layout.addView(eventsTextView);
        layout.addView(checkInsTextView);
    }

    public void addMood (View view){
        Intent intent = new Intent(this, addMood.class);
        startActivity(intent);
    }

    public void addEvent(View view){
        Intent intent = new Intent(this, addEvent.class);
        startActivity(intent);
    }


}
