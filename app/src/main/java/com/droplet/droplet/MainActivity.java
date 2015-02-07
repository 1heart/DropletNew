package com.droplet.droplet;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends ActionBarActivity implements AbsListView.OnScrollListener {
     Button locationButton;
     ProgressBar progressBar;
     LocationGetter locationGet;
     Firebase rootRef;
     ListView dropListView;
     InfiniteScrollAdapter<String> adapter;
     Handler mHandler;
     ArrayList<String> vals;
     int valsCounter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        vals = new ArrayList<String>();
        vals.add("START");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.MainToolbar);
        setSupportActionBar(toolbar);

        for(int i = 0; i < 20; i++){
            vals.add("");
        }


        locationGet = new LocationGetter(MainActivity.this);

        locationButton = (Button) findViewById(R.id.locationGetBtn);
        locationButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

                StrictMode.setThreadPolicy(policy);
                double latitude = locationGet.getLatitude();
                double longitude = locationGet.getLongitude();
                Log.wtf("WHEEEEE", "LONG " + longitude + " LAT " + latitude);
                Droplet drop = new Droplet(longitude, latitude, "Edward", "ayy lmao");
                vals.add(""+longitude+" " + latitude);
                PushFirebaseAsync runFirebase = new PushFirebaseAsync();
                runFirebase.execute(drop);
                GetFirebaseAsync getFirebase = new GetFirebaseAsync(longitude, latitude);
                getFirebase.execute(1);
            }
        });

        mHandler = new Handler();
        View footer = getLayoutInflater().inflate(R.layout.progress_bar_footer, null);
        progressBar = (ProgressBar) footer.findViewById(R.id.progressBar);

        dropListView = (ListView) findViewById(R.id.mainListView);
        dropListView.addFooterView(footer);
        adapter = new InfiniteScrollAdapter<String>(this, vals, 20, 10);
        dropListView.setAdapter(adapter);
        dropListView.setOnScrollListener(this);
        progressBar.setVisibility((20 < vals.size())? View.VISIBLE : View.GONE);

    }





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

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(firstVisibleItem + visibleItemCount == totalItemCount && !adapter.endReached() && !hasCallback){ //check if we've reached the bottom
//            double latitude = locationGet.getLatitude();
//            double longitude = locationGet.getLongitude();
//            Log.wtf("WHEEEEE", "LONG " );
//            vals.add(" "+longitude+" " + latitude);
//            adapter.notifyDataSetChanged();

            mHandler.post(showMore);
            hasCallback = true;
        }
    }

    private boolean hasCallback;
    private Runnable showMore = new Runnable(){
        public void run(){
            boolean noMoreToShow = adapter.showMore();
            progressBar.setVisibility(noMoreToShow? View.GONE : View.VISIBLE);
            hasCallback = false;
        }
    };

    private class PushFirebaseAsync extends AsyncTask<Droplet , Void, Void> {


        @Override
        protected Void doInBackground(Droplet... drops) {
            Firebase.setAndroidContext(getApplicationContext());
            rootRef = new Firebase("https://dropletserver.firebaseio.com/");
            Firebase dropRef = rootRef.child("Drops");
//            Map<String, Droplet> dropMap = new HashMap<String, Droplet>();
//            dropMap.put(drops[0].getHash(), drops[0]);
            dropRef.push().setValue(drops[0]);
            return null;
        }

    }


    private class GetFirebaseAsync extends AsyncTask<Integer , Void, Void> {

        private double longi;
        private double lati;

        public GetFirebaseAsync(double longIn, double latIn){
            super();
            longi = longIn;
            lati = latIn;
        }



        // returns [lowerLong, upperLong, lowerLat, upperLat
        private double[] getBounds(){

            //Assuming 1 deg lat is 111111 meters, 1 degree long is 111111*cos(lat) meters
            //800 meters is...
            double dLat = 0.0072;
            double dLong = 800/111111*Math.cos(Math.toRadians(dLat));

            double[] result = { longi - dLong, longi + dLong, lati - dLat, lati + dLat  };
            return result;
        }

        @Override
        protected Void doInBackground(Integer... i) {
            Firebase.setAndroidContext(getApplicationContext());
            rootRef = new Firebase("https://dropletserver.firebaseio.com/");
            Firebase dropRef = rootRef.child("Drops");
            double[] boundsArr = getBounds();
            Query locQuery = dropRef.orderByChild("longitude").startAt(boundsArr[0]).endAt(boundsArr[1]).orderByChild("latitude").startAt(boundsArr[2]).endAt(boundsArr[3]);

            locQuery.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                    Map<String, Object> value = (Map<String, Object>)snapshot.getValue();
                    Log.wtf("WLUIRAHWEF" , "long: " + value.get("longitude") + " lati: " + value.get("latitude"));
                    System.out.println(snapshot.getKey() + " was " + value.get("height") + " meters tall");
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }

            });
            return null;
        }

    }
}

