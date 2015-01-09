package com.droplet.droplet;

import android.os.AsyncTask;
import android.os.Looper;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.client.Firebase;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;
//ASLDKJFALKSDJFLAKJSDFLKAJLKSDJFLAKSJ*/

public class MainActivity extends ActionBarActivity {
    Button locationButton;
    LocationGetter locationGet;

    Firebase rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Firebase.setAndroidContext(this);
        rootRef = new Firebase("https://dropletserver.firebaseio.com/");

        super.onCreate(savedInstanceState);
        locationGet = new LocationGetter(MainActivity.this);
        setContentView(R.layout.activity_main);
        locationButton = (Button) findViewById(R.id.locationGetBtn);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

                StrictMode.setThreadPolicy(policy);
                double latitude = locationGet.getLatitude();
                double longitude = locationGet.getLongitude();
                Log.wtf("WHEEEEE", "LONG " + longitude + " LAT " + latitude);
                //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                Droplet drop = new Droplet(longitude, latitude, "Edward", "ayy lmao");

                Firebase dropRef = rootRef.child("Drops");
                Map<String, Droplet> dropMap = new HashMap<String, Droplet>();
                dropMap.put(drop.getHash(), drop);

                dropRef.push().setValue(dropMap);








//                JSONObject dropJSON = new JSONObject();
//                try {
//                    dropJSON.put("user", drop.getUser());
//                    dropJSON.put("text", drop.getMessage());
//                    dropJSON.put("lat", drop.getLatitude());
//                    dropJSON.put("longi", drop.getLongitude());
//                    dropJSON.put("score", drop.getScore());
//                    Toast.makeText(getApplicationContext(), "boop", Toast.LENGTH_LONG).show();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                try {
//
//                    dropJSON.put("text", "testestest");
//                    dropJSON.put("title", "TEST TtestestesttestestesttestestestITLE");
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                HttpRequestTask newRequest = new HttpRequestTask();
//                newRequest.execute(dropJSON);
//-----------------------------------------------

//                int TIMEOUT_MILLISEC = 10000;  // = 10 seconds
//                HttpParams httpParams = new BasicHttpParams();
//                HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
//                HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
//                HttpClient client = new DefaultHttpClient(httpParams);
//
//                HttpPost request = new HttpPost("https://sheltered-chamber-7333.herokuapp.com/data");
//                try {
//                    request.setEntity(new ByteArrayEntity(
//                            dropJSON.toString().getBytes("UTF8")));
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    HttpResponse response = client.execute(request);
//                    ResponseHandler<String> responseHandler=new BasicResponseHandler();
//                    String responseBody = client.execute(request, responseHandler);
//                    Toast.makeText(getApplicationContext(), responseBody, Toast.LENGTH_LONG).show();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

//-----------------------------------------------




            }
        });





    }





//    private class HttpRequestTask extends AsyncTask<JSONObject, Void, Double> {
//
//        protected Double doInBackground(JSONObject... jsonobjarr) {
//            try {
//                int TIMEOUT_MILLISEC = 10000;  // = 10 seconds
//                HttpParams httpParams = new BasicHttpParams();
//                HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
//                HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
//                HttpClient client = new DefaultHttpClient(httpParams);
//
//                HttpPost request = new HttpPost("https://droplet-server.herokuapp.com/post");
//                try {
//                    Log.w("myApp", request.getMethod());
//
//                    JSONObject jsonobj;
//                    jsonobj = jsonobjarr[0];
//                    StringEntity jsonStringEntity = new StringEntity(jsonobj.toString());
//                    jsonStringEntity.setContentType("application/json");
//                    request.setEntity(jsonStringEntity);
//
//                    try {
//                        HttpResponse response = client.execute(request);
//                        Log.d("myapp", "response " + EntityUtils.toString(response.getEntity()));
//                        Log.d("myappEntity", "content " + jsonobj.toString());
//                    } catch (ClientProtocolException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//            } catch (Exception e) {
//                Log.e("MainActivity", e.getMessage(), e);
//            }
//            return null;
//
//
//        }
//
//
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
