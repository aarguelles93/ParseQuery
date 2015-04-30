package com.uninorte.andresarguelles.parsequery;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Andres Arguelles on 30/04/2015.
 */
public class Frag1 extends Fragment implements LocationListener {


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

        return rootView;
    }


    @Override
    public void onLocationChanged(Location location) {

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