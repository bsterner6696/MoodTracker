package com.wordpress.httpsbenjaminsterner.moodtracker;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.LinearLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class addMood extends AppCompatActivity {

    private String weather = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mood);
        Intent thisIntent = getIntent();
        String Reason = thisIntent.getStringExtra("EXTRA_REASON");
        String Mood = thisIntent.getStringExtra("EXTRA_MOOD");
        int Severity = thisIntent.getIntExtra("EXTRA_SEVERITY", 1);
        Boolean selectedHasReason = thisIntent.getBooleanExtra("EXTRA_HAS_REASON", true);
        TextView wrapUp = (TextView) findViewById(R.id.wrapUp);
        wrapUp.setText("You have selected '"+ Mood + "' with a severity of "+Severity+ ".  Press 'Add Mood' to add this mood.");

    }


    public static class Moods implements BaseColumns {
        public static final String TABLE_NAME = "moods";
        public static final String COLUMN_NAME_MOOD = "mood";
        public static final String COLUMN_NAME_HASREASON = "hasReason";
        public static final String COLUMN_NAME_REASON = "reason";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_WEATHER = "weather";
        public static final String COLUMN_NAME_SEVERITY = "severity";
    }
    private static final String MOODS_CREATE_ENTRIES = "CREATE TABLE IF NOT EXISTS " + addMood.Moods.TABLE_NAME + " (" + addMood.Moods._ID + " INTEGER PRIMARY KEY," + addMood.Moods.COLUMN_NAME_MOOD + " VARCHAR ," + addMood.Moods.COLUMN_NAME_HASREASON + " BOOLEAN,"
            + addMood.Moods.COLUMN_NAME_REASON + " VARCHAR," + addMood.Moods.COLUMN_NAME_TIME + " TIMESTAMP," + Moods.COLUMN_NAME_WEATHER + " VARCHAR, "+ Moods.COLUMN_NAME_SEVERITY + " INTEGER )";

    public static String CreateDatabase(){ return MOODS_CREATE_ENTRIES; }

    public void AddMood(View view) throws InterruptedException {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {

                    try {

                        String longitude = "-120";
                        String latitude = "30";

                        if (MainActivity.locationApi.isConnected()){

                            Location location = LocationServices.FusedLocationApi.getLastLocation(MainActivity.locationApi);
                            longitude = String.valueOf(location.getLongitude());
                            latitude = String.valueOf(location.getLatitude());
                        }

                        String apiKey = "1ff12d7057e1f2c8dfe2971f2672c1ea";
                        String weatherApiUrl = "http://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon="+longitude+"&appid="+apiKey;
                        String weatherResults = "";
                        URL encodedUrl = new URL(weatherApiUrl);
                        HttpURLConnection con = null;
                        InputStream inStream = null;
                        try {
                            con = (HttpURLConnection) (encodedUrl.openConnection());
                            con.setRequestMethod("GET");
                            con.setDoInput(true);
                            con.setDoOutput(true);
                            con.connect();

                            StringBuffer buffer = new StringBuffer();
                            inStream = con.getInputStream();
                            BufferedReader bufferReader = new BufferedReader((new InputStreamReader(inStream)));
                            String line;
                            while ((line = bufferReader.readLine()) != null)
                                buffer.append(line + "rn");
                            inStream.close();
                            con.disconnect();
                            weatherResults = buffer.toString();
                        } catch (Throwable T) {
                            System.out.print(T);
                        } finally {
                            try{inStream.close();}catch(Throwable T) {}
                            try{con.disconnect();}catch(Throwable T) {}
                        }

                        JSONObject jObj = new JSONObject(weatherResults);
                        JSONArray jArr = jObj.getJSONArray("weather");
                        JSONObject weatherObject = jArr.getJSONObject(0);
                        weather = getString("main", weatherObject);
                    } catch (Exception e){

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        thread.join();




        Intent intent = new Intent(this, DisplayDatabaseActivity.class);
        Intent thisIntent = getIntent();
        String Reason = thisIntent.getStringExtra("EXTRA_REASON");
        String Mood = thisIntent.getStringExtra("EXTRA_MOOD");
        int Severity = thisIntent.getIntExtra("EXTRA_SEVERITY", 1);
        Boolean selectedHasReason = thisIntent.getBooleanExtra("EXTRA_HAS_REASON", true);
        SQLiteDatabase moodsDB = null;
        try {
            moodsDB = this.openOrCreateDatabase("moods", MODE_PRIVATE, null);
            moodsDB.execSQL(MOODS_CREATE_ENTRIES);
            String mood = Mood;
            boolean hasReason = selectedHasReason;
            String reason = Reason;
            String regx = "'";
            char[] characterArray = regx.toCharArray();
            for (char character : characterArray){
                reason = reason.replace(""+character, "");
            }
            String MOODS_ADD_ENTRY = "INSERT INTO " + Moods.TABLE_NAME + " (" + Moods.COLUMN_NAME_MOOD + ", " + Moods.COLUMN_NAME_HASREASON + ", " + Moods.COLUMN_NAME_REASON + ", " + Moods.COLUMN_NAME_TIME + ", " + Moods.COLUMN_NAME_WEATHER +", "+ Moods.COLUMN_NAME_SEVERITY+ ") VALUES ('"
                    + mood + "', '" + hasReason + "', '" + reason + "', " + "CURRENT_TIMESTAMP, '"+ weather +"', " + Severity +" );";
            moodsDB.execSQL(MOODS_ADD_ENTRY);
        } catch (Exception e) {
            Log.e("Error", "Error", e);
        } finally {
            if (moodsDB != null)
                moodsDB.close();
        }

        intent.putExtra("EXTRA_MESSAGE", "Database Updated");
        startActivity(intent);
    }

    private static String getString(String tagName, JSONObject jObj) throws Exception {
        return jObj.getString(tagName);
    }
    public void GoHome(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }



}
