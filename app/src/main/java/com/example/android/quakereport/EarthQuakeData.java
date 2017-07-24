package com.example.android.quakereport;

/**
 * Created by Rachel on 16/04/2017.
 */

public class EarthQuakeData {
    //magnitude of the earthquake
    private double mMag;
    //mamber variable location of the earthquake
    private String mLocation;

    //date andtime of the earthqucke
    private long mDate;

    private String mUrl;

    // create a new earthqukedata object

    public EarthQuakeData(double vMag,String vLocation, long vDate,String vUrl ){

        mMag = vMag;
        mLocation= vLocation;
        mDate = vDate;
        mUrl  = vUrl;
    }

    public double getMag(){
        return mMag;
    }

    public String getLocation(){
        return mLocation;
    }
    public long getDate(){
    return mDate;
    }

    public String getUrl(){
        return mUrl;
    }
}
