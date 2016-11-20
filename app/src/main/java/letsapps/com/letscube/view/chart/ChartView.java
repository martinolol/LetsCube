package letsapps.com.letscube.view.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import letsapps.com.letscube.singleton.AppInstance;
import letsapps.com.letscube.util.DatabaseTime;
import letsapps.com.letscube.util.ListUtil;
import letsapps.com.letscube.util.TimeList;
import letsapps.com.letscube.util.TimerUtils;

public class ChartView extends View {

    public static final float AXES_WIDTH = 4f;
    public float axesOffset = 20f;

    public static final int NB_UNITY_X_AXIS = 2;

    protected int nbDatas;
    protected int lowestRoundTo5Data;
    protected int highestRoundTo5Data;
    protected int minData;
    protected int maxData;
    protected int datasRoundedTo5Gap;
    protected int datasGap;

    protected Paint axesPaint;
    protected Paint graduationPaint;
    protected Paint backgroundPaint;
    protected Paint textPaint;

    protected int textSize;

    protected AppInstance appInstance;

    public ChartView(Context context) {
        super(context);
        init();
    }

    public ChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        axesPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        axesPaint.setStrokeWidth(AXES_WIDTH);
        axesPaint.setStyle(Paint.Style.STROKE);
        axesPaint.setColor(Color.BLACK);

        graduationPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        graduationPaint.setStrokeWidth(1f);
        graduationPaint.setStyle(Paint.Style.STROKE);
        graduationPaint.setColor(Color.BLACK);

        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setColor(Color.WHITE);

        textSize = (int) axesOffset - (int)AXES_WIDTH;

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(textSize);
        textPaint.setColor(Color.BLACK);
    }

    @Override
    protected void onDraw(Canvas c){
        final int cWidth = c.getWidth();
        final int cHeight = c.getHeight();
        final float insideAxesWidth = cWidth - (2 * axesOffset);
        final float insideAxesHeight = cHeight - (2 * axesOffset);

        //draw background and frame
        c.drawRect(AXES_WIDTH / 2, AXES_WIDTH / 2, cWidth - (AXES_WIDTH / 2),
                cHeight - (AXES_WIDTH / 2), backgroundPaint);
        c.drawRect(AXES_WIDTH / 2, AXES_WIDTH / 2, cWidth - (AXES_WIDTH / 2),
                cHeight - (AXES_WIDTH / 2), axesPaint);


    }

    public void refreshData() {
        appInstance = AppInstance.getInstance();
        if(appInstance.getTimes() != null) {
            nbDatas = appInstance.getTimes().size();
            if(nbDatas != 0) {
                minData = ListUtil.getMinValue(appInstance.getTimes());
                lowestRoundTo5Data = TimerUtils.roundTo5secBelow(minData);
                //Log.d("ChartView", "lowestRoundTo5Data = " + lowestRoundTo5Data);
                maxData = ListUtil.getMaxValue(appInstance.getTimes());
                highestRoundTo5Data = TimerUtils.roundTo5secAbove(maxData);
                //Log.d("ChartView", "highestRoundTo5Data = " + highestRoundTo5Data);
                datasRoundedTo5Gap = highestRoundTo5Data - lowestRoundTo5Data;
                datasGap = maxData - minData;
            }
        }
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        final int width = MeasureSpec.getSize(widthMeasureSpec);
        final int availableHeight = MeasureSpec.getSize(heightMeasureSpec);
        final int calculatedHeight = (2 * width) / 3;

        if(calculatedHeight > availableHeight){
            final int calculatedWidth = (3 * availableHeight) / 2;
            super.setMeasuredDimension(calculatedWidth, availableHeight);
            axesOffset = calculatedWidth / 20;
        }else{
            super.setMeasuredDimension(width, calculatedHeight);
            axesOffset = width / 20;
        }

        textSize = (int) axesOffset - (int)AXES_WIDTH;
        textPaint.setTextSize(textSize);
    }

    public int getNbDatas(){
        return nbDatas;
    }
}
