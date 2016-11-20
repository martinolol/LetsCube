package letsapps.com.letscube.view.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import letsapps.com.letscube.R;
import letsapps.com.letscube.singleton.TimerSettings;
import letsapps.com.letscube.util.DatabaseTime;
import letsapps.com.letscube.util.TimerUtils;

/**
 * Created by marti on 19/08/2016.
 */
public class BestTimesDialogView extends RelativeLayout {

    ViewDialogTitle title;
    ListView timeList;
    TimeListAdapter timeListAdapter;

    public BestTimesDialogView(Context context) {
        super(context);
        final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rootView = inflater.inflate(R.layout.dialog_list, this);

        title = (ViewDialogTitle) rootView.findViewById(R.id.title);
        timeList = (ListView) rootView.findViewById(R.id.list);
    }

    public void setRecordType(int nbTimes) {
        switch (nbTimes) {
            case 1:
                title.setTitle(getContext().getString(R.string.dialog_best_time_title, "single"));
                break;
            case 3:
                title.setTitle(getContext().getString(R.string.dialog_best_time_title, "mo3"));
                break;
            case 5:
                title.setTitle(getContext().getString(R.string.dialog_best_time_title, "avg5"));
                break;
            case 12:
                title.setTitle(getContext().getString(R.string.dialog_best_time_title, "avg12"));
                break;
            case 50:
                title.setTitle(getContext().getString(R.string.dialog_best_time_title, "mo50"));
                break;
            case 100:
                title.setTitle(getContext().getString(R.string.dialog_best_time_title, "mo100"));
                break;
            default:
                title.setTitle(getContext().getString(R.string.dialog_best_time_title, "-"));
        }
    }

    public void setTimes(DatabaseTime[] times) {
        if(times != null) {
            setRecordType(times.length);
            timeListAdapter = new TimeListAdapter(times);
            timeList.setAdapter(timeListAdapter);
        }else{
            Log.i("BestTimeDV", "times == null");
        }
    }

    private class TimeListAdapter extends BaseAdapter {

        DatabaseTime[] times;
        LayoutInflater inflater;

        AlertDialog.Builder scrambleDialog;
        TextDialogView textDialogView = null;

        public TimeListAdapter(DatabaseTime[] times) {
            this.times = times;
            inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            scrambleDialog = new AlertDialog.Builder(getContext());
            scrambleDialog.setNegativeButton(R.string.close, null);
        }

        @Override
        public int getCount() {
            return times.length;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.item_list_times, parent, false);

            final TextView timeTV = (TextView)convertView.findViewById(R.id.time);
            final TextView dateTV = (TextView)convertView.findViewById(R.id.date);

            final DatabaseTime currentTime = times[position];

            dateTV.setText(currentTime.getDate()
                    .substring(0, currentTime.getDate().length() - 5));

            timeTV.setText((position + 1) + "/  " +
                    TimerUtils.formatTime(currentTime, true, TimerSettings.TIMER_UPDATE_MILLISECOND));

            convertView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    final TextDialogView textDialogView = new TextDialogView(getContext());
                    textDialogView.setTitle(
                            timeTV.getText().toString().substring(3, timeTV.length()));
                    textDialogView.setText(currentTime.getScramble());
                    if(currentTime.getScramble() == null){
                        textDialogView.setText(R.string.dialog_scramble_empty_message);
                    }
                    scrambleDialog.setView(textDialogView);
                    scrambleDialog.show();
                }
            });

            return convertView;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return times[position];
        }
    }
}
