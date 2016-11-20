package letsapps.com.letscube.view.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import letsapps.com.letscube.R;

public class AddTimeDialogView extends RelativeLayout {

    EditText timeMinET, timeSecET, timeHunET;

    public AddTimeDialogView(Context context) {
        super(context);

        final LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rootView = inflater.inflate(R.layout.dialog_add_time, this);

        timeMinET = (EditText)rootView.findViewById(R.id.time_min);
        timeSecET = (EditText)rootView.findViewById(R.id.time_sec);
        timeHunET = (EditText)rootView.findViewById(R.id.time_mil);
    }

    public int getEnteredTimeInMs(){

        int msValueOfMin = 0;
        int msValueOfSec = 0;
        int msValueOfHun = 0;

        if(!timeMinET.getText().toString().isEmpty()){
            msValueOfMin = Integer.parseInt(timeMinET.getText().toString()) * 60000;
        }
        if(!timeSecET.getText().toString().isEmpty()){
            msValueOfSec = Integer.parseInt(timeSecET.getText().toString()) * 1000;
        }
        if(!timeHunET.getText().toString().isEmpty()){
            msValueOfHun = Integer.parseInt(timeHunET.getText().toString());
        }

        return msValueOfMin + msValueOfSec + msValueOfHun;
    }
}
