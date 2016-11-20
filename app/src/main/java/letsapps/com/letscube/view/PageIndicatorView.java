package letsapps.com.letscube.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import letsapps.com.letscube.R;

/**
 * Created by marti on 22/08/2016.
 */
public class PageIndicatorView extends LinearLayout {

    int nbPages = 0;
    int currentPage = 0;
    View[] indicators = null;

    public PageIndicatorView(Context context) {
        super(context);
        init();
    }

    public PageIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PageIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        //LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //View rootView = inflater.inflate(R.layout.view_page_indicator);
        super.setOrientation(HORIZONTAL);
    }

    public void setNbPages(int nbPages){
        this.nbPages = nbPages;
        indicators = new View[nbPages];

        final float dp = (int)TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());

        for(int i = 0; i < nbPages; i++){
            indicators[i] = new View(getContext());
            final LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams((int)(8*dp),(int)(8*dp));
            params.setMargins((int)(4*dp), 0, (int)(4*dp), 0);
            indicators[i].setLayoutParams(params);
            indicators[i].setBackgroundResource(R.drawable.indicator_unselected);
            super.addView(indicators[i]);
        }
        invalidate();
    }

    public void setCurrentPage(int currentPage){
        if(currentPage < 0){
            Log.w("PIV", "currentPage < 0 : " + currentPage);
            currentPage = 0;
        }
        if(currentPage >= nbPages){
            Log.w("PIV", "currentPage >= nbPages : " + currentPage);
            currentPage = nbPages - 1;
        }
        this.currentPage = currentPage;
        for(int i = 0; i < nbPages; i++){
            if(i == currentPage){
                indicators[i].setBackgroundResource(R.drawable.indicator_selected);
            }else{
                indicators[i].setBackgroundResource(R.drawable.indicator_unselected);
            }
        }
    }

    public void setNextPage(){
        currentPage++;
        setCurrentPage(currentPage);
    }

    public int getCurrentPage(){
        return currentPage;
    }
}
