package com.wordpress.httpsbenjaminsterner.moodtracker;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.wordpress.httpsbenjaminsterner.moodtracker.R.styleable.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MoodBar.notify(this,"Add Mood");
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
    }
    public void clearMoodDB(View view){
        SQLiteDatabase moodsDB = null;
        moodsDB = this.openOrCreateDatabase("moods", MODE_PRIVATE, null);
        moodsDB.execSQL("DROP TABLE IF EXISTS moods");
        moodsDB = this.openOrCreateDatabase("moods", MODE_PRIVATE, null);
        moodsDB.execSQL(addMood.CreateDatabase());
        moodsDB.close();
    }

}
