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

public class AddEventDialogView extends RelativeLayout {

    EditText eventNameET;
    ScrambleTypePicker scrambleTypePicker;

    public AddEventDialogView(Context context) {
        super(context);

        final LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rootView = inflater.inflate(R.layout.dialog_add_event, this);

        eventNameET = (EditText)rootView.findViewById(R.id.event_name);
        scrambleTypePicker = (ScrambleTypePicker)rootView.findViewById(R.id.scramble_type);

    }

    public Event getEnteredEvent(){
        final Event event = new Event();
        event.setName(eventNameET.getText().toString());
        event.setScrambleTypeId(scrambleTypePicker.getSelectedScrambleType());
        event.setIsOfficial(CubeEvents.EVENTS_UNOFFICIAL);

        return event;
    }
}
