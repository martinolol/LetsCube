package letsapps.com.letscube.view.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import letsapps.com.letscube.R;
import letsapps.com.letscube.util.cube.CubeEvents;
import letsapps.com.letscube.util.cube.Event;
import letsapps.com.letscube.view.ScrambleTypePicker;

public class EditEventDialogView extends RelativeLayout{

    EditText eventNameET;
    ScrambleTypePicker scrambleTypePicker;

    Event event;

    public EditEventDialogView(Context context, Event event) {
        super(context);

        final LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rootView = inflater.inflate(R.layout.dialog_edit_event, this);

        this.event = event;

        eventNameET = (EditText)rootView.findViewById(R.id.event_name);
        scrambleTypePicker = (ScrambleTypePicker)rootView.findViewById(R.id.scramble_type);

        eventNameET.setText(event.getName());
        scrambleTypePicker.select(event.getScrambleTypeId());

    }

    public Event getEvent(){
        event.setName(eventNameET.getText().toString());
        event.setScrambleTypeId(scrambleTypePicker.getSelectedScrambleType());
        return event;
    }
}
