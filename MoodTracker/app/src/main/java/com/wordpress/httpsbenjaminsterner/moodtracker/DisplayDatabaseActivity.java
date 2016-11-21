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

        c.moveToFirst();
        if (c!= null) {
            do {
                String Mood = c.getString(ColumnMood);
                String HasReason = c.getString(ColumnHasReason);
                String Reason = c.getString(ColumnReason);
                String Time = c.getString(ColumnTime);
                message = message +"\n" + Mood + "/" + HasReason + "/" + Reason + "/" + Time;

            } while (c.moveToNext());
        }
        TextView textView = new TextView(this);
        textView.setTextSize(10);
        textView.setText(message);

        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_display_database);
        layout.addView(textView);
    }

    public void addMood (View view){
        Intent intent = new Intent(this, addMood.class);
        startActivity(intent);
    }
}
