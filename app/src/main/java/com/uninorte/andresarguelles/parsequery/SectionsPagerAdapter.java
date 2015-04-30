package com.uninorte.andresarguelles.parsequery;

/**
 * Created by Andres Arguelles on 24/04/2015.
 */

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Locale;


public class SectionsPagerAdapter extends FragmentPagerAdapter {


    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).

        //if (position  == 0)
            return Frag1.newInstance(position + 1);
        //else return Frag2.newInstance(position + 1);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        Resources res = Resources.getSystem();
        switch (position) {
            case 0:
                return res.getString(R.string.title_section1).toUpperCase(l);
        }
        return null;
    }
}
