package com.patrick.knowyourgovernment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class OfficialActivity extends AppCompatActivity {
    Official official;
    String location;
    private final String TAG = "OfficialActivity";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);

        //get references to views
        TextView locationTV = findViewById(R.id.locationTextView);
        TextView officeTV = findViewById(R.id.officeTextView);
        TextView nameTV = findViewById(R.id.nameTextView);
        TextView partyTV = findViewById(R.id.partyTextView);
        final ImageView imageView = findViewById(R.id.imageView2);
        TextView addressTV = findViewById(R.id.addressTextView);
        TextView phoneTV = findViewById(R.id.phoneTextView);
        TextView emailTV = findViewById(R.id.emailTextView);
        TextView websiteTV = findViewById(R.id.websiteTextView);
        ImageView youtubeIV = findViewById(R.id.imageView4);
        ImageView gpIV = findViewById(R.id.imageView5);
        ImageView twitterIV = findViewById(R.id.imageView6);
        ImageView facebookIV = findViewById(R.id.imageView7);

        //get Location and Official object Intents
        Intent intent = getIntent();
        if(intent.hasExtra("Location")){
            location =  intent.getStringExtra("Location");
            //set location heading
            locationTV.setText(location);
        }
        if(intent.hasExtra("Official")){
            official = (Official) intent.getSerializableExtra("Official");

            //set info Views
            officeTV.setText(official.getTitle());
            nameTV.setText(official.getName());
            partyTV.setText("("+official.getParty()+")");
            addressTV.setText(official.getAddress());
            phoneTV.setText(official.getPhone());
            emailTV.setText(official.getEmail());
            websiteTV.setText(official.getUrl());

            //set social media views
            if(official.getYoutube().equals("No Data Provided")){
                youtubeIV.setVisibility(View.INVISIBLE);
            }
            if(official.getGooglePlus().equals("No Data Provided")){
                gpIV.setVisibility(View.INVISIBLE);
            }
            if(official.getTwitter().equals("No Data Provided")){
                twitterIV.setVisibility(View.INVISIBLE);
            }
            if(official.getFacebook().equals("No Data Provided")){
                facebookIV.setVisibility(View.INVISIBLE);
            }

            //picasso photo
            Log.d(TAG, "onCreate: loadimage" + official.getPhotoURL());
            if(!official.getPhotoURL().equals("")){
                Picasso picasso = new Picasso.Builder(this)
                        .listener(new Picasso.Listener() {
                            @Override
                            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                                Log.d(TAG, "onImageLoadFailed: ");
                                picasso.load(R.drawable.brokenimage).into(imageView);
                            }
                        })
                        .build();
                picasso.load(official.getPhotoURL())
                        .error(R.drawable.missingimage)
                        .placeholder(R.drawable.placeholder)
                        .into(imageView);
            }
            //set Background color
            ConstraintLayout currentView= findViewById(R.id.activity_official);

            if (official.getParty().equals("Democrat"))
                currentView.setBackgroundColor(getResources().getColor(R.color.democrat));
            else if (official.getParty().equals("Republican"))
                currentView.setBackgroundColor(getResources().getColor(R.color.republican));
            else
                currentView.setBackgroundColor(getResources().getColor(R.color.no_party));


            //Linkify address, phone, email, website TextViews
            if(!official.getAddress().equals("No Data Provided")){
                Linkify.addLinks(addressTV, Linkify.MAP_ADDRESSES);
            }
            if(!official.getPhone().equals("No Data Provided")){
                Linkify.addLinks(phoneTV,Linkify.PHONE_NUMBERS);
            }
            if(!official.getEmail().equals("No Data Provided")){
                Linkify.addLinks(emailTV,Linkify.EMAIL_ADDRESSES);
            }
            if(!official.getUrl().equals("No Data Provided")){
                Linkify.addLinks(websiteTV,Linkify.WEB_URLS);
            }
        }
        else{
            youtubeIV.setVisibility(View.INVISIBLE);
            gpIV.setVisibility(View.INVISIBLE);
            twitterIV.setVisibility(View.INVISIBLE);
            facebookIV.setVisibility(View.INVISIBLE);
        }




    }
    public void doYouTube(View v){
        //Toast.makeText(this, "youtube clicked", Toast.LENGTH_SHORT).show();
        String name = official.getYoutube();
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/" + name));
            startActivity(intent);
        } catch (Exception e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/" + name)));
        }

    }
    public void doGooglePlus(View v) {
        //Toast.makeText(this, "G+ clicked", Toast.LENGTH_SHORT).show();
        String name = official.getGooglePlus();
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setClassName("com.google.android.apps.plus",
                    "com.google.android.apps.plus.phone.UrlGatewayActivity");
            intent.putExtra("customAppUri", name);
            startActivity(intent);
        } catch (Exception e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://plus.google.com/" + name)));
        }
    }
    public void doTwitter(View v){
        //Toast.makeText(this, "Twitter Clicked", Toast.LENGTH_SHORT).show();
        Intent intent = null;
        String name = official.getTwitter();
        try {
            // get the Twitter app if possible
            getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + name));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            // no Twitter app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + name));
        }
        startActivity(intent);

    }

    public void doFacebook(View v){
        //Toast.makeText(this, "Facebook clicked", Toast.LENGTH_SHORT).show();
            String FACEBOOK_URL = "https://www.facebook.com/" + official.getFacebook();
            String urlToUse;
            PackageManager packageManager = getPackageManager();
            try {
                int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
                if (versionCode >= 3002850) { //newer versions of fb app
                    urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
                } else { //older versions of fb app
                    urlToUse = "fb://page/" + FACEBOOK_URL;
                }
            } catch (PackageManager.NameNotFoundException e) {
                urlToUse = FACEBOOK_URL; //normal web url
            }
            Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
            facebookIntent.setData(Uri.parse(urlToUse));
            startActivity(facebookIntent);

        }
    public void openPhotoActivity(View v){
        //Toast.makeText(this, official.getPhotoURL(), Toast.LENGTH_SHORT).show();
        if(!official.getPhotoURL().equals("No Data Provided")){
            Intent intent = new Intent(OfficialActivity.this, PhotoActivity.class);
            intent.putExtra("Official", official);
            intent.putExtra("Location", location);
            startActivity(intent);
        }
    }
}
