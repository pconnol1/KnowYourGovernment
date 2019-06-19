package com.patrick.knowyourgovernment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class AboutActivity extends AppCompatActivity {

    private static final String TAG = "AboutActivity";
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: Started Task");
        setContentView(R.layout.activity_about);
        Log.d(TAG, "onCreate: Set Layout");
    }
}
