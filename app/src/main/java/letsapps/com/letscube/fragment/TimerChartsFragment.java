package letsapps.com.letscube.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import letsapps.com.letscube.R;
import letsapps.com.letscube.activity.TimerActivity;
import letsapps.com.letscube.listener.SingletonChange;
import letsapps.com.letscube.singleton.AppInstance;
import letsapps.com.letscube.util.AverageCalculator;
import letsapps.com.letscube.util.TimerUtils;
import letsapps.com.letscube.util.cube.Event;
import letsapps.com.letscube.view.AverageArrayView;
import letsapps.com.letscube.view.chart.ChartHistogramView;
import letsapps.com.letscube.view.chart.ChartLineView;

public class TimerChartsFragment extends LCTFragment implements SingletonChange {

    LinearLayout chartLayout = null;
    TextView emptyChartsTV = null;
    ChartLineView timesCLV;
    ChartHistogramView timesCHV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_timer_charts, container, false);

        chartLayout = (LinearLayout)rootView.findViewById(R.id.chart_layout);
        emptyChartsTV = (TextView)rootView.findViewById(R.id.empty_chart);
        timesCLV = (ChartLineView)rootView.findViewById(R.id.chart_line_times);
        timesCHV = (ChartHistogramView)rootView.findViewById(R.id.chart_histogram_times);

        updateDatas();

        return rootView;
    }

    @Override
    public void onCurrentEventChange(){
        if(timesCLV != null) {
            updateDatas();
        }
    }

    public void onCurrentTimesChange(){
        if(timesCLV != null) {
            updateDatas();
        }
    }

    private void updateDatas(){
        timesCLV.refreshData();
        timesCHV.refreshData();
        if(timesCLV.getNbDatas() < 2){
            chartLayout.setVisibility(View.GONE);
            emptyChartsTV.setVisibility(View.VISIBLE);
        }else{
            chartLayout.setVisibility(View.VISIBLE);
            emptyChartsTV.setVisibility(View.GONE);
        }
    }
}
