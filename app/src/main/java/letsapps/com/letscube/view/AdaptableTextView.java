package letsapps.com.letscube.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;

import letsapps.com.letscube.R;
import letsapps.com.letscube.util.scramble.ScrambleGenerator;

/**
 * Created by marti on 27/08/2016.
 */
public class AdaptableTextView extends TextView {

    String text;

    public AdaptableTextView(Context context) {
        super(context);
        init();
    }

    public AdaptableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AdaptableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){

        Log.d("AdaptableTV", "init()");
    }

    public void setAdaptableText(String text, int scrambleType){
        this.text = text;
        Log.d("AdaptableTV", "start setAdaptableText(" + text + ")");

        super.setText(text);

        switch(scrambleType){
            case ScrambleGenerator.SCRAMBLE_TYPE_2x2:
            case ScrambleGenerator.SCRAMBLE_TYPE_3x3:
            case ScrambleGenerator.SCRAMBLE_TYPE_4x4:
            case ScrambleGenerator.SCRAMBLE_TYPE_PYRAMINX:
            case ScrambleGenerator.SCRAMBLE_TYPE_SKEWB:
            case ScrambleGenerator.SCRAMBLE_TYPE_CLOCK:
            case ScrambleGenerator.SCRAMBLE_TYPE_3x3x2:
                super.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.scramble_text_size_big));
                break;
            case ScrambleGenerator.SCRAMBLE_TYPE_5x5:
                super.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.scramble_text_size_medium));
                break;
            case ScrambleGenerator.SCRAMBLE_TYPE_6x6:
                super.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.scramble_text_size_small));
                break;
            case ScrambleGenerator.SCRAMBLE_TYPE_7x7:
            case ScrambleGenerator.SCRAMBLE_TYPE_MEGAMINX:
                super.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.scramble_text_size_xsmall));
                break;

        }

        /*

        final int viewWidth = super.getWidth();
        final int viewHeight = super.getHeight();

        if(viewWidth == 0){
            return;
        }

        for(int i = 20; i > 10; i -= 2){
            super.setTextSize(TypedValue.COMPLEX_UNIT_DIP, i);

            super.measure(0, 0);

            final int width = super.getMeasuredWidth();

            final int lineHeight = super.getLineHeight();
            final int nbLines = (width / viewWidth) + 1;
            final int maxNbLines = viewHeight / lineHeight;

            if(nbLines <= maxNbLines){
                break;
            }

        }

        */
        //Log.d("AdaptableTV", "end setAdaptableText(" + text + ")");

        /*Log.i("AdaptableTV", "text width : " + width + " | height : " + height);
        Log.i("AdaptableTV", "view width : " + getWidth() + " | height : " + getHeight());
        Log.i("AdaptableTV", "maxNbLines : " + maxNbLines + " | nbLines : " + nbLines);
        Log.i("AdaptableTV", "text line height : " + lineHeight);*/
    }

    /*private int getLineHeight(){
        final String textTemp = super.getText().toString();

        super.setAdaptableText("X");
        super.measure(0, 0);
        final int baseHeight = super.getMeasuredHeight();
        super.setAdaptableText("X\nX");
        super.measure(0, 0);
        final int secondHeight = super.getMeasuredHeight();

        super.setAdaptableText(textTemp);

        return secondHeight - baseHeight;
    }
*/

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //setAdaptableText(text);
        //Log.d("AdaptableTV", "onMeasure()");
    }
}