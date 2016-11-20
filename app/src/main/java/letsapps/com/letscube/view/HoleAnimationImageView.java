package letsapps.com.letscube.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.ImageView;

import letsapps.com.letscube.singleton.TimerSettings;
import letsapps.com.letscube.util.Circle;

/**
 * Created by marti on 26/08/2016.
 */
public class HoleAnimationImageView extends ImageView {

    private static final int ANIMATION_DURATION_MS = 100;
    private static final int NB_DP_MARGIN = 16;
    private static final int MIN_RADIUS_DP = 96;

    private Canvas temp;
    private Paint outterPaint, borderPaint, innerPaint = null;

    private float borderWidth;

    SizeChangeAnimation sizeChangeAnimation;

    Circle holeBounds;
    Circle fillCircle;

    int viewWidth, viewHeight;
    float marginSizePx;
    float minRadius;

    boolean isOpen;
    float theoricHoleRadius = 0;

    public HoleAnimationImageView(Context context) {
        super(context);
        init();
    }

    public HoleAnimationImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HoleAnimationImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){

        outterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        outterPaint.setColor(TimerSettings.getInstance().getTheme().getMainColor());

        borderWidth = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());

        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(borderWidth);
        borderPaint.setColor(Color.BLACK);

        innerPaint = new Paint();
        innerPaint.setColor(getResources().getColor(android.R.color.transparent));
        innerPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        holeBounds = new Circle();
        isOpen = true;

        marginSizePx = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, NB_DP_MARGIN, getResources().getDisplayMetrics());
        minRadius = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, MIN_RADIUS_DP, getResources().getDisplayMetrics());

        sizeChangeAnimation = new SizeChangeAnimation();
    }

    public void setBorderColor(int color){
        if(borderPaint != null){
            borderPaint.setColor(color);
            invalidate();
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final Bitmap bitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
        temp = new Canvas(bitmap);

        final int cWidth = canvas.getWidth();
        final int cHeight = canvas.getHeight();

        holeBounds.x = cWidth / 2;
        holeBounds.y = cHeight / 2;
        if(holeBounds.radius <= marginSizePx) {
            holeBounds.radius = Math.max(cWidth, cHeight);
        }
        //Log.d("HAIV", "onDraw() => holeBounds : " + holeBounds.toString());
        //Log.d("HAIV", "onDraw() => fillCircle : " + fillCircle.toString());
        temp.drawRect(0, 0, temp.getWidth(), temp.getHeight(), outterPaint);
        temp.drawCircle(holeBounds.x, holeBounds.y, holeBounds.radius, innerPaint);
        temp.drawCircle(holeBounds.x, holeBounds.y, holeBounds.radius, borderPaint);
        canvas.drawBitmap(bitmap, 0, 0, new Paint());
    }

    public void open(boolean withAnimation){
        setHoleRadius(withAnimation, Math.max(viewWidth, viewHeight));
        isOpen = true;
    }

    public void close(boolean withAnimation, float radius){
        isOpen = false;
        setHoleRadius(withAnimation, radius);
    }

    // change hole size only when radius is changing
    public void setHoleRadius(boolean withAnimation, float radius){

        if(isOpen){
            return;
        }

        // adding a default margin
        radius += marginSizePx;

        radius = Math.max(radius, minRadius);

        if(theoricHoleRadius == radius){
            return;
        }

        theoricHoleRadius = radius;

        Log.d("HAIV", "setHoleRadius : " + radius);

        if(!withAnimation) {
            holeBounds.radius = radius;
            invalidate();
        }else{
            sizeChangeAnimation.start(new Circle(viewWidth / 2, viewHeight / 2, radius));
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(viewWidth, viewHeight);
        fillCircle = new Circle(
                viewWidth / 2,
                viewHeight / 2,
                Math.max(viewWidth, viewHeight));
    }

    private class SizeChangeAnimation extends Animation {

        Circle startCircle;
        Circle destCircle;

        public SizeChangeAnimation() {
            super();

            setDuration(ANIMATION_DURATION_MS);
            setStartOffset(0);

        }

        public void start(Circle destCircle){
            startCircle = new Circle(holeBounds);
            this.destCircle = destCircle;

            if(destCircle.radius > startCircle.radius){
                setInterpolator(new AccelerateInterpolator());
            }else{
                setInterpolator(new DecelerateInterpolator());
            }

            HoleAnimationImageView.this.startAnimation(this);

        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);

            holeBounds.x = interpolatedTime * destCircle.x + (1 - interpolatedTime) * startCircle.x;
            holeBounds.y = interpolatedTime * destCircle.y + (1 - interpolatedTime) * startCircle.y;
            holeBounds.radius = interpolatedTime * destCircle.radius +
                    (1 - interpolatedTime) * startCircle.radius;

            invalidate();

        }

    }

}
