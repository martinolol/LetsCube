package letsapps.com.letscube.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import letsapps.com.letscube.R;
import letsapps.com.letscube.adapter.ViewPagerAdapter;
import letsapps.com.letscube.listener.SingletonChange;
import letsapps.com.letscube.singleton.TimerSettings;
import letsapps.com.letscube.util.DatabaseTime;
import letsapps.com.letscube.util.TimeList;
import letsapps.com.letscube.util.cube.CubeRecord;
import letsapps.com.letscube.util.cube.Event;
import letsapps.com.letscube.view.SlidingTabs.SlidingTabLayout;

public class TimesFragment extends LCTFragment implements SingletonChange{

    TimerTimelistFragment timelistFragment = null;
    TimerStatsFragment statsFragment = null;
    TimerChartsFragment chartsFragment = null;
    int nbTabs = 3;

    String[] titles;

    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    SlidingTabLayout tabs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_times, container, false);

        titles = new String[]{
                getContext().getString(R.string.fragment_times_times),
                getContext().getString(R.string.fragment_times_stats),
                getContext().getString(R.string.fragment_times_charts)
        };

        timelistFragment = new TimerTimelistFragment();
        statsFragment = new TimerStatsFragment();
        chartsFragment = new TimerChartsFragment();
        statsFragment.setTimesFragment(this);

        viewPagerAdapter =  new ViewPagerAdapter(getChildFragmentManager(), titles,
                new Fragment[]{timelistFragment, statsFragment, chartsFragment}, nbTabs);

        viewPager = (ViewPager) rootView.findViewById(R.id.pager);
        viewPager.setAdapter(viewPagerAdapter);

        tabs = (SlidingTabLayout) rootView.findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);

        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                if(TimerSettings.getInstance().getTheme() != null) {
                    return TimerSettings.getInstance().getTheme().getMainColor();
                }
                return Color.BLACK;
            }
        });

        tabs.setViewPager(viewPager);

        Log.d("TimesFragment", "finish onCreateView(Bundle);");

        return rootView;
    }

    public void highlightTimes(DatabaseTime[] times){
        timelistFragment.highlightTimes(times);
        viewPager.setCurrentItem(0);
    }

    @Override
    public void onCurrentEventChange(){
        if(timelistFragment != null){
            timelistFragment.onCurrentEventChange();
        }
        if(statsFragment != null){
            statsFragment.onCurrentEventChange();
        }
        if(chartsFragment != null){
            chartsFragment.onCurrentEventChange();
        }
        if(viewPagerAdapter != null){
            viewPagerAdapter.notifyDataSetChanged();
        }
    }

    public void onCurrentTimesChange(){
        if(statsFragment != null) {
            statsFragment.onCurrentTimesChange();
        }
        if(timelistFragment != null){
            timelistFragment.onCurrentTimesChange();
        }
        if(chartsFragment != null){
            chartsFragment.onCurrentTimesChange();
        }
        if(viewPagerAdapter != null){
            viewPagerAdapter.notifyDataSetChanged();
        }
    }
}
