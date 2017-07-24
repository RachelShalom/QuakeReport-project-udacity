package com.example.android.quakereport;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Rachel on 01/07/2017.
 */

public class EarthquakeLoader extends AsyncTaskLoader<ArrayList<EarthQuakeData>> {
    private String mUrl;
// constructor
public EarthquakeLoader(Context context, String url){
    super(context);
    mUrl = url;

}
    @Override
    protected void onStartLoading() {
        forceLoad();
        Log.v("EarthquakeLoader","TEST:onsartloading is called ");
    }


    @Override
    public ArrayList<EarthQuakeData> loadInBackground() {
        Log.v("EarthquakeLoader","TEST:doitinbackground is called");
        if(mUrl==null){
            return null;
        }
        return QueryUtils.fetchEarthquakeData(mUrl);
    }
}
