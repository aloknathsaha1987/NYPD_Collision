package com.aloknath.nypd_collision.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.aloknath.nypd_collision.HttpManager.HttpManager;
import com.aloknath.nypd_collision.JSONParser.JSONParser;
import com.aloknath.nypd_collision.Objects.CollisionDetail;
import com.aloknath.nypd_collision.R;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private Button getData;
    private ProgressDialog progressDialog;
    public static List<CollisionDetail> collisions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog = new ProgressDialog(MainActivity.this);
        getData = (Button)findViewById(R.id.button);

        getData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetDataAsyncTask getDataAsyncTask = new GetDataAsyncTask();
                getDataAsyncTask.execute();

            }
        });
    }

    private class GetDataAsyncTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Fetching Data and Creating Objects");
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String content = HttpManager.getData("https://data.cityofnewyork.us/resource/h9gi-nx95.json");
            collisions = JSONParser.parseFeed(content);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.hide();
            // Start a New Activity That will display the collisions Points on the Map
            Intent intent = new Intent(MainActivity.this, CollisionMapActivity.class);
            startActivity(intent);
        }
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
        if (id == R.id.google_license) {
            Intent intent = new Intent(this, GPSLicenseActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
