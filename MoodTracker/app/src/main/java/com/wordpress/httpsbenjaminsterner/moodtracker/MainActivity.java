package com.wordpress.httpsbenjaminsterner.moodtracker;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void addMood (View view){
        Intent intent = new Intent(this, addMood.class);
        startActivity(intent);
    }

    public void addEvent (View view){
        Intent intent = new Intent(this, addEvent.class);
        startActivity(intent);
    }

    public void checkInEndOfDay (View view){
        Intent intent = new Intent(this, EndOfDayCheckIn.class);
        startActivity(intent);
    }

    public void clearDB(View view){
        SQLiteDatabase checkInsDB = null;
        checkInsDB = this.openOrCreateDatabase("checkIns", MODE_PRIVATE, null);
        checkInsDB.execSQL("DROP TABLE IF EXISTS checkIns");
        checkInsDB = this.openOrCreateDatabase("checkIns", MODE_PRIVATE, null);
        checkInsDB.execSQL(EndOfDayCheckIn.CreateDatabase());
        checkInsDB.close();
        EndOfDayCheckIn.CreateDatabase();
    }

}
