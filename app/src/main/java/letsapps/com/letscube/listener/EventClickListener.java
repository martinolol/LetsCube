package letsapps.com.letscube.listener;

import letsapps.com.letscube.util.cube.Event;

public interface EventClickListener {
    void onEventClick(Event event, boolean dismissDialog);
}
