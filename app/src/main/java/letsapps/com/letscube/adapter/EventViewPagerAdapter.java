package letsapps.com.letscube.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import letsapps.com.letscube.R;
import letsapps.com.letscube.activity.TimerActivity;
import letsapps.com.letscube.listener.EventClickListener;
import letsapps.com.letscube.util.cube.CubeEvents;
import letsapps.com.letscube.util.cube.Event;
import letsapps.com.letscube.view.dialog.AddEventDialogView;
import letsapps.com.letscube.view.dialog.EditEventDialogView;

public class EventViewPagerAdapter extends PagerAdapter {

    private Context context;
    private String[] tabsTitles;
    private ArrayList<Event> addedEvents;

    Event currentEvent;
    EventClickListener eventClickListener;
    ListView[] eventLists;
    EventListAdapter[] eventListAdapters;

    public EventViewPagerAdapter(Context context, String[] tabsTitles, Event currentEvent,
                                 ArrayList<Event> addedEvents,
                                 EventClickListener eventClickListener) {
        this.context = context;
        this.tabsTitles = tabsTitles;
        this.currentEvent = currentEvent;
        this.addedEvents = addedEvents;
        this.eventClickListener = eventClickListener;

        eventLists = new ListView[getCount()];
        eventListAdapters = new EventListAdapter[getCount()];
    }

    @Override
    public int getCount() {
        return tabsTitles.length;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        eventLists[position] = new ListView(context);

        Log.d("EVPA (instItem)", "currentEvent = " + currentEvent);
        if(position == 0){ // OFFICIAL EVENTS
            eventListAdapters[position] = new EventListAdapter(context, CubeEvents.EVENTS_OFFICIAL,
                    addedEvents, currentEvent);
        }else{ // UNOFFICIAL EVENTS
            eventListAdapters[position] = new EventListAdapter(context, CubeEvents.EVENTS_UNOFFICIAL,
                    addedEvents, currentEvent);
        }

        eventLists[position].setOnItemClickListener(clickItemList);
        eventLists[position].setOnItemLongClickListener(longClickItemList);

        eventLists[position].setAdapter(eventListAdapters[position]);

        collection.addView(eventLists[position]);
        return eventLists[position];
    }

    private AdapterView.OnItemClickListener clickItemList = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            // Si l'item est add_event, alors lance la dialog sur l'ajout d'event
            if((int)id == CubeEvents.EVENTS_ADD.getId()){
                final AddEventDialogView dialogView = new AddEventDialogView(context);

                final AlertDialog.Builder addEventDialog = new AlertDialog.Builder(context);
                addEventDialog.setView(dialogView);
                addEventDialog.setNegativeButton(R.string.cancel, null);
                addEventDialog.setPositiveButton(R.string.dialog_add_event_add,
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((TimerActivity) context).addEvent(dialogView.getEnteredEvent());
                        refreshListsData();
                        eventLists[1].setSelection(eventListAdapters[1].getCount());
                    }
                });
                addEventDialog.show();

            // sinon lance l'event sélectionné
            }else {

                Event clickedEvent = CubeEvents.getEvent(context, (int) id);

                if (eventClickListener != null) {
                    eventClickListener.onEventClick(clickedEvent, true);
                }
                eventListAdapters[0].setCurrentEvent(clickedEvent);
                eventListAdapters[1].setCurrentEvent(clickedEvent);
            }
        }
    };

    private AdapterView.OnItemLongClickListener longClickItemList =
            new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view,
                                       int position, final long id) {

            // Ne fonctionne que sur les events ajouté par l'utilisateurs
            if(CubeEvents.isAddedEvent((int)id)){

                // Récupère l'event concerné
                Event clickedEventTemp = null;
                for(Event event : addedEvents){
                    if(event.getId() == (int)id){
                        clickedEventTemp = event;
                        break;
                    }
                }
                final Event clickedEvent = clickedEventTemp;

                // initialize la boîte de dialogue editEvent
                final EditEventDialogView dialogView = new EditEventDialogView(context, clickedEvent);

                final AlertDialog.Builder editEventDialog = new AlertDialog.Builder(context);
                editEventDialog.setView(dialogView);
                editEventDialog.setNegativeButton(R.string.dialog_edit_event_save,
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((TimerActivity) context).updateEvent(dialogView.getEvent());
                        currentEvent = dialogView.getEvent();
                        refreshListsData();
                        eventLists[1].setSelection(addedEvents.indexOf(dialogView.getEvent()));
                    }
                });
                editEventDialog.setPositiveButton(R.string.dialog_edit_event_delete,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                ((TimerActivity) context).deleteEvent(clickedEvent);

                                currentEvent = CubeEvents.EITAN_TWIST;

                                if (eventClickListener != null) {
                                    eventClickListener.onEventClick(currentEvent, false);
                                }

                                refreshListsData();
                                eventLists[1].setSelection(eventListAdapters[1].getCount());

                            }
                        });
                editEventDialog.show();
            }

            return true;
        }
    };

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabsTitles[position];
    }

    private void refreshListsData(){
        eventListAdapters[0] = new EventListAdapter(context,
                CubeEvents.EVENTS_OFFICIAL,
                addedEvents, currentEvent);
        eventListAdapters[1] = new EventListAdapter(context,
                CubeEvents.EVENTS_UNOFFICIAL,
                addedEvents, currentEvent);
        eventListAdapters[0].setCurrentEvent(currentEvent);
        eventListAdapters[1].setCurrentEvent(currentEvent);
        eventLists[0].setAdapter(eventListAdapters[0]);
        eventLists[1].setAdapter(eventListAdapters[1]);
    }
}
