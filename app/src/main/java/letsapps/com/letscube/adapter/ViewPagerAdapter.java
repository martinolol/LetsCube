package letsapps.com.letscube.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int nbPage; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
    Fragment[] fragments;


    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter(FragmentManager fm, CharSequence titles[], Fragment[] fragments, int nbPage) {
        super(fm);

        this.titles = titles;
        this.nbPage = nbPage;
        this.fragments = fragments;

        for(Fragment f : fragments){
            Log.d("VPA", "f is " + (f == null?"null":"not null"));
        }

    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {
        Log.d("VPA", "getItem(int) : " + position);
        return fragments[position];

    }

    @Override
    public int getItemPosition(Object object) {
        // Causes adapter to reload all Fragments when
        // notifyDataSetChanged is called
        return POSITION_NONE;
    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return nbPage;
    }
}