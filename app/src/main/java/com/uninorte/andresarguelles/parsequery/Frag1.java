package com.uninorte.andresarguelles.parsequery;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andres Arguelles on 30/04/2015.
 */
public class Frag1 extends Fragment implements LocationListener {


    private Button mButtonQuery;
    private ListView mListView;

    private boolean mLocating;
    private LocationManager mLocationManager;

    public Location currentLocation;
    public Location lastLocation;

    private ProgressDialog pDialog;
    List<ParseObject> ob;
    private ArrayList values;


    private ParseQueryAdapter<LocationPost> postsQueryAdapter;

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
        final View rootView = inflater.inflate(R.layout.fragment_frag1, container, false);
        //VISUAL INSTANCES
        mButtonQuery = (Button) rootView.findViewById(R.id.buttonQuery);
        mListView = (ListView) rootView.findViewById(R.id.posts_listview);


        mLocating=false;

        /*
         * PARSE SETUP CONFIG
         */

        // Set up a customized query
        /*
        ParseQueryAdapter.QueryFactory<LocationPost> factory =
                new ParseQueryAdapter.QueryFactory<LocationPost>() {
                    public ParseQuery<LocationPost> create() {
                        ParseQuery<LocationPost> query = LocationPost.getQuery();
                        query.orderByDescending("createdAt");
                        query.setLimit(20);
                        return query;
                    }
                };
                */
        // Set up the query adapter
        /*
        postsQueryAdapter = new ParseQueryAdapter<LocationPost>(getActivity(), factory);


        // Disable automatic loading when the adapter is attached to a view.
        postsQueryAdapter.setAutoload(false);

        // Disable pagination, we'll manage the query limit ourselves
        postsQueryAdapter.setPaginationEnabled(false);
        // Attach the query adapter to the view
        ListView postsListView = (ListView) rootView.findViewById(R.id.posts_listview);
        postsListView.setAdapter(postsQueryAdapter);
        */

        /*
         *
         */

        //mLocationPosts = new ArrayList<LocationPost>();



        mButtonQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetData().execute();
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


        mLocating = false;
        if(mLocationManager != null){
            mLocationManager.removeUpdates(this);
        }

        super.onStop();
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;

        if(lastLocation != null
                && geoPointFromLocation(location).distanceInKilometersTo(geoPointFromLocation(lastLocation)) < 0.01){
            return;
        }
        lastLocation = location;

        ParseGeoPoint geoPoint = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
        //LocationPost locPost = new LocationPost();
        //locPost.setLocation(geoPoint);
        //locPost.saveInBackground();
        ParseObject parseLocation = new ParseObject("LocationPost");
        parseLocation.put("location",geoPoint);
        parseLocation.saveInBackground();
        Toast.makeText(getActivity(),"New position uploaded", Toast.LENGTH_LONG).show();
    }

    private ParseGeoPoint geoPointFromLocation(Location loc) {
        return new ParseGeoPoint(loc.getLatitude(), loc.getLongitude());
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



    private class GetData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            pDialog = new ProgressDialog(getActivity());
            // Set progressdialog title
            pDialog.setTitle("Fetching Parse data");
            // Set progressdialog message
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            // Show progressdialog
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // Create the array
            values = new ArrayList<String>();
            try {

                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("LocationPost");

                ob = query.find();
                for (ParseObject dato : ob) {

                    //values.add(dato.get("first")+ " " + dato.get("last"));
                    values.add(dato.get("location"));

                }
            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Locate the listview in listview_main.xml
            // Pass the results into ListViewAdapter.java

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1, android.R.id.text1, values);

            mListView.setAdapter(adapter);

            // Close the progressdialog
            pDialog.dismiss();
        }
    }

}