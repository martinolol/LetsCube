package letsapps.com.letscube.view.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;

import letsapps.com.letscube.singleton.AppInstance;
import letsapps.com.letscube.singleton.TimerSettings;
import letsapps.com.letscube.util.AverageCalculator;
import letsapps.com.letscube.util.DatabaseTime;
import letsapps.com.letscube.util.TimerUtils;

public class ChartLineView extends ChartView {

    public static final float LINE_WIDTH = 2f;

    protected Paint linePaint;
    protected Paint line2Paint;
    protected Paint lineDNFPaint;
    protected Paint lineAveragePaint;
    protected Paint lineTrendPaint;

    public boolean isOnboardingExemple = false;

    public ChartLineView(Context context) {
        super(context);
        init();
    }

    public ChartLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChartLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStrokeWidth(LINE_WIDTH);
        linePaint.setColor(Color.BLUE);

        line2Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        line2Paint.setStrokeWidth(LINE_WIDTH);
        line2Paint.setColor(Color.parseColor("#0ACC10"));

        lineDNFPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        lineDNFPaint.setStrokeWidth(LINE_WIDTH);
        lineDNFPaint.setColor(Color.RED);
/*
        lineAveragePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        lineAveragePaint.setStrokeWidth(LINE_WIDTH);
        lineAveragePaint.setColor(Color.RED);

        lineTrendPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        lineTrendPaint.setStrokeWidth(LINE_WIDTH);
        lineTrendPaint.setColor(Color.parseColor("#00AA00"));
        */
    }

    public void setIsOnboardingExemple(boolean isOnboardingExemple){
        this.isOnboardingExemple = isOnboardingExemple;
    }

    @Override
    protected void onDraw(Canvas c){
        super.onDraw(c);

        final int cWidth = c.getWidth();
        final int cHeight = c.getHeight();
        final float insideAxesWidth = cWidth - (2 * axesOffset);
        final float insideAxesHeight = cHeight - (2 * axesOffset);

        //draw axes
        c.drawLine(axesOffset, cHeight - axesOffset,
                cWidth - axesOffset, cHeight - axesOffset, axesPaint); // axe x
        c.drawLine(axesOffset, axesOffset,
                axesOffset, cHeight - axesOffset, axesPaint); // axe y

        if(isOnboardingExemple){
            int[] fakeTimes = {9875, 14187, 11321, 12318, 8547, 10234, 11255, 9746, 10521, 9879};

            for(int i = 0; i < 2; i++){
                final float yPos = (cHeight - axesOffset) - ((insideAxesHeight / 2) * (i + 1));
                c.drawLine(axesOffset, yPos, cWidth - axesOffset,  yPos, graduationPaint); // axe x
            }

            final float spaceBetweenPoints = insideAxesWidth / (fakeTimes.length - 1);
            for (int i = 0; i < 9; i++) {
                c.drawLine(axesOffset + (i * spaceBetweenPoints),
                        axesOffset + (insideAxesHeight * (10000 - (fakeTimes[i] - 5000)) / 10000),
                        axesOffset + ((i + 1) * spaceBetweenPoints),
                        axesOffset + (insideAxesHeight * (10000 - (fakeTimes[i+1] - 5000)) / 10000),
                        linePaint);
            }

            for (int i = 1; i < (NB_UNITY_X_AXIS + 1); i++){
                c.drawText(String.valueOf((10 * i) / NB_UNITY_X_AXIS),
                        (axesOffset / 2) + ((insideAxesWidth * (0.95f * i)) / NB_UNITY_X_AXIS),
                        cHeight - AXES_WIDTH - 2, textPaint);
            }
            //separator
            c.drawLine((axesOffset / 2) + ((insideAxesWidth * 0.95f) / NB_UNITY_X_AXIS) - 4,
                    cHeight - axesOffset,
                    (axesOffset / 2) + ((insideAxesWidth * 0.95f) / NB_UNITY_X_AXIS) - 4,
                    cHeight, axesPaint);
            //draw y axis data (values)
            c.drawText(TimerUtils.formatTime(15000, true, 0, TimerSettings.TIMER_UPDATE_HUNDREDTH),
                    AXES_WIDTH + 2, textSize, textPaint);
            c.drawText(TimerUtils.formatTime(5000, true, 0, TimerSettings.TIMER_UPDATE_HUNDREDTH),
                    AXES_WIDTH + 2, cHeight - (AXES_WIDTH + 2), textPaint);

            return;
        }

        //draw horizontal lines
        final int nbLines = (highestRoundTo5Data - lowestRoundTo5Data) / 5000;
        //Log.d("ChartView", "highestData : " + highestRoundTo5Data);
        //Log.d("ChartView", "lowestData : " + lowestRoundTo5Data);
        //Log.d("ChartView", "(highestData - lowestData) / 5000 : " + (highestRoundTo5Data - lowestRoundTo5Data) / 5000);
        //Log.d("ChartView", "nbLines : " + nbLines);
        for(int i = 0; i < nbLines; i++){
            final float yPos = (cHeight - axesOffset) - ((insideAxesHeight / nbLines) * (i + 1));
            c.drawLine(axesOffset, yPos, cWidth - axesOffset,  yPos, graduationPaint); // axe x
        }

        if(appInstance.getTimes() != null && nbDatas != 0) {
            final float spaceBetweenPoints = insideAxesWidth / (nbDatas - 1);
            //int beforeAverage = 0;
            //int beforePlusNowAverage = AverageCalculator.calculateFirstMean(
            //        appInstance.getTimes(), 1);

            // draw all lines from i to i+1
            for (int i = 0; i < nbDatas - 1; i++) {
                Paint paintToUse;
                final DatabaseTime currentTime = appInstance.getTimes().get(i + 1);
                if (i < nbDatas - 1){
                    switch (currentTime.getPenalty()){
                        case DatabaseTime.PENALTY_2 : paintToUse = line2Paint; break;
                        case DatabaseTime.PENALTY_DNF : paintToUse = lineDNFPaint; break;
                        default : paintToUse = linePaint; break;
                    }
                }else{
                    paintToUse = linePaint;
                }
                c.drawLine(axesOffset + (i * spaceBetweenPoints),
                        axesOffset + (insideAxesHeight * (datasRoundedTo5Gap -
                                (appInstance.getTimes().get(i)
                                        .getTimeAccordingToPenaltyWithoutDNF() -
                                        lowestRoundTo5Data))
                                / datasRoundedTo5Gap),
                        axesOffset + ((i + 1) * spaceBetweenPoints),
                        axesOffset + (insideAxesHeight * (datasRoundedTo5Gap -
                                (currentTime.getTimeAccordingToPenaltyWithoutDNF() - lowestRoundTo5Data))
                                / datasRoundedTo5Gap),
                        paintToUse);
/*
                beforeAverage = beforePlusNowAverage;
                beforePlusNowAverage = AverageCalculator.calculateFirstMean(datas, i + 2);

                c.drawLine(axesOffset + (i * spaceBetweenPoints),
                        axesOffset + (insideAxesHeight * (datasRoundedTo5Gap -
                                (beforeAverage - lowestRoundTo5Data))
                                / datasRoundedTo5Gap),
                        axesOffset + ((i + 1) * spaceBetweenPoints),
                        axesOffset + (insideAxesHeight * (datasRoundedTo5Gap -
                                (beforePlusNowAverage - lowestRoundTo5Data))
                                / datasRoundedTo5Gap),
                        lineAveragePaint);
*/
            }
/*
            final int firstHalfAverage = AverageCalculator.calculateFirstMean(datas, nbDatas / 2);
            final int lastHalfAverage = AverageCalculator.calculateLastMean(datas, nbDatas / 2);

            int sumTotal = 0;
            int sumX = 0;
            int sumXSquared = 0;
            int cc;
            int dd;
            int sumY = 0;
            int sumXY = 0;

            for(int i = 0; i < nbDatas; i++){
                sumTotal += (axesOffset + (i * spaceBetweenPoints)) *
                        (axesOffset + (insideAxesHeight * (datasRoundedTo5Gap -
                        (beforeAverage - lowestRoundTo5Data))
                        / datasRoundedTo5Gap));

                sumX += (axesOffset + (i * spaceBetweenPoints));
                sumXSquared += Math.pow((axesOffset + (i * spaceBetweenPoints)), 2);
                sumY += (axesOffset + (insideAxesHeight * (datasRoundedTo5Gap -
                        (beforeAverage - lowestRoundTo5Data))
                        / datasRoundedTo5Gap));

            }

            sumXY = sumX * sumY;
            cc = nbDatas * sumXSquared;
            dd = (int)Math.pow(sumX, 2);

            int slope = (sumTotal - sumXY) / (cc - dd);

            Log.d("CLV (onDraw)", "slope = " + slope);

            c.drawLine(axesOffset,
                    axesOffset + (insideAxesHeight * (datasRoundedTo5Gap -
                            (firstHalfAverage - lowestRoundTo5Data))
                            / datasRoundedTo5Gap),
                    cWidth - axesOffset,
                    axesOffset + (insideAxesHeight * (datasRoundedTo5Gap -
                            (lastHalfAverage - lowestRoundTo5Data))
                            / datasRoundedTo5Gap),
                    lineTrendPaint);
*/
            if(appInstance.getTimes() != null && nbDatas != 0) {
                //draw x axis data (unities)
                for (int i = 1; i < (NB_UNITY_X_AXIS + 1); i++){
                    c.drawText(String.valueOf((nbDatas * i) / NB_UNITY_X_AXIS), (axesOffset / 2) + ((insideAxesWidth * (0.95f * i)) / NB_UNITY_X_AXIS), cHeight - AXES_WIDTH - 2, textPaint);
                }
                //separator
                c.drawLine((axesOffset / 2) + ((insideAxesWidth * 0.95f) / NB_UNITY_X_AXIS) - 4, cHeight - axesOffset, (axesOffset / 2) + ((insideAxesWidth * 0.95f) / NB_UNITY_X_AXIS) - 4, cHeight, axesPaint);
                //draw y axis data (values)
                c.drawText(TimerUtils.formatTime(
                        highestRoundTo5Data, true, 0, TimerSettings.TIMER_UPDATE_HUNDREDTH),
                        AXES_WIDTH + 2, textSize, textPaint);
                c.drawText(TimerUtils.formatTime(
                        lowestRoundTo5Data, true, 0, TimerSettings.TIMER_UPDATE_HUNDREDTH),
                        AXES_WIDTH + 2, cHeight - (AXES_WIDTH + 2), textPaint);

            }

        }

    }
}
