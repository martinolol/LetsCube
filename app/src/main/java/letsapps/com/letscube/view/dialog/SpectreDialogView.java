package letsapps.com.letscube.view.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import letsapps.com.letscube.R;
import letsapps.com.letscube.singleton.TimerSettings;
import letsapps.com.letscube.util.Theme;
import letsapps.com.letscube.view.SpectreView;

public class SpectreDialogView extends LinearLayout{

    ViewDialogTitle titleView;
    SpectreView spectreView;
    TextView themeName;

    public SpectreDialogView(final Context context) {
        super(context);

        final LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rootView = inflater.inflate(R.layout.dialog_spectre, this);

        titleView = (ViewDialogTitle)rootView.findViewById(R.id.dialog_spectre_title);
        spectreView = (SpectreView)rootView.findViewById(R.id.spectre);
        themeName = (TextView)rootView.findViewById(R.id.theme_name);

        themeName.setText(TimerSettings.getInstance().getTheme().getName(context));
        titleView.setBackgroundColor(TimerSettings.getInstance().getTheme().getMainColor());

        spectreView.setSpectreListener(new SpectreView.SpectreListener() {
            @Override
            public void OnSelectedThemeChange(Theme selectedTheme) {
                themeName.setText(selectedTheme.getName(context));
                titleView.setBackgroundColor(selectedTheme.getMainColor());
            }
        });
    }

    public Theme getSelectedTheme(){
        return spectreView.getSelectedTheme();
    }
}
