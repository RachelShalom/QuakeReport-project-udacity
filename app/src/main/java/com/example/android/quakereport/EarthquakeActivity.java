/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<EarthQuakeData>> {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    private static final int EARTHQUAKE_LOADER_ID = 1;
    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query";
    ConnectivityManager cm;
    boolean isConnected;
    private EarthQuakeAdapter mAdapter;
    private TextView mEmptyText;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        mEmptyText = (TextView) findViewById(R.id.empty);
        mProgressBar = (ProgressBar) findViewById(R.id.loading_spinner);
        ListView earthquakeListView = (ListView) findViewById(R.id.list);
        //check for network connectivity
        cm = (ConnectivityManager) EarthquakeActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        earthquakeListView.setEmptyView(mEmptyText);
        // Create a new {@link ArrayAdapter} of  empty earthquakes
        mAdapter = new EarthQuakeAdapter(EarthquakeActivity.this, new ArrayList<EarthQuakeData>());
        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(mAdapter);
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                EarthQuakeData eq_item = (EarthQuakeData) adapterView.getItemAtPosition(i);
                String current_url = eq_item.getUrl();
                Intent earth_quake_url = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(current_url));
                startActivity(earth_quake_url);
            }
        });
        if (isConnected) {
            getSupportLoaderManager().initLoader(EARTHQUAKE_LOADER_ID, null, this);
            Log.v(LOG_TAG, "TEST:getloadermanager and initLoaderis called");
        } else {
            mEmptyText.setText(R.string.no_internet);
            mProgressBar.setVisibility(View.GONE);

        }
    }

    @Override
    public Loader<ArrayList<EarthQuakeData>> onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG, "TEST:oncreateLoader is called");
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude = sharedPrefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "10");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby",orderBy);


        return new EarthquakeLoader(EarthquakeActivity.this, uriBuilder.toString());


    }

    @Override
    public void onLoadFinished(Loader<ArrayList<EarthQuakeData>> loader, ArrayList<EarthQuakeData> earthQuakeDatas) {

        // clean the adapter from previous data of the list

        mAdapter.clear();
        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (earthQuakeDatas != null && !earthQuakeDatas.isEmpty()) {
            mAdapter.addAll(earthQuakeDatas);
            Log.v(LOG_TAG, " TEST:OnLoad finish is called");
        } else {

            mEmptyText.setText(R.string.no_eq);
        }
        mProgressBar.setVisibility(View.GONE);

    }


    @Override
    public void onLoaderReset(Loader<ArrayList<EarthQuakeData>> loader) {

        // Loader reset, to clear out our existing data.
        mAdapter.clear();
        Log.v(LOG_TAG, "TEST: onloadreset is called");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
