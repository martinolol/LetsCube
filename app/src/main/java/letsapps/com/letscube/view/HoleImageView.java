package letsapps.com.letscube.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import letsapps.com.letscube.singleton.TimerSettings;

/**
 * Created by marti on 22/08/2016.
 */

// tuto : https://medium.com/@rgomez/android-how-to-draw-an-overlay-with-a-transparent-hole-471af6cf3953#.xtto07tv6
public class HoleImageView extends ImageView {

    private Canvas temp;
    private Paint outterPaint, borderPaint, innerPaint = null;
    private int outterColor;

    private float borderWidth;

    public HoleImageView(Context context) {
        super(context);
        init();
    }

    public HoleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HoleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        outterColor = TimerSettings.THEMES[0].getMainColor();

        outterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        outterPaint.setColor(outterColor);

        borderWidth = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());

        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(borderWidth);
        borderPaint.setColor(Color.parseColor("#88FFFFFF"));

        innerPaint = new Paint();
        innerPaint.setColor(getResources().getColor(android.R.color.transparent));
        innerPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final Bitmap bitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
        temp = new Canvas(bitmap);

        final int maxSize = Math.max(canvas.getWidth(), canvas.getHeight());
        final int minSize = Math.min(canvas.getWidth(), canvas.getHeight());

        final RectF circleRect = new RectF(
                (maxSize / 2) - (minSize / 2) + (borderWidth / 2),
                (borderWidth / 2),
                (maxSize / 2) + (minSize / 2) - (borderWidth / 2),
                minSize - (borderWidth / 2)
        );

        temp.drawRect(0, 0, temp.getWidth(), temp.getHeight(), outterPaint);
        temp.drawOval(circleRect, innerPaint);
        temp.drawOval(circleRect, borderPaint);
        canvas.drawBitmap(bitmap, 0, 0, new Paint());
    }

    public void setOutterColor(int outterColor){
        this.outterColor = outterColor;
        outterPaint.setColor(outterColor);
        invalidate();
    }
}
