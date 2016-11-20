package letsapps.com.letscube.view.actionBar;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import letsapps.com.letscube.R;
import letsapps.com.letscube.activity.TimerActivity;
import letsapps.com.letscube.singleton.TimerSettings;

public class ActionBarButton extends RelativeLayout {

    private ImageView picture;
    private View leftV, topV, rightV, bottomV;

    public ActionBarButton(Context context) {
        super(context);
        init();
    }

    public ActionBarButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ActionBarButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs){
        init();
        TypedArray attributes = getContext().getTheme()
                .obtainStyledAttributes(attrs, R.styleable.ActionBarButton, 0, 0);

        try {
            picture.setImageResource(attributes.getResourceId(
                    R.styleable.ActionBarButton_src, R.color.transparent));
        } finally {
            attributes.recycle();
        }

    }

    private void init() {
        final LayoutInflater inflater =
                (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rootView = inflater.inflate(R.layout.view_button_action_bar, this);

        picture = (ImageView) rootView.findViewById(R.id.picture);
        leftV = rootView.findViewById(R.id.left);
        topV = rootView.findViewById(R.id.top);
        rightV = rootView.findViewById(R.id.right);
        bottomV = rootView.findViewById(R.id.bottom);
    }

    public void setSelected(boolean isSelected){
        if(isSelected){
            leftV.setVisibility(View.VISIBLE);
            topV.setVisibility(View.VISIBLE);
            rightV.setVisibility(View.VISIBLE);
            bottomV.setVisibility(View.INVISIBLE);
            picture.setBackgroundResource(R.color.app_background);
        }else{
            leftV.setVisibility(View.INVISIBLE);
            topV.setVisibility(View.INVISIBLE);
            rightV.setVisibility(View.INVISIBLE);
            bottomV.setVisibility(View.VISIBLE);
            picture.setBackgroundResource(R.color.transparent);
        }
    }
}
