package letsapps.com.letscube.view;

import android.app.AlertDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import letsapps.com.letscube.R;
import letsapps.com.letscube.database.DatabaseTimeHandler;
import letsapps.com.letscube.singleton.AppInstance;
import letsapps.com.letscube.singleton.TimerSettings;
import letsapps.com.letscube.util.DatabaseTime;
import letsapps.com.letscube.listener.AverageArrayTimeClickListener;
import letsapps.com.letscube.util.AverageCalculator;
import letsapps.com.letscube.util.TimeList;
import letsapps.com.letscube.util.TimerUtils;
import letsapps.com.letscube.util.cube.CubeRecord;
import letsapps.com.letscube.util.cube.RecordList;
import letsapps.com.letscube.view.dialog.BestTimesDialogView;

public class AverageArrayView extends RelativeLayout {

    final int NB_ROWS = 7;

    AlertDialog.Builder bestTimesDialog;
    BestTimesDialogView bestTimesDialogView;

    TextView singleInCourseTV, mo3inCourseTV, avg5inCourseTV, avg12inCourseTV, mo50inCourseTV, mo100inCourseTV;
    TextView singleBestTV, mo3bestTV, avg5bestTV, avg12bestTV, mo50bestTV, mo100bestTV;

    DatabaseTimeHandler databaseTimeHandler = null;

    AverageArrayTimeClickListener timeClickListener;

    public AverageArrayView(Context context) {
        super(context);
        init();
    }

    public AverageArrayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AverageArrayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        Log.d("AAV", "init() start");

        final LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rootView = inflater.inflate(R.layout.view_average_array, this);

        Log.d("AAV", "init() inflater setups");

        singleInCourseTV = (TextView)rootView.findViewById(R.id.single_in_course);
        mo3inCourseTV = (TextView)rootView.findViewById(R.id.mo3_in_course);
        avg5inCourseTV = (TextView)rootView.findViewById(R.id.avg5_in_course);
        avg12inCourseTV = (TextView)rootView.findViewById(R.id.avg12_in_course);
        mo50inCourseTV = (TextView)rootView.findViewById(R.id.mo50_in_course);
        mo100inCourseTV = (TextView)rootView.findViewById(R.id.mo100_in_course);

        singleBestTV = (TextView)rootView.findViewById(R.id.single_best);
        mo3bestTV = (TextView)rootView.findViewById(R.id.mo3_best);
        avg5bestTV = (TextView)rootView.findViewById(R.id.avg5_best);
        avg12bestTV = (TextView)rootView.findViewById(R.id.avg12_best);
        mo50bestTV = (TextView)rootView.findViewById(R.id.mo50_best);
        mo100bestTV = (TextView)rootView.findViewById(R.id.mo100_best);

        bestTimesDialog = new AlertDialog.Builder(getContext());
        bestTimesDialogView = new BestTimesDialogView(getContext());
        bestTimesDialog.setView(bestTimesDialogView);

        databaseTimeHandler = new DatabaseTimeHandler(getContext());

    }

    public void refreshData(){

        Log.d("AAV", "refreshData() start");

        final TimeList data = AppInstance.getInstance().getTimes();

        singleInCourseTV.setText(TimerUtils.formatTime(
                AverageCalculator.calculateLastMean(data, 1),
                false, 0,
                TimerSettings.TIMER_UPDATE_HUNDREDTH));
        mo3inCourseTV.setText(TimerUtils.formatTime(
                AverageCalculator.calculateLastMean(data, 3),
                false, 0,
                TimerSettings.TIMER_UPDATE_HUNDREDTH));
        avg5inCourseTV.setText(TimerUtils.formatTime(
                AverageCalculator.calculateLastAverage(data, 5),
                false, 0,
                TimerSettings.TIMER_UPDATE_HUNDREDTH));
        avg12inCourseTV.setText(TimerUtils.formatTime(
                AverageCalculator.calculateLastAverage(data, 12),
                false, 0,
                TimerSettings.TIMER_UPDATE_HUNDREDTH));
        mo50inCourseTV.setText(TimerUtils.formatTime(
                AverageCalculator.calculateLastMean(data, 50),
                false, 0,
                TimerSettings.TIMER_UPDATE_HUNDREDTH));
        mo100inCourseTV.setText(TimerUtils.formatTime(
                AverageCalculator.calculateLastMean(data, 100),
                false, 0,
                TimerSettings.TIMER_UPDATE_HUNDREDTH));

        if(data.getRecord() != null) {
            singleBestTV.setText(TimerUtils.formatTime(
                    data.getRecord().getSingle(),
                    false, 0,
                    TimerSettings.TIMER_UPDATE_HUNDREDTH));
            mo3bestTV.setText(TimerUtils.formatTime(
                    data.getRecord().getMo3(),
                    false, 0,
                    TimerSettings.TIMER_UPDATE_HUNDREDTH));
            avg5bestTV.setText(TimerUtils.formatTime(
                    data.getRecord().getAvg5(),
                    false, 0,
                    TimerSettings.TIMER_UPDATE_HUNDREDTH));
            avg12bestTV.setText(TimerUtils.formatTime(
                    data.getRecord().getAvg12(),
                    false, 0,
                    TimerSettings.TIMER_UPDATE_HUNDREDTH));
            mo50bestTV.setText(TimerUtils.formatTime(
                    data.getRecord().getMo50(),
                    false, 0,
                    TimerSettings.TIMER_UPDATE_HUNDREDTH));
            mo100bestTV.setText(TimerUtils.formatTime(
                    data.getRecord().getMo100(),
                    false, 0,
                    TimerSettings.TIMER_UPDATE_HUNDREDTH));

        }else{
            singleBestTV.setText(TimerUtils.TIME_NO_VALUE_TEXT);
            mo3bestTV.setText(TimerUtils.TIME_NO_VALUE_TEXT);
            avg5bestTV.setText(TimerUtils.TIME_NO_VALUE_TEXT);
            avg12bestTV.setText(TimerUtils.TIME_NO_VALUE_TEXT);
            mo50bestTV.setText(TimerUtils.TIME_NO_VALUE_TEXT);
            mo100bestTV.setText(TimerUtils.TIME_NO_VALUE_TEXT);
        }
        setListenersToViews();

    }

    public void setTimeClickListener(AverageArrayTimeClickListener timeClickListener){
        this.timeClickListener = timeClickListener;
        setListenersToViews();
    }

    private void setListenersToViews(){
        final TimeList data = AppInstance.getInstance().getTimes();

        if(timeClickListener != null && data != null) {

            final int datasSize = data.size();
            if(datasSize == 0){
                return;
            }

            List<DatabaseTime> tempList;

            Log.d("AAV (setListeners)", "datasSize = " + data.size());

            singleInCourseTV.setOnClickListener(
                    new OnCurrentTimeClickListener(new DatabaseTime[]{data.get(datasSize - 1)}));
            if (datasSize >= 3) {
                tempList = new ArrayList(data.subList(datasSize - 3, datasSize));
                Collections.reverse(tempList);
                mo3inCourseTV.setOnClickListener(
                        new OnCurrentTimeClickListener(tempList.toArray(new DatabaseTime[]{})));
            }
            if(datasSize >= 5){
                tempList = new ArrayList(data.subList(datasSize - 5, datasSize));
                Collections.reverse(tempList);
                avg5inCourseTV.setOnClickListener(
                        new OnCurrentTimeClickListener(tempList.toArray(new DatabaseTime[]{})));
            }
            if(datasSize >= 12) {
                tempList = new ArrayList(data.subList(datasSize - 12, datasSize));
                Collections.reverse(tempList);
                avg12inCourseTV.setOnClickListener(
                        new OnCurrentTimeClickListener(tempList.toArray(new DatabaseTime[]{})));
            }
            if(datasSize >= 50) {
                tempList = new ArrayList(data.subList(datasSize - 50, datasSize));
                Collections.reverse(tempList);
                mo50inCourseTV.setOnClickListener(
                        new OnCurrentTimeClickListener(tempList.toArray(new DatabaseTime[]{})));
            }
            if(datasSize >= 100) {
                tempList = new ArrayList(data.subList(datasSize - 100, datasSize));
                Collections.reverse(tempList);
                mo100inCourseTV.setOnClickListener(
                        new OnCurrentTimeClickListener(tempList.toArray(new DatabaseTime[]{})));
            }

            if(data.getRecord() != null) {
                singleBestTV.setOnClickListener(
                        new OnBestTimeClickListener(databaseTimeHandler.getTimes(
                                AppInstance.getInstance().getEvent().getId(),
                                1)));
                mo3bestTV.setOnClickListener(
                        new OnBestTimeClickListener(databaseTimeHandler.getTimes(
                                AppInstance.getInstance().getEvent().getId(),
                                3)));
                avg5bestTV.setOnClickListener(
                        new OnBestTimeClickListener(databaseTimeHandler.getTimes(
                                AppInstance.getInstance().getEvent().getId(),
                                5)));
                avg12bestTV.setOnClickListener(
                        new OnBestTimeClickListener(databaseTimeHandler.getTimes(
                                AppInstance.getInstance().getEvent().getId(),
                                12)));
                mo50bestTV.setOnClickListener(
                        new OnBestTimeClickListener(databaseTimeHandler.getTimes(
                                AppInstance.getInstance().getEvent().getId(),
                                50)));
                mo100bestTV.setOnClickListener(
                        new OnBestTimeClickListener(databaseTimeHandler.getTimes(
                                AppInstance.getInstance().getEvent().getId(),
                                100)));
            }

        }
    }

    public class OnCurrentTimeClickListener implements OnClickListener{

        DatabaseTime[] times;

        public OnCurrentTimeClickListener(DatabaseTime[] times){
            this.times = times;
        }

        @Override
        public void onClick(View v) {
            if(timeClickListener != null && times != null){
                timeClickListener.OnClickTime(times);
            }
        }
    }

    public class OnBestTimeClickListener implements OnClickListener{

        DatabaseTime[] times;

        public OnBestTimeClickListener(DatabaseTime[] times){
            this.times = times;
        }

        @Override
        public void onClick(View v) {
            if(times != null) {
                bestTimesDialogView = new BestTimesDialogView(getContext());
                bestTimesDialogView.setTimes(times);
                bestTimesDialog = new AlertDialog.Builder(getContext());
                bestTimesDialog.setView(bestTimesDialogView);
                bestTimesDialog.setNegativeButton(R.string.close, null);
                bestTimesDialog.show();
            }
        }
    }

    public void makeRowVisible(int rowIndex){
        final View descV = ((LinearLayout)findViewById(R.id.layout_desc)).getChildAt(rowIndex);
        final View inCourseV = ((LinearLayout)findViewById(R.id.layout_in_course)).getChildAt(rowIndex);
        final View bestV = ((LinearLayout)findViewById(R.id.layout_best)).getChildAt(rowIndex);

        if(descV != null && inCourseV != null && bestV != null) {

            descV.setVisibility(View.VISIBLE);
            inCourseV.setVisibility(View.VISIBLE);
            bestV.setVisibility(View.VISIBLE);
        }
    }

    public void makeRowInvisible(int rowIndex){
        final View descV = ((LinearLayout)findViewById(R.id.layout_desc)).getChildAt(rowIndex);
        final View inCourseV = ((LinearLayout)findViewById(R.id.layout_in_course)).getChildAt(rowIndex);
        final View bestV = ((LinearLayout)findViewById(R.id.layout_best)).getChildAt(rowIndex);

        if(descV != null && inCourseV != null && bestV != null) {

            descV.setVisibility(View.INVISIBLE);
            inCourseV.setVisibility(View.INVISIBLE);
            bestV.setVisibility(View.INVISIBLE);

        }
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        final int arrayHeight = MeasureSpec.getSize(heightMeasureSpec);

        View v = findViewById(R.id.single_best);
        measureChildWithMargins(v, widthMeasureSpec, 0, heightMeasureSpec, 0);
        final int rowHeight = v.getMeasuredHeight();

        if(arrayHeight >= rowHeight * NB_ROWS || arrayHeight == 0){
            for(int i = 0; i < NB_ROWS; i++){
                makeRowVisible(i);
            }
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }

        for(int i = 0; i <= NB_ROWS; i++){
            if(arrayHeight < rowHeight * i){
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                makeRowInvisible(i - 1);
                return;
            }else{
                makeRowVisible(i - 1);
            }

        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}