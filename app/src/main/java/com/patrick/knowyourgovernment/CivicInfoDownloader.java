package com.patrick.knowyourgovernment;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class CivicInfoDownloader extends AsyncTask<String,Void ,String> {
    private MainActivity mainActivity;

    private static final String TAG = "CivicInfoDownloader";

    public CivicInfoDownloader(MainActivity ma){mainActivity=ma;}
    @Override
    protected String doInBackground(String... strings) {
        String loc = mainActivity.getZip();
        Uri dataUri = Uri.parse("https://www.googleapis.com/civicinfo/v2/representatives?key="+"AIzaSyBt2_bZ8FiYUT4iVADB2gvbsjFr1ULwvvM"+"&address="+loc);
        Log.d(TAG, "doInBackground: "+dataUri.toString());
        String urlToUse= dataUri.toString();
        Log.d(TAG, "doInBackground: "+urlToUse);
        StringBuilder sb =new StringBuilder();
        try{
            URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            Log.d(TAG, "doInBackground: REsponseCode: " + conn.getResponseCode());

            conn.setRequestMethod("GET");

            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            String line;
            while((line = reader.readLine()) != null){
                sb.append(line).append("\n");
                Log.d(TAG, "doInBackground: line:"+line);
            }
            is.close();
        }
        catch (Exception e){
            Log.d(TAG, "doInBackground: exception: " + e.getMessage());
            return null;
        }

        Log.d(TAG, "doInBackground: sb = " + sb.toString());
        return sb.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        ArrayList<Object> list =parseJSON(s);

        mainActivity.civicInfo(list);

    }

    private ArrayList<Object> parseJSON(String s){
        ArrayList<Object> list= new ArrayList<>();
        ArrayList<Official> officialList = new ArrayList<>();
        Log.d(TAG, "parseJSON: String = "+ s);
        if(s==null) {
            Toast.makeText(mainActivity, "Civic Info Service is Unavailable", Toast.LENGTH_SHORT).show();
            return list;
        }
        if( s.equals("")) {
            Toast.makeText(mainActivity, "No Data Available for Specified Location", Toast.LENGTH_SHORT).show();
            return list;
        }
        try{
            //object one is normalizedInput
            JSONObject jObjMain = new JSONObject(s);
            JSONObject normalizedInput = jObjMain.getJSONObject("normalizedInput");
            String location = normalizedInput.getString("city")+"," +normalizedInput.getString("state")+ " " + normalizedInput.getString("zip");
            //obj3 is offices ( title )
            JSONArray offices =  jObjMain.getJSONArray("offices");
            //obj4 is officials (everything else)
            JSONArray officials = jObjMain.getJSONArray("officials");
            for (int i =0;i<offices.length(); i++){
                JSONObject office = offices.getJSONObject(i);
                String title = office.getString("name");
                JSONArray positions = office.getJSONArray("officialIndices");
                for(int j=0;i<positions.length();i++){
                    //TODO Check for nulls
                    int pos = positions.getInt(j);
                    JSONObject official= officials.getJSONObject(pos);
                    String name = official.getString("name");

                    //ADDRESS INFO
                    //myAddress is the concatenation
                    // may be skipped use "No Data Provided"
                    String myAddress = "No Data Provided";
                    if(official.getJSONArray("address") != null) {
                        JSONObject address = official.getJSONArray("address").getJSONObject(0);
                        String line1 = address.getString("line1");
                        String line2 = address.getString("line2");
                        String line3 = address.getString("city") + ", " + address.getString("state") + " " + address.getString("zip");
                        myAddress = line1 + "\n" + line2 + "\n" + line3;
                    }

                    String party = "No Data Provided";
                    String phone = "No Data Provided";
                    String url = "No Data Provided";
                    String email = "No Data Provided";
                    String photoURL = "No Data Provided";

                    String p = official.getString("party");
                    JSONArray phones = official.getJSONArray("phones");
                    if(phones!=null)
                        phone= phones.getString(0);

                    if(official.has("urls")) {
                        JSONArray urls = official.getJSONArray("urls");
                        String u = "No Data Provided";
                        if (urls != null)
                            url = urls.getString(0);
                    }

                    if(official.has("emails")) {
                        JSONArray em = official.getJSONArray("emails");
                        if (em != null)
                            email = em.getString(0);
                    }
                    String pu =official.getString("photoUrl");




                    if(p!=null && !p.trim().equals("null")) {
                        party= p;
                    }
                    if(pu!=null && !pu.trim().equals("null")) {
                        photoURL= pu;
                    }





                    String facebook="No Data Provided";
                    String twitter="No Data Provided";
                    String gPlus="No Data Provided";
                    String youtube="No Data Provided";

                    JSONArray channels = official.getJSONArray("channels");
                    Log.d(TAG, "parseJSON: channels"+channels.toString());
                    Log.d(TAG, "parseJSON: num channels"+ channels.length());
                    if (channels != null) {
                        for (int k = 0; k < channels.length(); k++) {
                            Log.d(TAG, "parseJSON: k= "+k);
                            JSONObject channel = channels.getJSONObject(k);
                            switch (channel.getString("type")) {
                                case "Facebook":
                                    facebook = checkNull(channel.getString("id"));
                                case "Twitter":
                                    twitter = checkNull(channel.getString("id"));
                                case "GooglePlus":
                                    gPlus = checkNull(channel.getString("id"));
                                case "YouTube":
                                    youtube = checkNull(channel.getString("id"));
                            }
                        }
                    }
                    //make officials and add to list
                    officialList.add(new Official(name,title,myAddress,party,phone,url,email,photoURL,gPlus,facebook,twitter,youtube));
                }

            }
            list.add(location);
            list.add(officialList);

        }
        catch (Exception e){
            Log.d(TAG, "parseJSON: "+e.getMessage());
            e.printStackTrace();
        }



        return list;
    }

    private String checkNull(String p){
        if(p!=null && !p.trim().equals("null")) {
            return p;
        }
        return "No Data Provided";
    }
}
