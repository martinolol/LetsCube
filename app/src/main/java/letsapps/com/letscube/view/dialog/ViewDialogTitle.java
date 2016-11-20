package letsapps.com.letscube.view.dialog;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import letsapps.com.letscube.R;
import letsapps.com.letscube.activity.TimerActivity;
import letsapps.com.letscube.singleton.TimerSettings;

public class ViewDialogTitle extends LinearLayout{

    TextView titleView;
    String title;

    public ViewDialogTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ViewDialogTitle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs){
        Log.d("VDialogTitle", "init(attrs) start");


        TypedArray attributes = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.ViewDialogTitle, 0, 0);

        try {
            title = getContext().getString(attributes.getResourceId(R.styleable.ViewDialogTitle_dialog_title, R.string.app_name));
        } finally {
            attributes.recycle();
        }

        init();
    }

    private void init(){
        final LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rootView = inflater.inflate(R.layout.dialog_view_title, this);

        titleView = (TextView)rootView.findViewById(R.id.dialog_title);

        if(TimerSettings.getInstance().getTheme() != null) {
            super.setBackgroundColor(TimerSettings.getInstance().getTheme().getMainColor());
        }

        if(title != null){
            titleView.setText(title);
        }

    }

    public void setTitle(String title){
        this.title = title;
        titleView.setText(title);
    }

    public void setTitle(int titleResId){
        this.title = getContext().getString(titleResId);
        titleView.setText(title);
    }

    public void setBackgroundColor(int color){
        super.setBackgroundColor(color);
    }
}
