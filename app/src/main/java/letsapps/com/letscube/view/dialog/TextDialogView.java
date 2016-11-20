package letsapps.com.letscube.view.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import letsapps.com.letscube.R;

public class TextDialogView  extends LinearLayout {

    private ViewDialogTitle titleV;
    private TextView textV;

    private BaseAdapter listAdapter;

    public TextDialogView(Context context) {
        super(context);

        final LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rootView = inflater.inflate(R.layout.dialog_text, this);

        titleV = (ViewDialogTitle)rootView.findViewById(R.id.title);
        textV = (TextView)rootView.findViewById(R.id.text);

    }

    public TextDialogView(Context context, int titleResId) {
        super(context);

        final LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rootView = inflater.inflate(R.layout.dialog_text, this);

        titleV = (ViewDialogTitle)rootView.findViewById(R.id.title);
        textV = (TextView)rootView.findViewById(R.id.text);

        titleV.setTitle(context.getString(titleResId));
    }

    public void setTitle(String title){
        titleV.setTitle(title);
    }

    public void setTitle(int titleResId){
        titleV.setTitle(titleResId);
    }

    public void setText(int textResId){
        textV.setText(textResId);
    }

    public void setText(String text){
        textV.setText(text);
    }

    public void setText(int textResId, Object... formatArgs){
        textV.setText(getContext().getString(textResId, formatArgs));
    }
}
