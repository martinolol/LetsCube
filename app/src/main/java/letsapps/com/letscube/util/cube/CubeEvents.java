package letsapps.com.letscube.util.cube;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

import letsapps.com.letscube.R;
import letsapps.com.letscube.database.DatabaseEventHandler;
import letsapps.com.letscube.util.scramble.ScrambleGenerator;

public class CubeEvents {

    public static final int NO_EVENT = 0x00;

    // EVENTS TYPE
    public static final boolean EVENTS_OFFICIAL = true;
    public static final boolean EVENTS_UNOFFICIAL = false;

    public static final Event EVENTS_ADD = new Event(0xADD, R.string.event_add,
            R.drawable.ic_events_add, ScrambleGenerator.NO_SCRAMBLE_TYPE,
            EVENTS_UNOFFICIAL);

    // OFFICIAL EVENTS
    public static final Event SQUARE1 = new Event(0xA01, R.string.event_square1,
            R.drawable.cube_square1_finished, ScrambleGenerator.SCRAMBLE_TYPE_SQUARE1,
            EVENTS_OFFICIAL);
    public static final Event CUBE2x2 = new Event(0xA02, R.string.event_2x2,
            R.drawable.cube_2_finished, ScrambleGenerator.SCRAMBLE_TYPE_2x2,
            EVENTS_OFFICIAL);
    public static final Event CUBE3x3 = new Event(0xA03, R.string.event_3x3,
            R.drawable.cube_3_finished, ScrambleGenerator.SCRAMBLE_TYPE_3x3,
            EVENTS_OFFICIAL);
    public static final Event CUBE4x4 = new Event(0xA04, R.string.event_4x4,
            R.drawable.cube_4_finished, ScrambleGenerator.SCRAMBLE_TYPE_4x4,
            EVENTS_OFFICIAL);
    public static final Event CUBE5x5 = new Event(0xA05, R.string.event_5x5,
            R.drawable.cube_5_finished, ScrambleGenerator.SCRAMBLE_TYPE_5x5,
            EVENTS_OFFICIAL);
    public static final Event CUBE6x6 = new Event(0xA06, R.string.event_6x6,
            R.drawable.cube_6_finished, ScrambleGenerator.SCRAMBLE_TYPE_6x6,
            EVENTS_OFFICIAL);
    public static final Event CUBE7x7 = new Event(0xA07, R.string.event_7x7,
            R.drawable.cube_7_finished, ScrambleGenerator.SCRAMBLE_TYPE_7x7,
            EVENTS_OFFICIAL);
    public static final Event CUBE3x3OH = new Event(0xA08, R.string.event_3x3oh,
            R.drawable.cube_3_oh_finished, ScrambleGenerator.SCRAMBLE_TYPE_3x3,
            EVENTS_OFFICIAL);
    public static final Event CUBE3x3FT = new Event(0xA09, R.string.event_3x3ft,
            R.drawable.cube_3_ft_finished, ScrambleGenerator.SCRAMBLE_TYPE_3x3,
            EVENTS_OFFICIAL);
    public static final Event CUBE3x3BLD = new Event(0xA0A, R.string.event_3x3bld,
            R.drawable.cube_3_bld_finished, ScrambleGenerator.SCRAMBLE_TYPE_3x3,
            EVENTS_OFFICIAL);
    public static final Event CUBE4x4BLD = new Event(0xA0B, R.string.event_4x4bld,
            R.drawable.cube_4_bld_finished, ScrambleGenerator.SCRAMBLE_TYPE_4x4,
            EVENTS_OFFICIAL);
    public static final Event CUBE5x5BLD = new Event(0xA0C, R.string.event_5x5bld,
            R.drawable.cube_5_bld_finished, ScrambleGenerator.SCRAMBLE_TYPE_5x5,
            EVENTS_OFFICIAL);
    public static final Event PYRAMINX = new Event(0xA0D, R.string.event_pyraminx,
            R.drawable.cube_pyraminx_finished, ScrambleGenerator.SCRAMBLE_TYPE_PYRAMINX,
            EVENTS_OFFICIAL);
    public static final Event MEGAMINX = new Event(0xA0E, R.string.event_megaminx,
            R.drawable.cube_megaminx_finished, ScrambleGenerator.SCRAMBLE_TYPE_MEGAMINX,
            EVENTS_OFFICIAL);
    public static final Event SKEWB = new Event(0xA0F, R.string.event_skewb,
            R.drawable.cube_skewb_finished, ScrambleGenerator.SCRAMBLE_TYPE_SKEWB,
            EVENTS_OFFICIAL);
    public static final Event CLOCK = new Event(0xA10, R.string.event_clock,
            R.drawable.cube_clock_finished, ScrambleGenerator.SCRAMBLE_TYPE_CLOCK,
            EVENTS_OFFICIAL);

    // UNOFFICIAL EVENTS
    public static final Event CUBE3x3x2 = new Event(0xB01, R.string.event_3x3x2,
            R.drawable.cube_3x3x2_finished, ScrambleGenerator.SCRAMBLE_TYPE_3x3x2,
            EVENTS_UNOFFICIAL);
    public static final Event CUBE2x2x3 = new Event(0xB02, R.string.event_2x2x3,
            R.drawable.cube_2x2x3_finished, ScrambleGenerator.SCRAMBLE_TYPE_3x3x2,
            EVENTS_UNOFFICIAL);
    public static final Event CUBE_MIRROR = new Event(0xB03, R.string.event_mirror,
            R.drawable.cube_mirror_finished, ScrambleGenerator.SCRAMBLE_TYPE_3x3,
            EVENTS_UNOFFICIAL);
    public static final Event BARREL3x3 = new Event(0xB04, R.string.event_barrel3x3,
            R.drawable.cube_barrel3x3_finished, ScrambleGenerator.SCRAMBLE_TYPE_3x3,
            EVENTS_UNOFFICIAL);
    public static final Event MASTERMORPHIX = new Event(0xB05, R.string.event_mastermorphix,
            R.drawable.cube_mastermorphix_finished, ScrambleGenerator.NO_SCRAMBLE_TYPE,
            EVENTS_UNOFFICIAL);
    public static final Event EITAN_TWIST = new Event(0xB06, R.string.event_eitantwist,
            R.drawable.cube_eitan_twist_finished, ScrambleGenerator.SCRAMBLE_TYPE_3x3,
            EVENTS_UNOFFICIAL);

    public static Event[] getEvents(final boolean eventType){
        return eventType == EVENTS_OFFICIAL
                ? new Event[]{CUBE2x2, CUBE3x3, CUBE4x4, CUBE5x5, CUBE6x6, CUBE7x7,
                        CUBE3x3OH, CUBE3x3FT, CUBE3x3BLD, CUBE4x4BLD, CUBE5x5BLD, SKEWB, SQUARE1,
                        PYRAMINX, MEGAMINX, CLOCK}
                : new Event[]{EVENTS_ADD, CUBE2x2x3, CUBE3x3x2, CUBE_MIRROR, BARREL3x3, MASTERMORPHIX,
                        EITAN_TWIST};
    }

    public static Event[] getEvents(){
        return new Event[]{CUBE2x2, CUBE3x3, CUBE4x4, CUBE5x5, CUBE6x6, CUBE7x7,
                CUBE3x3OH, CUBE3x3FT, CUBE3x3BLD, CUBE4x4BLD, CUBE5x5BLD, SKEWB, SQUARE1,
                PYRAMINX, MEGAMINX, CLOCK, CUBE2x2x3, CUBE3x3x2, CUBE_MIRROR, BARREL3x3,
                MASTERMORPHIX, EITAN_TWIST, EVENTS_ADD};
    }

    public static Event[] getEvents(Context context){
        Log.d("CubeEvents", "start getting all events");
        final ArrayList<Event> addedEvents = new DatabaseEventHandler(context).getEvents();

        if(addedEvents == null){
            return getEvents();
        }

        final Event[] non_added_event = getEvents();
        Event[] events = new Event[non_added_event.length + addedEvents.size()];
        for(int i = 0; i < events.length; i++){
            if(i < non_added_event.length) {
                events[i] = non_added_event[i];
            }else{
                events[i] = addedEvents.get(i - non_added_event.length);
            }
        }
        Log.d("CubeEvents", "returning " + events.length + " events.");
        return events;
    }

    public static Event getEvent(Context context, int eventId){
        final Event[] events = getEvents();
        for(Event event : events){
            if(event.getId() == eventId){
                return event;
            }
        }
        Log.e("CubeEvents", "attempting to get an event from an id that don't exist.");
        final DatabaseEventHandler database = new DatabaseEventHandler(context);
        return database.getEvent(eventId);
    }

    public static boolean isAddedEvent(int eventId){
        // square1 est le plus petit id des events de base
        if(eventId >= SQUARE1.getId()){
            return false;
        }else{
            return true;
        }
    }
}