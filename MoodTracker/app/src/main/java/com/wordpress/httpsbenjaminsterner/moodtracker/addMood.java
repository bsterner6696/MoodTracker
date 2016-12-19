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

    private boolean selectedHasReason = true;
    private String weather = "";
    private String selectedMood = "";
    private int selectedSeverity = 0;
    private String selectedReason = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mood);
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
    public void onRadioButtonClicked(View view){
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()){
            case R.id.radio_has_reason:
                if(checked){
                    selectedHasReason = true;
                }
                break;
            case R.id.radio_no_reason:
                if(checked){
                    selectedHasReason = false;
                }
                break;
        }
    }
    public void SelectMood(View view){
        TextView moodText = (TextView) findViewById(R.id.moodText);
        Spinner moodSpinner = (Spinner) findViewById(R.id.mood_spinner);
        Button moodButton = (Button) findViewById(R.id.moodButton);
        TextView severityText = (TextView) findViewById(R.id.severityText);
        Spinner severitySpinner = (Spinner) findViewById(R.id.severity_spinner);
        Button severityButton = (Button) findViewById(R.id.severityButton);
        selectedMood = String.valueOf(moodSpinner.getSelectedItem());
        moodText.setVisibility(view.GONE);
        moodSpinner.setVisibility(view.GONE);
        moodButton.setVisibility(view.GONE);
        severityText.setVisibility(view.VISIBLE);
        severitySpinner.setVisibility(view.VISIBLE);
        severityButton.setVisibility(view.VISIBLE);
    }

    public void SelectSeverity(View view){
        TextView severityText = (TextView) findViewById(R.id.severityText);
        Spinner severitySpinner = (Spinner) findViewById(R.id.severity_spinner);
        Button severityButton = (Button) findViewById(R.id.severityButton);
        selectedSeverity = Integer.parseInt(String.valueOf(severitySpinner.getSelectedItem()));
        severityText.setVisibility(view.GONE);
        severitySpinner.setVisibility(view.GONE);
        severityButton.setVisibility(view.GONE);
        TextView hasReasonText = (TextView) findViewById(R.id.reasonText);
        RadioGroup reasonRadio = (RadioGroup) findViewById(R.id.reasonRadio);
        Button hasReasonButton = (Button) findViewById(R.id.hasReasonButton);
        hasReasonText.setVisibility(view.VISIBLE);
        reasonRadio.setVisibility(view.VISIBLE);
        hasReasonButton.setVisibility(view.VISIBLE);

    }
    public void SelectHasReason(View view){
        TextView hasReasonText = (TextView) findViewById(R.id.reasonText);
        RadioGroup reasonRadio = (RadioGroup) findViewById(R.id.reasonRadio);
        Button hasReasonButton = (Button) findViewById(R.id.hasReasonButton);
        hasReasonText.setVisibility(view.GONE);
        reasonRadio.setVisibility(view.GONE);
        hasReasonButton.setVisibility(view.GONE);
        Button reasonButton = (Button) findViewById(R.id.reasonButton);
        Button restartButton = (Button) findViewById(R.id.restartMoodButton);
        EditText reasonEditText = (EditText) findViewById(R.id.reason);
        TextView wrapUpText = (TextView)findViewById(R.id.wrapUp);
        String wrapUp = "You have selected:\nMood: " + selectedMood+"\nSeverity: "+ selectedSeverity+"\nHas Reason?: "+selectedHasReason+"\nWould you like to add the mood, or start the mood entry over?";
        wrapUpText.setText(wrapUp);
        wrapUpText.setTextSize(20);
        if (selectedHasReason){
            reasonEditText.setVisibility(view.VISIBLE);
        }
        wrapUpText.setVisibility(view.VISIBLE);
        reasonButton.setVisibility(view.VISIBLE);
        restartButton.setVisibility(view.VISIBLE);

    }
    public void RestartSelection(View view){
        Intent intent = new Intent(this, addMood.class);
        startActivity(intent);
    }
    public void SelectReason(View view) throws InterruptedException {
        weather = "";
        EditText reasonEditText = (EditText) findViewById(R.id.reason);
        selectedReason = reasonEditText.getText().toString();
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
        String Reason = selectedReason;

        String Mood = selectedMood;

        int Severity = selectedSeverity;
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
