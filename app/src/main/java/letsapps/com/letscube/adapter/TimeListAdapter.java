package letsapps.com.letscube.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import letsapps.com.letscube.R;
import letsapps.com.letscube.singleton.AppInstance;
import letsapps.com.letscube.singleton.TimerSettings;
import letsapps.com.letscube.util.DatabaseTime;
import letsapps.com.letscube.util.TimeList;
import letsapps.com.letscube.util.TimerUtils;

public class TimeListAdapter extends BaseAdapter {

    //TODO : make a possibility to sort time!!! (taking care of memory...) /!\

    Context context;
    LayoutInflater inflater;

    List<DatabaseTime> times;

    DatabaseTime[] timesToHighlight;

    public TimeListAdapter(Context context){
        this.context = context;
        this.times = AppInstance.getInstance().getTimes().getCurrentList();
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        timesToHighlight = new DatabaseTime[0];
    }

    @Override
    public int getCount() {
        if(times != null) {
            return times.size();
        }else{
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return times.get((times.size() - 1) - position);
    }

    @Override
    public long getItemId(int position) {
        if(position > times.size()){
            return 0;
        }
        return times.get((times.size() - 1) - position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.item_list_times, parent, false);

        TextView timeTV = (TextView)convertView.findViewById(R.id.time);
        TextView dateTV = (TextView)convertView.findViewById(R.id.date);

        final DatabaseTime currentTime = times.get((times.size() - 1) - position);

        final int nbTimesToHighLight = timesToHighlight.length;
        for(int i = 0; i < nbTimesToHighLight; i++) {
            if (currentTime.getId() == timesToHighlight[i].getId()) {
                convertView.setBackgroundColor(
                        context.getResources().getColor(R.color.list_highlight));
                break;
            }
        }

        if(currentTime.getDate() != null) {
            dateTV.setText(currentTime.getDate().substring(0, currentTime.getDate().length() - 5));
        }else{
            dateTV.setText("--/--");
        }

        timeTV.setText((position + 1) + "/  " +
                TimerUtils.formatTime(currentTime, true, TimerSettings.TIMER_UPDATE_MILLISECOND));

        return convertView;
    }

    public void highlightTimes(DatabaseTime[] timesToHighlight){

        int minTimeToHighlightId = 0;

        if(timesToHighlight[timesToHighlight.length - 1].getId() > timesToHighlight[0].getId()){
            minTimeToHighlightId = timesToHighlight[0].getId();
        }else{
            minTimeToHighlightId = timesToHighlight[timesToHighlight.length - 1].getId();
        }
        for(int i = 0; i < times.size(); i++) {
            if (times.get(i).getId() == minTimeToHighlightId) {
                break;
            }
        }
        this.timesToHighlight = timesToHighlight;
        notifyDataSetChanged();
    }
}