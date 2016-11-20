package letsapps.com.letscube.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import letsapps.com.letscube.R;
import letsapps.com.letscube.activity.TimerActivity;
import letsapps.com.letscube.singleton.AppInstance;
import letsapps.com.letscube.singleton.TimerSettings;
import letsapps.com.letscube.util.DatabaseTime;
import letsapps.com.letscube.mixpanel.MixPanelHelper;
import letsapps.com.letscube.util.TimeList;
import letsapps.com.letscube.util.TimerUtils;
import letsapps.com.letscube.view.dialog.ProgressDialogView;

public class TimeListSelectionAdapter extends BaseAdapter {

    public static final int POSITION_ALL = 0;

    Context context;
    TimerActivity activity;
    LayoutInflater inflater;

    ArrayList<DatabaseTime> times; // liste de tous les temps.
    ArrayList<Boolean> checkedTimes; // liste des booleans des temps pour savoir s'ils sont checked

    AlertDialog progressDialog = null;
    AlertDialog.Builder progressDialogBuilder = null;
    ProgressDialogView progressDialogView = null;
    int nbRemovedTimes = 0; // à déclarer à un niveau global pour l'handle dans le thread mult. del.
    int nbTimesToRemove = 0;

    boolean isAllSelected;

    public TimeListSelectionAdapter(Context context){
        this.context = context;
        activity = (TimerActivity) context;
        this.times = AppInstance.getInstance().getTimes().getCurrentList();
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        checkedTimes = new ArrayList<>();
        for(int i = 0; i < times.size(); i++){
            checkedTimes.add(false);
        }
        isAllSelected = false;

        progressDialogBuilder = new AlertDialog.Builder(context);
        progressDialogView = new ProgressDialogView(context);
        progressDialogView.setTitle(R.string.dialog_progress_deletion_title);
        progressDialogBuilder.setView(progressDialogView);
        progressDialog = progressDialogBuilder.create();
    }

    @Override
    public int getCount() {
        return times.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return times.get((times.size() - 1) - position);
    }

    @Override
    public long getItemId(int position) {
        if((times.size() - 1) - position >= 0) {
            return times.get((times.size() - 1) - position).getId();
        }else{
            return 0L;
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.item_list_times_selection, parent, false);

        final CheckBox selectCB = (CheckBox)convertView.findViewById(R.id.selector);
        TextView timeTV = (TextView)convertView.findViewById(R.id.time);
        TextView dateTV = (TextView)convertView.findViewById(R.id.date);

        if(position == POSITION_ALL){
            timeTV.setText(R.string.fragment_timelist_selection_all);
            dateTV.setText("");

            selectCB.setChecked(isAllSelected);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectCB.setChecked(!selectCB.isChecked());
                    isAllSelected = selectCB.isChecked();
                    for(int i = 0; i < checkedTimes.size(); i++){
                        checkedTimes.set(i, isAllSelected);
                    }
                    notifyDataSetChanged();
                }
            });

        }else {

            final DatabaseTime currentTime = times.get((times.size() - 1) - (position - 1));

            selectCB.setChecked(checkedTimes.get((checkedTimes.size() - 1) - (position - 1)));

            dateTV.setText(currentTime.getDate().substring(0, currentTime.getDate().length() - 5));

            timeTV.setText(position + "/  " +
                    TimerUtils.formatTime(currentTime, true, TimerSettings.TIMER_UPDATE_MILLISECOND));

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectCB.setChecked(!selectCB.isChecked());
                    checkedTimes.set((checkedTimes.size() - 1) - (position - 1),
                            selectCB.isChecked());
                    if(selectCB.isChecked()){
                        for(int i = 0; i < checkedTimes.size(); i++){
                            if(!checkedTimes.get(i)){
                                isAllSelected = false;
                                break;
                            }
                            if(i == checkedTimes.size() - 1 && checkedTimes.get(i)){
                                isAllSelected = true;
                                notifyDataSetChanged();
                            }
                        }
                    }else{
                        isAllSelected = false;
                        notifyDataSetChanged();
                    }
                }
            });

        }

        //timeTV.setAdaptableText(timeTV.getText() + "  [" + TimerUtils.formatTime(currentTime.getMo3()) + "]");
        //timeTV.setAdaptableText(timeTV.getText() + "[" + TimerUtils.formatTime(currentTime.getAvg5()) + "]");

        return convertView;
    }

    public ArrayList<DatabaseTime> getCheckedTimes(){
        if(isAllSelected){
            return times;
        }
        final ArrayList<DatabaseTime> timesToReturn = new ArrayList<>();
        for(int i = 0; i < times.size(); i++){
            if(checkedTimes.get(i)){
                timesToReturn.add(times.get(i));
            }
        }
        return timesToReturn;
    }

    // demande à l'activité de supprimer les temps selectionnés 1 par 1.
    public void removeSelectedTimes(){
        nbRemovedTimes = 0;
        nbTimesToRemove = 0;

        if(isAllSelected){
            nbRemovedTimes = times.size();
            ((TimerActivity)context).deleteAllTimes(AppInstance.getInstance().getEvent());
        }else{
            Log.i("TLSA", "start showing progressDialog for multiple times deletion");
            progressDialog.show();
            for(int i = 0; i < checkedTimes.size(); i++){
                if(checkedTimes.get(i)) {
                    nbTimesToRemove++;
                }
            }
            final Thread multipleDeletionThread = new Thread() {
                @Override
                public void run() {
                    try {
                        super.run();
                        for(int i = 0; i < times.size(); i++){
                            if(checkedTimes.get(i)){
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialogView.setProgressMessage(
                                                context.getString(R.string.dialog_progress_deletion_message,
                                                        nbRemovedTimes,
                                                        nbTimesToRemove));
                                    }
                                });
                                ((TimerActivity)context).deleteTime(times.get(i));
                                checkedTimes.remove(i);
                                i--;
                                nbRemovedTimes++;
                            }
                        }
                    } catch (Exception e) {
                        Log.e("TLSA", "exception occure deleting times : " + e.getMessage());
                    } finally {
                        Log.i("TLSA", "multipleDeletionThread finished");
                        progressDialog.dismiss();
                    }
                }
            };
            multipleDeletionThread.start();
        }
        MixPanelHelper.sendMultipleDeletion(((TimerActivity)context).getMixpanelInstance(),
                nbRemovedTimes, AppInstance.getInstance().getEvent().getName(context));
    }
}