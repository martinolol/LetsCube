package letsapps.com.letscube.view.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import letsapps.com.letscube.R;

/**
 * Created by marti on 21/08/2016.
 */
public class ProgressDialogView extends RelativeLayout {

    ViewDialogTitle titleView = null;
    TextView progressTV = null;

    public ProgressDialogView(Context context) {
        super(context);

        final LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rootView = inflater.inflate(R.layout.dialog_progress, this);

        titleView = (ViewDialogTitle)rootView.findViewById(R.id.dialog_progress_title);
        progressTV = (TextView)rootView.findViewById(R.id.dialog_progress_message);
    }

    public void setTitle(int titleResId){
        titleView.setTitle(titleResId);
    }

    public void setProgressMessage(String progressMessageResId){
        progressTV.setText(progressMessageResId);
    }
}
