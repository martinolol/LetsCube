package letsapps.com.letscube.view.dialog;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import letsapps.com.letscube.R;
import letsapps.com.letscube.util.DatabaseTime;
import letsapps.com.letscube.util.TimerUtils;

public class TimeDetailDialogView extends RelativeLayout {

    TextView eventTV, singleTV, mo3TV, avg5TV, avg12TV, mo50TV, mo100TV, scrambleTV, dateTV;
    RadioButton penaltyNoRB, penalty2RB, penaltyDnfRB;

    DatabaseTime time;

    public TimeDetailDialogView(Context context) {
        super(context);

        final LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rootView = inflater.inflate(R.layout.dialog_time_detail, this);

        eventTV = (TextView)rootView.findViewById(R.id.event);
        singleTV = (TextView)rootView.findViewById(R.id.single);
        mo3TV = (TextView)rootView.findViewById(R.id.mo3);
        avg5TV = (TextView)rootView.findViewById(R.id.avg5);
        avg12TV = (TextView)rootView.findViewById(R.id.avg12);
        mo50TV = (TextView)rootView.findViewById(R.id.mo50);
        mo100TV = (TextView)rootView.findViewById(R.id.mo100);
        scrambleTV = (TextView)rootView.findViewById(R.id.scramble);
        dateTV = (TextView)rootView.findViewById(R.id.date);
        penaltyNoRB = (RadioButton)rootView.findViewById(R.id.penalty_no);
        penalty2RB = (RadioButton)rootView.findViewById(R.id.penalty_2);
        penaltyDnfRB = (RadioButton)rootView.findViewById(R.id.penalty_dnf);
    }

    public void setTime(DatabaseTime time){
        this.time = time;

        if(time == null){
            Log.e("TDDV", "time is null");
        }
        if(time.getEvent() == null){
            Log.e("TDDV", "time.getEvent() is null");
        }

        eventTV.setText(time.getEvent().getName(getContext()));
        dateTV.setText(time.getDate());
        singleTV.setText(TimerUtils.formatTime(time.getTime()));
        mo3TV.setText(TimerUtils.formatTime(time.getMo3()));
        avg5TV.setText(TimerUtils.formatTime(time.getAvg5()));
        avg12TV.setText(TimerUtils.formatTime(time.getAvg12()));
        mo50TV.setText(TimerUtils.formatTime(time.getMo50()));

        mo100TV.setText(TimerUtils.formatTime(time.getMo100()));

        if(time.getScramble() != null){
            scrambleTV.setText(time.getScramble());
        }else{
            scrambleTV.setText("--");
        }

        switch (time.getPenalty()){
            case DatabaseTime.NO_PENALTY: penaltyNoRB.setChecked(true); break;
            case DatabaseTime.PENALTY_2: penalty2RB.setChecked(true); break;
            case DatabaseTime.PENALTY_DNF: penaltyDnfRB.setChecked(true); break;
            default : penaltyNoRB.setChecked(true); break;
        }
    }

    public DatabaseTime getTime(){
        time.setPenalty(getPenalty());
        return time;
    }

    public int getPenalty(){
        if(penaltyNoRB.isChecked()){
            return DatabaseTime.NO_PENALTY;
        }else if(penalty2RB.isChecked()){
            return DatabaseTime.PENALTY_2;
        }else if(penaltyDnfRB.isChecked()){
            return DatabaseTime.PENALTY_DNF;
        }
        return DatabaseTime.NO_PENALTY;
    }
}
