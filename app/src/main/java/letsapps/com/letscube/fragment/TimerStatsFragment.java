package letsapps.com.letscube.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import letsapps.com.letscube.R;
import letsapps.com.letscube.activity.TimerActivity;
import letsapps.com.letscube.listener.SingletonChange;
import letsapps.com.letscube.singleton.AppInstance;
import letsapps.com.letscube.util.DatabaseTime;
import letsapps.com.letscube.listener.AverageArrayTimeClickListener;
import letsapps.com.letscube.util.AverageCalculator;
import letsapps.com.letscube.util.TimeList;
import letsapps.com.letscube.util.TimerUtils;
import letsapps.com.letscube.util.cube.CubeRecord;
import letsapps.com.letscube.util.cube.Event;
import letsapps.com.letscube.view.AverageArrayView;
import letsapps.com.letscube.view.chart.ChartHistogramView;
import letsapps.com.letscube.view.chart.ChartLineView;

public class TimerStatsFragment extends LCTFragment implements SingletonChange {

    AverageArrayView averageArrayV;
    TextView nbTimesTV, totalMeanTV;

    TimesFragment timesFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_timer_stats, container, false);

        averageArrayV = (AverageArrayView)rootView.findViewById(R.id.average_array);

        nbTimesTV = (TextView)rootView.findViewById(R.id.nb_times);
        totalMeanTV = (TextView)rootView.findViewById(R.id.total_mean);

        updateDatas();

        averageArrayV.setTimeClickListener(clickTimeAverageArray);

        return rootView;
    }

    private AverageArrayTimeClickListener clickTimeAverageArray = new AverageArrayTimeClickListener() {
        @Override
        public void OnClickTime(DatabaseTime[] concernedTimes) {
            timesFragment.highlightTimes(concernedTimes);
        }
    };

    //récupère timesFragment pour pouvoir chager de tabs lors du clic sur un event
    public void setTimesFragment(TimesFragment timesFragment){
        this.timesFragment = timesFragment;
    }

    public void onCurrentEventChange(){
        if(averageArrayV != null) {
            updateDatas();
        }
    }

    public void onCurrentTimesChange() {
        if(averageArrayV != null) {
            updateDatas();
        }
    }

    private void updateDatas() {
        if (averageArrayV != null && nbTimesTV != null && totalMeanTV != null) {
            averageArrayV.refreshData();
            nbTimesTV.setText(getContext().getString(
                    R.string.fragment_stats_nbtimes,
                    AppInstance.getInstance().getTimes().getStats().getNbTimes()));
            totalMeanTV.setText(getContext().getString(
                    R.string.fragment_stats_totalmean,
                    AppInstance.getInstance().getTimes().getStats().getNbTimes(),
                    TimerUtils.formatTime((int)
                            AppInstance.getInstance().getTimes().getStats().getTotalAverage())));
            Log.i("TstF", "totalAvg : " +
                    AppInstance.getInstance().getTimes().getStats().getTotalAverage());
        }
    }

}
