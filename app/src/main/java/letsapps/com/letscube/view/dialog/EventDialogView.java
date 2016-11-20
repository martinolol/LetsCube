package letsapps.com.letscube.view.dialog;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import java.util.ArrayList;

import letsapps.com.letscube.R;
import letsapps.com.letscube.activity.TimerActivity;
import letsapps.com.letscube.adapter.EventViewPagerAdapter;
import letsapps.com.letscube.listener.EventClickListener;
import letsapps.com.letscube.singleton.AppInstance;
import letsapps.com.letscube.singleton.TimerSettings;
import letsapps.com.letscube.util.Theme;
import letsapps.com.letscube.util.cube.Event;
import letsapps.com.letscube.view.SlidingTabs.SlidingTabLayout;

public class EventDialogView extends LinearLayout {

    private EventViewPagerAdapter viewPagerAdapter;
    private ViewPager viewPager;

    SlidingTabLayout tabs;

    public EventDialogView(final Context context, ArrayList<Event> addedEvents,
                           EventClickListener eventClickListener) {
        super(context);

        final LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rootView = inflater.inflate(R.layout.dialog_events, this);

        final String[] tabsTitles = new String[2];
        tabsTitles[0] = context.getString(R.string.event_official);
        tabsTitles[1] = context.getString(R.string.event_unofficial);

        Log.i("EventDialogV ()", "currentEvent = " + AppInstance.getInstance().getEvent().getName(context));

        viewPagerAdapter =  new EventViewPagerAdapter(context, tabsTitles,
                AppInstance.getInstance().getEvent(), addedEvents, eventClickListener);
        viewPager = (ViewPager) rootView.findViewById(R.id.pager);
        viewPager.setAdapter(viewPagerAdapter);

        tabs = (SlidingTabLayout) rootView.findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);

        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return TimerSettings.getInstance().getTheme().getMainColor();
            }
        });

        tabs.setViewPager(viewPager);

    }
}
