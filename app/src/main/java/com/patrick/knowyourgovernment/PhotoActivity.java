package com.patrick.knowyourgovernment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PhotoActivity extends AppCompatActivity {
    Official official;
    String location;
    final String TAG = "PhotoActivity";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: SuperOnCreate");
        
        setContentView(R.layout.activity_photo_detail);
        TextView locationTV = findViewById(R.id.locationTextView);
        TextView officeTV = findViewById(R.id.officeTextView);
        TextView nameTV = findViewById(R.id.nameTextView);
        final ImageView imageView = findViewById(R.id.imageView3);

        Log.d(TAG, "onCreate: Set Views");
        Intent intent = getIntent();
        Log.d(TAG, "onCreate: Got Intents");
        if(intent.hasExtra("Location")){
            location =  intent.getStringExtra("Location");
            //set location heading
            locationTV.setText(location);
            Log.d(TAG, "onCreate: Set Location");
        }
        if(intent.hasExtra("Official")){
            official = (Official) intent.getSerializableExtra("Official");
            Log.d(TAG, "onCreate: Got Official");
            //picasso photo
            if(!official.getPhotoURL().equals("")){
                Picasso picasso = new Picasso.Builder(this)
                        .listener(new Picasso.Listener() {
                            @Override
                            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
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
            ConstraintLayout currentView= findViewById(R.id.activity_photo);
            Log.d(TAG, "onCreate: Party = " + official.getParty());

            if (official.getParty().equals("Democrat"))
                currentView.setBackgroundColor(getResources().getColor(R.color.democrat));
            else if (official.getParty().equals("Republican"))
                currentView.setBackgroundColor(getResources().getColor(R.color.republican));
            else
                currentView.setBackgroundColor(getResources().getColor(R.color.no_party));


            officeTV.setText(official.getTitle());
            nameTV.setText(official.getName());
        }

    }
}
