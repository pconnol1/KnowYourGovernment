package com.patrick.knowyourgovernment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.security.Provider;

public class Locator {

    private int MY_PERM_REQUEST_CODE = 12345;
    private String TAG = "Locator";
    private MainActivity mainActivity;
    private LocationManager locationManager;
    private Location location;

    public Locator(MainActivity activity){
        mainActivity=activity;
        if(checkPermission()){
            setUpLocationManager();
            determineLocation();
        }
    }
    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(mainActivity,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // I do not yet have permission
            ActivityCompat.requestPermissions(mainActivity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERM_REQUEST_CODE);

            Log.d(TAG, "checkPermission: ACCESS_FINE_LOCATION Permission requested, awaiting response.");
            return false; // Do not yet have permission - but I just asked for it
        } else {
            Log.d(TAG, "checkPermission: Already have ACCESS_FINE_LOCATION Permission for this app.");
            return true;  // I already have this permission
        }
    }
    public void setUpLocationManager(){
        if (checkPermission())
            locationManager = (LocationManager) mainActivity.getSystemService(mainActivity.LOCATION_SERVICE);
    }
    public void determineLocation(){
        if (checkPermission()){
            if (locationManager==null)
                setUpLocationManager();
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location==null){
                location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                if (location ==null){
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location == null) {
                        mainActivity.noLocationAvailable();
                        return;
                    }
                    else
                        Toast.makeText(mainActivity, "Using GPS Provider for Location", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(mainActivity, "Using Passive Provider for Location", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(mainActivity, "Using Network Provider for Location", Toast.LENGTH_SHORT).show();
            mainActivity.doLocationWork(location.getLatitude(), location.getLongitude());
        }
    }
}
