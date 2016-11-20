package letsapps.com.letscube.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import letsapps.com.letscube.singleton.AppInstance;
import letsapps.com.letscube.singleton.TimerSettings;
import letsapps.com.letscube.util.Theme;

public class SpectreView extends View{

    private SpectreListener spectreListener;

    private Paint colorPaint;
    private Paint selectorPaint;
    private final int nbColors = TimerSettings.THEMES.length;

    Theme selectedTheme;
    int canvasWidth = 0;
    float selectorWidth = 4.0f;

    public SpectreView(Context context) {
        super(context);
        init();
    }

    public SpectreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SpectreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        colorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        selectorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        selectorPaint.setStyle(Paint.Style.STROKE);
        selectorPaint.setStrokeWidth(selectorWidth);
        selectorPaint.setColor(Color.BLACK);

        selectedTheme = TimerSettings.getInstance().getTheme();
        if(selectedTheme == null){
            selectedTheme = TimerSettings.THEMES[0];
        }
    }

    protected void onDraw(Canvas canvas){
        canvasWidth = canvas.getWidth();
        final int height = canvas.getHeight();
        final int colorWidth = canvasWidth / nbColors;

        for(int i = 0; i < nbColors; i++){
            // Dessine les couleurs une par une
            colorPaint.setColor(TimerSettings.THEMES[i].getMainColor());
            canvas.drawRect(
                    i * colorWidth,
                    0,
                    (i + 1) * colorWidth,
                    height,
                    colorPaint);

            if(super.isClickable()){
                // Dessine le contour de la couleur selectionné
                if(selectedTheme.getId() == TimerSettings.THEMES[i].getId()){
                    canvas.drawRect(
                            i * colorWidth + (selectorWidth / 2),
                            selectorWidth / 2,
                            (i + 1) * colorWidth - (selectorWidth / 2),
                            height - (selectorWidth / 2),
                            selectorPaint);
                }
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        final int width = MeasureSpec.getSize(widthMeasureSpec);
        // La hauteur de chaque rectangle est 2x sa largeur.
        super.setMeasuredDimension(width, width / (nbColors / 2));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        if(!super.isClickable()){
            return false;
        }

        final int action = event.getAction();

        if(event.getX() >= 0 && event.getX() < canvasWidth) {

            if(action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE){
                selectedTheme =
                        TimerSettings.THEMES[(int)((event.getX() * nbColors) / canvasWidth)];
                if(spectreListener != null){
                    spectreListener.OnSelectedThemeChange(selectedTheme);
                }
                invalidate();
                return true;
            }

            if(action == MotionEvent.ACTION_UP){
                selectedTheme =
                        TimerSettings.THEMES[(int)((event.getX() * nbColors) / canvasWidth)];
                if(spectreListener != null){
                    spectreListener.OnSelectedThemeChange(selectedTheme);
                }
                invalidate();
                return false;
            }
        }
        return true;
    }

    public Theme getSelectedTheme(){
        return selectedTheme;
    }

    public void setCurrentTheme(Theme currentTheme){
        this.selectedTheme = currentTheme;
        invalidate();
    }

    public void setSpectreListener(SpectreListener spectreListener){
        this.spectreListener = spectreListener;
    }

    public interface SpectreListener{
        void OnSelectedThemeChange(Theme selectedTheme);
    }
}
