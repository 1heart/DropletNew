package com.droplet.droplet;
import android.app.ActionBar;
import android.content.Context;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
                //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                Droplet drop = new Droplet(longitude, latitude, "Edward", "ayy lmao");
                vals.add(""+longitude+" " + latitude);
                FirebaseAsync runFirebase = new FirebaseAsync();
                runFirebase.execute(drop);

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

    private class FirebaseAsync extends AsyncTask<Droplet , Void, Void> {


        @Override
        protected Void doInBackground(Droplet... drops) {
            Firebase.setAndroidContext(getApplicationContext());
            rootRef = new Firebase("https://dropletserver.firebaseio.com/");
            Firebase dropRef = rootRef.child("Drops");
            Map<String, Droplet> dropMap = new HashMap<String, Droplet>();
            dropMap.put(drops[0].getHash(), drops[0]);
            dropRef.push().setValue(dropMap);
            return null;
        }

    }
}

