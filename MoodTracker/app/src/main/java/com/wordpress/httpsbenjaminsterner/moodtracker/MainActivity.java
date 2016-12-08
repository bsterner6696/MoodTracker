package com.wordpress.httpsbenjaminsterner.moodtracker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.SensorEvent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEventListener;
import android.widget.TextView;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.wordpress.httpsbenjaminsterner.moodtracker.R.styleable.AppCompatTextView;
import static com.wordpress.httpsbenjaminsterner.moodtracker.R.styleable.Spinner;
import static com.wordpress.httpsbenjaminsterner.moodtracker.R.styleable.View;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, SensorEventListener{

    //private TextView textView;
    public static GoogleApiClient locationApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //textView = (TextView) findViewById(R.id.stepCount);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
        }
        locationApi = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        locationApi.connect();

        MoodBar.notify(this,"Add Mood");
    }

    public void addMood (View view){
        Intent intent = new Intent(this, addMood.class);
        startActivity(intent);
    }
    public void CheckOverTime(View view){
        Intent intent = new Intent(this, CheckMoodsOverTimePeriod.class);
        startActivity(intent);
    }

    public void addEvent (View view){
        Intent intent = new Intent(this, addEvent.class);
        startActivity(intent);
    }
    public void setAverageDay (View view){
        Intent intent = new Intent(this, AverageDay.class);
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
    public void clearCheckInsDB(View view){
        SQLiteDatabase checkInsDB = null;
        checkInsDB = this.openOrCreateDatabase("checkIns", MODE_PRIVATE, null);
        checkInsDB.execSQL("DROP TABLE IF EXISTS checkIns");
        checkInsDB = this.openOrCreateDatabase("checkIns", MODE_PRIVATE, null);
        checkInsDB.execSQL(addMood.CreateDatabase());
        checkInsDB.close();
    }
//    public void StartWalkSensor(View view){
//        SensorManager sManager;
//        Sensor walkSensor;
//        sManager = (SensorManager) getSystemService(SENSOR_SERVICE);
//        walkSensor = sManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
//        sManager.registerListener(this, walkSensor, SensorManager.SENSOR_DELAY_FASTEST);
//
//    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationRequest request = createLocationRequest();
        LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }
        };
        LocationServices.FusedLocationApi.requestLocationUpdates(locationApi, request, listener);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
    private LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(120000);
        mLocationRequest.setFastestInterval(30000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        return mLocationRequest;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }
                return;
            }

        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor = sensorEvent.sensor;
        float[] values = sensorEvent.values;
        int value = -1;
        if (values.length >0){
            value = (int) values [0];
        }

        if (sensor.getType() == Sensor.TYPE_STEP_COUNTER){
            //textView.setText(String.valueOf(value));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
