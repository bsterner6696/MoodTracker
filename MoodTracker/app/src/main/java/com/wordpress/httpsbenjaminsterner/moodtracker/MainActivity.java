package com.wordpress.httpsbenjaminsterner.moodtracker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;

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

import static com.wordpress.httpsbenjaminsterner.moodtracker.R.styleable.View;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks{

    public static GoogleApiClient locationApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
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
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
