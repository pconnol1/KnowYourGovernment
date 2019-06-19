package com.patrick.knowyourgovernment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private List<Official> officialList = new ArrayList<>();
    private final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private OfficialsAdapter oAdapter;
    private int MY_PERM_REQUEST_CODE = 12345;
    private String addressText;
    private String zip;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        oAdapter = new OfficialsAdapter(officialList, this);
        recyclerView.setAdapter(oAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


       if (netCheck()){
           Locator locator = new Locator(this);
           locator.setUpLocationManager();
           locator.determineLocation();
           new CivicInfoDownloader(this).execute();
       }
       else{
            showNoConnection();
       }
    }

    @Override
    public void onClick(View v) {
        //Toast.makeText(this, "You clicked a thing", Toast.LENGTH_SHORT).show();
        //send the intent to new activity
        int pos = recyclerView.getChildLayoutPosition(v);
        Official o = officialList.get(pos);
        Intent intent = new Intent(MainActivity.this, OfficialActivity.class);
        intent.putExtra("Official", o);
        intent.putExtra("Location", addressText);
        startActivity(intent);
    }

    @Override
    public boolean onLongClick(View v) {
        //Toast.makeText(this, "You long clicked.", Toast.LENGTH_SHORT).show();
        int pos = recyclerView.getChildLayoutPosition(v);
        onClick(v);
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //todo Check internet
        switch (item.getItemId()) {
            case R.id.aboutMenuItem:
                //open About activity
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);

                return true;
            case R.id.searchMenuItem:
                //todo manual search for location
                if(!netCheck()){
                    showNoConnection();
                    return false;
                }
                LayoutInflater inflater = LayoutInflater.from(this);
                final View view = inflater.inflate(R.layout.dialog_location, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Enter a City, State, or a Zip Code:");
                builder.setView(view);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText et = view.findViewById(R.id.locationEditText);
                        String location = et.getText().toString();
                        //todo search location
                        doAddress(location);

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Dialog canceled
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }




    public void noLocationAvailable(){
        officialList.clear();
        oAdapter.notifyDataSetChanged();
        ((TextView) findViewById(R.id.locationTextView)).setText("No Data For Location");
    }
    public void doLocationWork(double latitude, double longitude){
        Geocoder geocoder = new Geocoder(this);
        try{
            List<Address> addresses= geocoder.getFromLocation(latitude,longitude,1);
            Address address =addresses.get(0);
            addressText = String.format("%s %s %s",
                    (address.getLocality() == null ? "" : address.getLocality()+",") ,
                    (address.getAdminArea() == null ? "" : address.getAdminArea()),
                    (address.getPostalCode() == null ? "" : address.getPostalCode()));
            zip=address.getPostalCode();
            ((TextView) findViewById(R.id.locationTextView)).setText(addressText);

            //TODO Async Task with address.getPostalCode() if not null.
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void civicInfo(ArrayList<Object> list){
        if(list.size()!=0){
            addressText= (String) list.get(0);
            ((TextView) findViewById(R.id.locationTextView)).setText(addressText);
            officialList.clear();
            officialList.addAll((ArrayList<Official>)list.get(1));
            oAdapter.notifyDataSetChanged();
        }
        else{
            //the list is empty
            noLocationAvailable();
        }
    }
    private boolean netCheck(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            showNoConnection();
            return false;
        }

        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            showNoConnection();
            return false;
        }
    }
    public String getZip(){
        return zip;
    }
    private void showNoConnection(){
        officialList.clear();
        oAdapter.notifyDataSetChanged();
        LayoutInflater inflater = LayoutInflater.from(this);
        final View view = inflater.inflate(R.layout.dialog_location, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No Internet Connection:");
        builder.setView(view);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {@Override public void onClick(DialogInterface dialog, int which) { }});
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {@Override public void onClick(DialogInterface dialog, int which) { }});
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void doAddress(String s){
        zip=s;
        addressText=s;
        new CivicInfoDownloader(this).execute();
        ((TextView) findViewById(R.id.locationTextView)).setText(addressText);
    }


}
