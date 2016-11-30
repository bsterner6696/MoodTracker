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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class addMood extends AppCompatActivity  implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks{

    private boolean selectedHasReason = true;
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
    }
    private static final String MOODS_CREATE_ENTRIES = "CREATE TABLE IF NOT EXISTS " + addMood.Moods.TABLE_NAME + " (" + addMood.Moods._ID + " INTEGER PRIMARY KEY," + addMood.Moods.COLUMN_NAME_MOOD + " VARCHAR ," + addMood.Moods.COLUMN_NAME_HASREASON + " BOOLEAN,"
            + addMood.Moods.COLUMN_NAME_REASON + " VARCHAR," + addMood.Moods.COLUMN_NAME_TIME + " TIMESTAMP," + Moods.COLUMN_NAME_WEATHER + " VARCHAR )";

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
    public void sendMessage(View view){

        GoogleApiClient locationApi = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        locationApi.connect();
        String weather = "";
        try {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location location = LocationServices.FusedLocationApi.getLastLocation(locationApi);
            String longitude = String.valueOf(location.getLongitude());
            String latitude = String.valueOf(location.getLatitude());
            String apiKey = "1ff12d7057e1f2c8dfe2971f2672c1ea";
            String weatherApiUrl = "http://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon="+longitude+"&appid="+apiKey;
            URL encodedUrl = new URL(weatherApiUrl);
            BufferedReader in = new BufferedReader((new InputStreamReader(encodedUrl.openStream())));
            String weatherResults;
            while ((weatherResults = in.readLine()) != null){
                System.out.println(weatherResults);
            }
            in.close();
            JSONObject jObj = new JSONObject(weatherResults);
            JSONArray jArr = jObj.getJSONArray("weather");
            JSONObject weatherObject = jArr.getJSONObject(0);
            weather = getString("main", weatherObject);
        } catch (Exception e){

        }



        Intent intent = new Intent(this, DisplayDatabaseActivity.class);
        EditText reasonEditText = (EditText) findViewById(R.id.reason);
        String Reason = reasonEditText.getText().toString();
        Spinner moodSpinner = (Spinner) findViewById(R.id.mood_spinner);
        String Mood = String.valueOf(moodSpinner.getSelectedItem());

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
            String MOODS_ADD_ENTRY = "INSERT INTO " + Moods.TABLE_NAME + " (" + Moods.COLUMN_NAME_MOOD + ", " + Moods.COLUMN_NAME_HASREASON + ", " + Moods.COLUMN_NAME_REASON + ", " + Moods.COLUMN_NAME_TIME + ", " + Moods.COLUMN_NAME_WEATHER +") VALUES ('"
                    + mood + "', '" + hasReason + "', '" + reason + "', " + "CURRENT_TIMESTAMP, '"+ weather +"' );";
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

}
