package letsapps.com.letscube.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

import letsapps.com.letscube.R;
import letsapps.com.letscube.activity.TimerActivity;
import letsapps.com.letscube.adapter.TimeListAdapter;
import letsapps.com.letscube.adapter.TimeListSelectionAdapter;
import letsapps.com.letscube.listener.SingletonChange;
import letsapps.com.letscube.singleton.AppInstance;
import letsapps.com.letscube.util.DatabaseTime;
import letsapps.com.letscube.mixpanel.MixPanelHelper;
import letsapps.com.letscube.util.Date;
import letsapps.com.letscube.util.TimeList;
import letsapps.com.letscube.util.TimerUtils;
import letsapps.com.letscube.view.dialog.AddTimeDialogView;
import letsapps.com.letscube.view.dialog.TextDialogView;
import letsapps.com.letscube.view.dialog.TimeDetailDialogView;

public class TimerTimelistFragment extends LCTFragment implements SingletonChange{

    ListView timeLV;
    TextView emptyListTV;
    Button addTimeBT, deleteTimesBT;

    TimeListAdapter timeListAdapter;
    TimeListSelectionAdapter timeListSelectionAdapter;

    AlertDialog.Builder detailDialog;
    AlertDialog.Builder addTimeDialog;
    TimeDetailDialogView timeDetailDialogView;
    AddTimeDialogView addTimeDialogView;
    AlertDialog deleteTimesDialog = null;


    boolean isInSelectionMode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_timer_timelist, container, false);

        timeLV = (ListView)rootView.findViewById(R.id.times_list);
        emptyListTV = (TextView)rootView.findViewById(R.id.empty_list);
        addTimeBT = (Button)rootView.findViewById(R.id.add_time);
        deleteTimesBT = (Button)rootView.findViewById(R.id.delete_times);

        timeListAdapter = new TimeListAdapter(getActivity());
        timeLV.setAdapter(timeListAdapter);
        isInSelectionMode = false;

        if(AppInstance.getInstance().getTimes().size() == 0){
            deleteTimesBT.setVisibility(View.GONE);
        }

        timeLV.setOnItemClickListener(clickItemTimeList);
        timeLV.setEmptyView(emptyListTV);

        detailDialog = new AlertDialog.Builder(getActivity());
        detailDialog.setView(timeDetailDialogView);

        addTimeDialog = new AlertDialog.Builder(getActivity());
        addTimeDialog.setNegativeButton(R.string.cancel, null);

        addTimeBT.setOnClickListener(clickAddTimeButton);
        deleteTimesBT.setOnClickListener(clickDeleteTimesButton);

        return rootView;
    }

    private AdapterView.OnItemClickListener clickItemTimeList = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
            if (isInSelectionMode) {

            } else {
                timeDetailDialogView = new TimeDetailDialogView(getContext());
                timeDetailDialogView.setTime((DatabaseTime) timeListAdapter.getItem(position));
                detailDialog.setView(timeDetailDialogView);
                detailDialog.setNegativeButton(R.string.timelist_detail_save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (((DatabaseTime) timeListAdapter.getItem(position)).getPenalty() !=
                                timeDetailDialogView.getPenalty()) {
                            ((TimerActivity) getActivity()).editTimePenalty(timeDetailDialogView.getTime());
                            timeListAdapter.notifyDataSetChanged();
                        }
                    }
                });
                detailDialog.setPositiveButton(R.string.timelist_detail_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MixPanelHelper.sendSingleTimeDeleted(
                                getContext(), (DatabaseTime) timeListAdapter.getItem(position));
                        ((TimerActivity) getActivity()).deleteTime(
                                (DatabaseTime) timeListAdapter.getItem(position));
                        timeListAdapter.notifyDataSetChanged();
                    }
                });
                detailDialog.show();
            }
        }
    };

    private View.OnClickListener clickAddTimeButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(isInSelectionMode){
                invertSelectionMode();
            }else {

                addTimeDialogView = new AddTimeDialogView(getContext());
                addTimeDialog.setView(addTimeDialogView);
                addTimeDialog.setNegativeButton(R.string.cancel, null);
                addTimeDialog.setPositiveButton(R.string.timelist_add_add,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if (addTimeDialogView.getEnteredTimeInMs() == TimerUtils.TIME_NO_VALUE){

                            } else {
                                final DatabaseTime time =
                                        new DatabaseTime(addTimeDialogView.getEnteredTimeInMs(),
                                                AppInstance.getInstance().getEvent());
                                time.setDate(Date.getDate());
                                time.setSession(((TimerActivity)getActivity()).getLastSession());
                                ((TimerActivity) getActivity()).addTimeToDatabase(time);
                                timeListAdapter.notifyDataSetChanged();
                                deleteTimesBT.setVisibility(View.VISIBLE);

                                MixPanelHelper.sendTimeAddedManually(getContext(), time);
                            }
                            Log.d("TTLF (addTimeOk)", "getEnteredTimeInMs() = " +
                                    addTimeDialogView.getEnteredTimeInMs());
                            Log.d("TTLF (addTimeOk)", "formatTime() = " +
                                    TimerUtils.formatTime(addTimeDialogView.getEnteredTimeInMs()));
                        }
                });
                addTimeDialog.show();
            }
        }
    };

    private View.OnClickListener clickDeleteTimesButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(isInSelectionMode){
                final AlertDialog.Builder deleteTimesDialogBuilder =
                        new AlertDialog.Builder(getContext());
                final TextDialogView dialogView = new TextDialogView(
                        getContext(), R.string.timelist_delete_dialog_title);

                deleteTimesDialogBuilder.setView(dialogView);
                deleteTimesDialogBuilder.setNegativeButton(R.string.cancel, null);

                final ArrayList<DatabaseTime> selectedTimes =
                        timeListSelectionAdapter.getCheckedTimes();

                if(selectedTimes.size() == 0) {
                    dialogView.setText(R.string.timelist_delete_dialog_desc_empty);
                }else{
                    dialogView.setText(getContext().getResources().getQuantityString(
                            R.plurals.timelist_delete_dialog_desc,
                            selectedTimes.size(),
                            selectedTimes.size(),
                            AppInstance.getInstance().getEvent().getName(getContext())));
                    deleteTimesDialogBuilder.setPositiveButton(R.string.delete,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteTimesDialog.dismiss();
                                    timeListSelectionAdapter.removeSelectedTimes();
                                    if(AppInstance.getInstance().getTimes().size() == 0){
                                        deleteTimesBT.setVisibility(View.GONE);
                                    }
                                    invertSelectionMode();

                                }
                            });
                }
                deleteTimesDialog = deleteTimesDialogBuilder.create();
                deleteTimesDialog.show();
            }else{
                invertSelectionMode();
            }
        }
    };

    public void invertSelectionMode(){
        isInSelectionMode = !isInSelectionMode;
        if(isInSelectionMode){
            addTimeBT.setText(R.string.cancel);
            deleteTimesBT.setText(R.string.delete);
            timeListSelectionAdapter = new TimeListSelectionAdapter(getContext());
            timeLV.setAdapter(timeListSelectionAdapter);
        }else{
            addTimeBT.setText(R.string.timelist_add_add);
            deleteTimesBT.setText(R.string.timelist_delete_multiple_times);
            timeListAdapter = new TimeListAdapter(getContext());
            timeLV.setAdapter(timeListAdapter);
        }
    }

    public void highlightTimes(DatabaseTime[] timesToHighlight){

        timeListAdapter.highlightTimes(timesToHighlight);

        int lowestTimeToHighlightId = timesToHighlight[0].getId();
        if(timesToHighlight[0].getId() < timesToHighlight[timesToHighlight.length - 1].getId()){
            lowestTimeToHighlightId = timesToHighlight[timesToHighlight.length - 1].getId();
        }

        final TimeList times = AppInstance.getInstance().getTimes();
        final int nbTimes = times.size();
        for(int i = 0; i < nbTimes; i++){
            if(times.get(i).getId() == lowestTimeToHighlightId){
                timeLV.setSelection(nbTimes - i - 1);
                break;
            }
        }
    }

    public void onCurrentEventChange(){

    }

    public void onCurrentTimesChange(){
        timeListAdapter.notifyDataSetChanged();
    }

}
