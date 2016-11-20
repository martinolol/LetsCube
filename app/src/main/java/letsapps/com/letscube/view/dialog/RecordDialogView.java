package letsapps.com.letscube.view.dialog;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import letsapps.com.letscube.R;
import letsapps.com.letscube.activity.TimerActivity;
import letsapps.com.letscube.singleton.AppInstance;
import letsapps.com.letscube.singleton.TimerSettings;
import letsapps.com.letscube.util.cube.Event;
import letsapps.com.letscube.util.TimerUtils;

public class RecordDialogView extends RelativeLayout {

    TextView recordTV;
    View backgroundV;

    String records = "";
    int nbRecords;

    public RecordDialogView(Context context) {
        super(context);

        final LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rootView = inflater.inflate(R.layout.dialog_record, this);

        recordTV = (TextView)rootView.findViewById(R.id.record_beat);
        backgroundV = rootView.findViewById(R.id.background);

        nbRecords = 0;

        backgroundV.setBackgroundColor(
                TimerSettings.getInstance().getTheme().getSecundaryColor());
    }

    public int getNbRecords(){
        return nbRecords;
    }

    public void addRecord(String recordType, int value, int previousRecordValue){
        nbRecords++;

        if (previousRecordValue == TimerUtils.TIME_NO_VALUE) {
            records += getContext().getResources().getString(R.string.beat_record_no_previous,
                    AppInstance.getInstance().getEvent().getName(getContext()),
                    recordType,
                    TimerUtils.formatTime(value)) +
                    "\n\n";
        } else {
            records += getContext().getResources().getString(R.string.beat_record,
                    AppInstance.getInstance().getEvent().getName(getContext()),
                    recordType,
                    TimerUtils.formatTime(value),
                    TimerUtils.formatTime(previousRecordValue)) +
                    "\n\n";
        }

        Log.d("RDV (addRecord)", "recordsString : " + records);

    }

    public void showRecord(){

        // Pour supprimer le dernier "\n\n"
        records = records.substring(0, records.length() - 2);
        recordTV.setText(records);
    }
}
