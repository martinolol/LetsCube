package letsapps.com.letscube.view.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import letsapps.com.letscube.activity.TimerActivity;
import letsapps.com.letscube.singleton.AppInstance;
import letsapps.com.letscube.singleton.TimerSettings;
import letsapps.com.letscube.util.DatabaseTime;
import letsapps.com.letscube.util.ListUtil;
import letsapps.com.letscube.util.TimeList;
import letsapps.com.letscube.util.TimerUtils;

public class ChartHistogramView extends ChartView {

    public static final float LINE_WIDTH = 2f;

    protected Paint rectBorderPaint;
    protected Paint rectInsidePaint;
    protected Paint textPaint;

    int[][] timesSeparated;
    final int INTERVAL_MIN = 0;
    final int INTERVAL_MAX = 1;
    final int NB_TIMES = 2;

    int nbInterval;
    int maxIntervalValue;

    public ChartHistogramView(Context context) {
        super(context);
        init();
    }

    public ChartHistogramView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChartHistogramView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){

        rectBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rectBorderPaint.setStrokeWidth(LINE_WIDTH);
        rectBorderPaint.setStyle(Paint.Style.STROKE);
        rectBorderPaint.setColor(Color.BLACK);

        rectInsidePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //if(TimerSettings.getInstance().getTheme() != null) {
            rectInsidePaint.setColor(Color.parseColor("#FFFFAA"));
        //}

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.BLACK);

        timesSeparated = new int[0][3];
    }

    @Override
    protected void onDraw(Canvas c){
        super.onDraw(c);

        if(nbInterval >= 2) {

            final int width = c.getWidth();
            final int height = c.getHeight() - (2 * (int)AXES_WIDTH);

            final float lineHeight = (float)height / (float)nbInterval;
            //Log.d("CHV (upData)", "width : " + width);
            //Log.d("CHV (upData)", "height : " + height);
            //Log.d("CHV (upData)", "unityToPx : " + unityToPx);

            float maxIntervalTextWidth = 0;
            textPaint.setTextSize(lineHeight - (2 * LINE_WIDTH));

            //Log.d("CHV (draw)", "---------------------------------");


            //récupère la taille du texte des intervals
            for(int i = 0; i < nbInterval; i++){

                //Log.d("CHV (draw)",
                //        "interval : " + TimerUtils.formatTimeAuthorizing0(timesSeparated[i][INTERVAL_MIN]) + "+ ||" +
                //        "textSize : " + textPaint.measureText(
                //                TimerUtils.formatTimeAuthorizing0(timesSeparated[i][INTERVAL_MIN]) + "000"));

                if(textPaint.measureText(
                        TimerUtils.formatTime(timesSeparated[i][INTERVAL_MIN]) + "000")
                        > maxIntervalTextWidth){
                    //Log.d("CHV (draw)", "text size record!");
                    maxIntervalTextWidth = textPaint.measureText(
                            TimerUtils.formatTime(
                                    timesSeparated[i][INTERVAL_MIN]) + "000");
                                    // 000 car ne prend pas le + en compte?
                }
            }

            final float unityToPx =
                    ((width - maxIntervalTextWidth) - (AXES_WIDTH * 2)) / (float)maxIntervalValue;

            for(int i = 0; i < nbInterval; i++){
                c.drawRect(
                        AXES_WIDTH,
                        AXES_WIDTH + (lineHeight * i),
                        AXES_WIDTH + maxIntervalTextWidth + (timesSeparated[i][NB_TIMES] * unityToPx),
                        AXES_WIDTH + (lineHeight * (i + 1)),
                        rectInsidePaint);
                c.drawRect(
                        AXES_WIDTH,
                        AXES_WIDTH + (lineHeight * i),
                        AXES_WIDTH + maxIntervalTextWidth + (timesSeparated[i][NB_TIMES] * unityToPx),
                        AXES_WIDTH + (lineHeight * (i + 1)),
                        rectBorderPaint);

                //dessine le texte à droite de la barre si elle est trop petite.
                if(timesSeparated[i][NB_TIMES] < (maxIntervalValue / 2)){
                    c.drawText(
                            String.valueOf(timesSeparated[i][NB_TIMES]),
                            AXES_WIDTH + maxIntervalTextWidth + (timesSeparated[i][NB_TIMES] * unityToPx) + (LINE_WIDTH * 2),
                            (lineHeight * (i + 1)),
                            textPaint);
                }else{ //sinon dessine le texte dans la barre.
                    c.drawText(
                            String.valueOf(timesSeparated[i][NB_TIMES]),
                            AXES_WIDTH + maxIntervalTextWidth + (timesSeparated[i][NB_TIMES] * unityToPx) -
                                    textPaint.measureText(
                                            String.valueOf(timesSeparated[i][NB_TIMES])) -
                                    (LINE_WIDTH * 2),
                            (lineHeight * (i + 1)),
                            textPaint);
                }
            }

            //pour tous les intervals
            for(int i = 0; i < nbInterval; i++){
                //dessine le texte à gauche du graphique.
                c.drawText(
                        TimerUtils.formatTime(
                                timesSeparated[i][INTERVAL_MIN],
                                true, 0,
                                TimerSettings.TIMER_UPDATE_HUNDREDTH) + "+",
                        AXES_WIDTH * 2,
                        (lineHeight * (i + 1)),
                        textPaint);
            }
        }
    }

    @Override
    public void refreshData() {
        super.refreshData();
        int intervalGap;
        if(datasGap == 0){
            nbInterval = 0;
            invalidate();
            return;
        }else if(datasGap < 1000){
            intervalGap = 100;
        }else if(datasGap < 2000){
            intervalGap = 200;
        }else if(datasGap < 5000){
            intervalGap = 500;
        }else if(datasGap < 10000){
            intervalGap = 1000;
        }else if(datasGap < 20000){
            intervalGap = 2000;
        }else if(datasGap < 50000){
            intervalGap = 5000;
        }else if(datasGap < 100_000){
            intervalGap = 10_000;
        }else if(datasGap < 300_000){
            intervalGap = 30_000;
        }else if(datasGap < 600_000){
            intervalGap = 60_000;
        }else{
            intervalGap = 120_000;
        }

        int minMS = minData - (minData % intervalGap);
        int maxMS = maxData + (intervalGap - (maxData % intervalGap));

        minMS -= (minMS % intervalGap);
        maxMS -= (minMS % intervalGap);


        int current = minMS;
        for(int i = 0; i <= 10; i++){
            current += intervalGap;
            if(current >= maxMS){
                nbInterval = i + 1;
                break;
            }
        }

        timesSeparated = new int[nbInterval][3];
        maxIntervalValue = 0;
        //Log.d("CHV (upData)", "minMS : " + minMS);
        //Log.d("CHV (upData)", "maxMS : " + maxMS);
        //Log.d("CHV (upData)", "nbInterval : " + nbInterval);
        for(int i = 0; i < nbInterval; i++){
            timesSeparated[i][INTERVAL_MIN] = minMS + (i * intervalGap);
            timesSeparated[i][INTERVAL_MAX] = minMS + ((i + 1) * intervalGap);
            timesSeparated[i][NB_TIMES] = ListUtil.getNbTimeInInterval(
                    appInstance.getTimes(),
                    timesSeparated[i][INTERVAL_MIN], timesSeparated[i][INTERVAL_MAX]);
            if(timesSeparated[i][NB_TIMES] > maxIntervalValue){
                maxIntervalValue = timesSeparated[i][NB_TIMES];
            }
            /*Log.d("CHV (upData)", "interval(" +
                    timesSeparated[i][INTERVAL_MIN] + ";" +
                    timesSeparated[i][INTERVAL_MAX] + ") -> nbTimes : " +
                    timesSeparated[i][NB_TIMES]);*/
        }
        //Log.d("CHV (upData)", "maxIntervalValue : " + maxIntervalValue);

        invalidate();
    }
}
