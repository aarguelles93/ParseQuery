package com.uninorte.andresarguelles.parsequery;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andres Arguelles on 30/04/2015.
 */
public class Frag1 extends Fragment implements LocationListener {

    public ArrayList<LocationPost> mLocationPosts;

    private Button mButtonQuery;
    private boolean mLocating;
    private LocationManager mLocationManager;

    private static final String ARG_SECTION_NUMBER = "section_number";


    public static Frag1 newInstance(int sectionNumber) {
        Frag1 fragment = new Frag1();
        Bundle args = new Bundle();

        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_frag1, container, false);
        //VISUAL INSTANCES
        mButtonQuery = (Button) rootView.findViewById(R.id.buttonQuery);

        mLocating=false;

        mLocationPosts = new ArrayList<LocationPost>();

        mButtonQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("GreenMarkers");
                query.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> markers, ParseException e) {
                        if (e == null) {

                        } else {
                            // handle Parse Exception here
                        }
                    }
                });
            }
        });

        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();
        if(!mLocating)
            startCapture(getActivity());

    }

    public void startCapture(Context context){
        mLocating = true;

        mLocationManager = (LocationManager) getActivity().getSystemService(
                Context.LOCATION_SERVICE);
        boolean enabled = mLocationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        if(!enabled){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            // Set other dialog properties
            builder.setTitle("GPS no esta habilitado");
            builder.setMessage("Habilitar ahora?");
            builder.setCancelable(false);
            // Add the buttons
            builder.setNegativeButton("Ahora no", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    return;
                }
            });

            builder = builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                    return;
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                0, 0, this);
    }

    @Override
    public void onStop(){
        super.onStop();

        mLocating = false;
        if(mLocationManager != null){
            mLocationManager.removeUpdates(this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        ParseGeoPoint geoPoint = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
        LocationPost locPost = new LocationPost();
        locPost.setLocation(geoPoint);
        locPost.saveInBackground();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}