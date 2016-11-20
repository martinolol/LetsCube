package letsapps.com.letscube.view.actionBar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import letsapps.com.letscube.R;
import letsapps.com.letscube.activity.TimerActivity;
import letsapps.com.letscube.singleton.AppInstance;
import letsapps.com.letscube.singleton.TimerSettings;
import letsapps.com.letscube.util.Theme;

public class LCActionBarV2 extends RelativeLayout {

    final int NB_BUTTONS = 3;

    private Button titleView;
    private ActionBarButton[] buttons;

    private String currentFragmentId;

    public LCActionBarV2(Context context) {
        super(context);
        init();
    }

    public LCActionBarV2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LCActionBarV2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        final LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rootView = inflater.inflate(R.layout.view_actionbar_v2, this);

        buttons = new ActionBarButton[NB_BUTTONS];

        titleView = (Button)rootView.findViewById(R.id.title);
        buttons[0] = (ActionBarButton)rootView.findViewById(R.id.timer);
        buttons[1] = (ActionBarButton)rootView.findViewById(R.id.stats);
        buttons[2] = (ActionBarButton)rootView.findViewById(R.id.settings);

        if(AppInstance.getInstance().getEvent() != null) {
            titleView.setText(AppInstance.getInstance().getEvent().getName(getContext()));
        }

        if(TimerSettings.getInstance().getTheme() != null) {
            setBackgroundColor(TimerSettings.getInstance().getTheme().getMainColor());
        }

        buttons[0].setOnClickListener(new OnButtonClickListener(TimerActivity.FRAGMENT_TIMER));
        buttons[1].setOnClickListener(new OnButtonClickListener(TimerActivity.FRAGMENT_TIMES));
        buttons[2].setOnClickListener(new OnButtonClickListener(TimerActivity.FRAGMENT_SETTINGS));
        titleView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TimerActivity) getContext()).launchEventDialog();
            }
        });

    }

    private class OnButtonClickListener implements OnClickListener{

        private String fragmentToLaunchId;

        public OnButtonClickListener(String fragmentToLaunchId){
            this.fragmentToLaunchId = fragmentToLaunchId;
        }

        @Override
        public void onClick(View v){
            if(currentFragmentId != fragmentToLaunchId) {
                ((TimerActivity) getContext()).launchFragment(fragmentToLaunchId);
            }
        }
    }

    public void selectCurrentMenu(String currentFragmentId){
        this.currentFragmentId = currentFragmentId;
        buttons[0].setSelected(false);
        buttons[1].setSelected(false);
        buttons[2].setSelected(false);
        switch(currentFragmentId){
            case TimerActivity.FRAGMENT_TIMER :
                buttons[0].setSelected(true);
                break;
            case TimerActivity.FRAGMENT_TIMES :
                buttons[1].setSelected(true);
                break;
            case TimerActivity.FRAGMENT_SETTINGS :
                buttons[2].setSelected(true);
                break;
        }
    }

    public void setTitle(String title){
        titleView.setText(title);
    }

    public void updateTheme(){
        if(TimerSettings.getInstance().getTheme() != null) {
            setBackgroundColor(TimerSettings.getInstance().getTheme().getMainColor());
        }
    }
}
