package letsapps.com.letscube.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import letsapps.com.letscube.R;
import letsapps.com.letscube.puzzle.Puzzle;
import letsapps.com.letscube.singleton.TimerSettings;
import letsapps.com.letscube.util.TimeList;
import letsapps.com.letscube.util.cube.CubeRecord;
import letsapps.com.letscube.view.chart.ChartHistogramView;
import letsapps.com.letscube.view.chart.ChartLineView;
import letsapps.com.letscube.view.pattern.PuzzlePatternView;

public class HomeInfoView extends RelativeLayout {

    View rootView;
    AverageArrayView averageAV;
    PuzzlePatternView puzzlePattern;
    ChartLineView chartLine;
    ChartHistogramView chartHistogram;
    TextView descTV;

    public HomeInfoView(Context context) {
        super(context);
        init();
    }

    public HomeInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HomeInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        final LayoutInflater inflater =
                (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootView = inflater.inflate(R.layout.view_home_info, this);

        averageAV = (AverageArrayView)rootView.findViewById(R.id.average_array);
        puzzlePattern = (PuzzlePatternView)rootView.findViewById(R.id.cube_pattern);
        chartLine = (ChartLineView)rootView.findViewById(R.id.chart_line);
        chartHistogram = (ChartHistogramView)rootView.findViewById(R.id.chart_histogram);
        descTV = (TextView)rootView.findViewById(R.id.desc);
    }

    public void showCorrectView(Puzzle puzzle){
        final int homeInfoId = TimerSettings.getInstance().getHomeInfo();
        switch (homeInfoId){
            case TimerSettings.HOME_INFO_NOTHING_ID :
                this.setVisibility(GONE); break;

            case TimerSettings.HOME_INFO_AVERAGES_ID :
                this.setVisibility(VISIBLE);
                averageAV.setVisibility(VISIBLE);
                puzzlePattern.setVisibility(GONE);
                chartLine.setVisibility(GONE);
                chartHistogram.setVisibility(GONE);
                descTV.setVisibility(GONE); break;

            case TimerSettings.HOME_INFO_PATTERN_ID :
                this.setVisibility(VISIBLE);
                averageAV.setVisibility(GONE);
                chartLine.setVisibility(GONE);
                chartHistogram.setVisibility(GONE);
                if(puzzle != null){
                    puzzlePattern.setVisibility(VISIBLE);
                    descTV.setVisibility(GONE);
                    puzzlePattern.setPuzzle(puzzle);
                }else{
                    puzzlePattern.setVisibility(GONE);
                    descTV.setVisibility(VISIBLE);
                }
                break;

            case TimerSettings.HOME_INFO_CHART_LINE_ID :
                this.setVisibility(VISIBLE);
                averageAV.setVisibility(GONE);
                puzzlePattern.setVisibility(GONE);
                chartLine.setVisibility(VISIBLE);
                chartHistogram.setVisibility(GONE);
                descTV.setVisibility(GONE); break;

            case TimerSettings.HOME_INFO_CHART_HISTOGRAM_ID :
                this.setVisibility(VISIBLE);
                averageAV.setVisibility(GONE);
                puzzlePattern.setVisibility(GONE);
                chartLine.setVisibility(GONE);
                chartHistogram.setVisibility(VISIBLE);
                descTV.setVisibility(GONE); break;
        }
    }

    public void refreshData(){
        if(averageAV != null) { // l'une non nulle inclut les autres vues non nulles.
            averageAV.refreshData();
            chartLine.refreshData();
            chartHistogram.refreshData();
        }
    }
}
