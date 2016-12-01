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
        String message = "MOODS DB \n\n";
        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_display_database);

        SQLiteDatabase moodsDB = null;
        TextView textView = null;
        try {
            moodsDB = this.openOrCreateDatabase("moods", MODE_PRIVATE, null);
            Cursor c = moodsDB.rawQuery("SELECT * FROM moods", null);
            int ColumnMood = c.getColumnIndex("mood");
            int ColumnHasReason = c.getColumnIndex("hasReason");
            int ColumnReason = c.getColumnIndex("reason");
            int ColumnTime = c.getColumnIndex("time");
            int ColumnWeather = c.getColumnIndex("weather");
            int ColumnSeverity = c.getColumnIndex("severity");

            c.moveToFirst();
            if (c.moveToFirst()) {
                do {
                    String Mood = c.getString(ColumnMood);
                    String HasReason = c.getString(ColumnHasReason);
                    String Reason = c.getString(ColumnReason);
                    String Time = c.getString(ColumnTime);
                    String Weather = c.getString(ColumnWeather);
                    String Severity = c.getString(ColumnSeverity);
                    message = message +"MOOD: " + Mood +" \n SEVERITY: " + Severity + " \nHAS REASON?: " + HasReason + "\nREASON: " + Reason + "\nTIMESTAMP: " + Time + "\nWEATHER: " + Weather + "\n\n";

                } while (c.moveToNext());
            }
            textView = new TextView(this);
            textView.setTextSize(12);
            textView.setText(message);
            layout.addView(textView);
        } catch (Throwable T) {
            System.out.print(T);
        } finally {
            if(moodsDB!=null) moodsDB.close();
        }

        String checkInsList = "\n CHECK INS DB \n\n";
        SQLiteDatabase checkInsDB = null;
        TextView checkInsTextView = null;
        try {
            checkInsDB = this.openOrCreateDatabase("checkIns", MODE_PRIVATE, null);
            Cursor checkInsCursor = checkInsDB.rawQuery("SELECT * FROM checkIns", null);
            int ColumnDiet = checkInsCursor.getColumnIndex("diet");
            int ColumnActivity = checkInsCursor.getColumnIndex("activity");
            int ColumnCheckInDate = checkInsCursor.getColumnIndex("checkInDate");

            checkInsCursor.moveToFirst();
            if (checkInsCursor.moveToFirst()) {
                do {
                    String Diet = checkInsCursor.getString(ColumnDiet);
                    Double Activity = checkInsCursor.getDouble(ColumnActivity);
                    String CheckInDate = checkInsCursor.getString(ColumnCheckInDate);
                    checkInsList = checkInsList +"\nDIET: " + Diet + "\nACTIVITY: " + Activity + "\nDATE; " + CheckInDate + "\n\n";

                } while (checkInsCursor.moveToNext());
            }
            checkInsTextView = new TextView(this);
            checkInsTextView.setTextSize(12);
            checkInsTextView.setText(checkInsList);
            layout.addView(checkInsTextView);
        } catch (Throwable T) {
            System.out.print(T);
        } finally {
            if (checkInsDB!=null) checkInsDB.close();
        }

        String eventsList = "\nEVENTS\n\n";
        SQLiteDatabase eventsDB = null;
        try {
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
                    eventsList = eventsList +"EVENT: " + Event + "\n EFFECT: " + Effect + "\nTIME" + EventTime + "\n\n";

                } while (eventsCursor.moveToNext());
            }
            TextView eventsTextView = new TextView(this);
            eventsTextView.setTextSize(12);
            eventsTextView.setText(eventsList);
            layout.addView(eventsTextView);
        } catch (Throwable T) {
            System.out.print(T);
        } finally {
            if (eventsDB!=null) eventsDB.close();
        }

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
