package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by Rachel on 17/04/2017.
 */

public class EarthQuakeAdapter extends ArrayAdapter<EarthQuakeData> {

    private static final String Location_separator = "of";
    String primeLocation;
    String offSetLocation;

    //private static final String LOG_TAG = EarthQuakeData.class.getSimpleName();

    public EarthQuakeAdapter(Context context, ArrayList<EarthQuakeData> earthquakes){
        super(context,0,earthquakes);

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        EarthQuakeData eqd = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        // Lookup view for data population
        TextView eqMag = (TextView) convertView.findViewById(R.id.mag);
        TextView eqPrimeLocation = (TextView) convertView.findViewById(R.id.primary_location);
        TextView eqOffsetLocation = (TextView)  convertView.findViewById(R.id.offset_location);
        TextView eqDate = (TextView) convertView.findViewById(R.id.date);
        // Find the TextView with view ID time
        TextView timeView = (TextView) convertView.findViewById(R.id.time);
        // Create a new Date object from the time in milliseconds of the earthquake
        Date dateObject = new Date(eqd.getDate());
        // Format the date string (i.e. "Mar 3, 1984")
        String formattedDate = formatDate(dateObject);
        // Format the time string (i.e. "4:30PM")
        String formattedTime = formatTime(dateObject);
        // Populate the data into the template view using the data object
        DecimalFormat formatter = new DecimalFormat("0.0");
        String output = formatter.format(eqd.getMag());
        eqMag.setText(output);
        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) eqMag.getBackground();
// Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(eqd.getMag());
        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);


        // checks if the location contains primary and offset location by finding the word "of"
        String currentLocation = eqd.getLocation();
        int separator_index =currentLocation.indexOf(Location_separator);

        if(separator_index!= -1) {
             primeLocation = currentLocation.substring(separator_index+Location_separator.length());
             offSetLocation = currentLocation.substring(0,separator_index+Location_separator.length());
        }
        else{
            primeLocation = currentLocation;
            offSetLocation= getContext().getString(R.string.Near_the);
        }
        eqPrimeLocation.setText(primeLocation);
        eqOffsetLocation.setText(offSetLocation);
        eqDate.setText(formattedDate);
        // Display the time of the current earthquake in that TextView
        timeView.setText(formattedTime);
        // Return the completed view to render on screen
        return convertView;
    }
    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }

     private int getMagnitudeColor(double mag_level){
        // round down the mag to an int number
         int int_mag = (int)mag_level;
         int mag_color;
         mag_color = ContextCompat.getColor(getContext(), R.color.magnitude1);
         switch (int_mag) {
             case 0:
             case 1:
                mag_color = ContextCompat.getColor(getContext(), R.color.magnitude1);
                 break;
             case 2:
                 mag_color = ContextCompat.getColor(getContext(), R.color.magnitude2);
                 break;
             case 3:
                 mag_color = ContextCompat.getColor(getContext(), R.color.magnitude3);
                 break;
             case 4:
                 mag_color = ContextCompat.getColor(getContext(), R.color.magnitude4);
                 break;
             case 5:
                 mag_color = ContextCompat.getColor(getContext(), R.color.magnitude5);
                 break;
             case 6:
                 mag_color = ContextCompat.getColor(getContext(), R.color.magnitude6);
                 break;
             case 7:
                 mag_color = ContextCompat.getColor(getContext(), R.color.magnitude7);
                 break;
             case 8:
                 mag_color = ContextCompat.getColor(getContext(), R.color.magnitude8);
                 break;
             case 9:
                 mag_color = ContextCompat.getColor(getContext(), R.color.magnitude9);
                 break;
             case 10:
                 mag_color = ContextCompat.getColor(getContext(), R.color.magnitude10plus);
                 break;
             default:
                 break;
         }
         return mag_color;
     }

}
