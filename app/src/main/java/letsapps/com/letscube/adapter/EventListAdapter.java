package letsapps.com.letscube.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import letsapps.com.letscube.R;
import letsapps.com.letscube.util.DatabaseTime;
import letsapps.com.letscube.util.cube.CubeEvents;
import letsapps.com.letscube.util.cube.Event;

public class EventListAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;

    Event[] events;
    Event currentEvent;

    ArrayList<Event> addedEvents;

    DatabaseTime[] timesToHighlight;

    public EventListAdapter(Context context, boolean isOfficialList,
                            ArrayList<Event> allAddedEvents, Event currentEvent){
        this.context = context;
        this.events = CubeEvents.getEvents(isOfficialList);
        this.currentEvent = currentEvent;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        timesToHighlight = new DatabaseTime[0];

        addedEvents = new ArrayList<>();

        if(allAddedEvents != null) {
            for (Event event : allAddedEvents) {
                if (event.isOfficial() == isOfficialList) {
                    addedEvents.add(event);
                }
            }
        }
    }

    @Override
    public int getCount() {
        return events.length + addedEvents.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        if(position < events.length){
            return events[position].getId();
        }else{
            return addedEvents.get(position - events.length).getId();
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.item_list_events, parent, false);

        if(position < events.length){
            final Event event = events[position];

            ((TextView) convertView.findViewById(R.id.event_name))
                    .setText(event.getName(context));
            ((ImageView)convertView.findViewById(R.id.event_picture))
                    .setImageResource(event.getPictureResId());

            if(currentEvent.getId() == event.getId()){
                convertView.setBackgroundColor(context.getResources().getColor(R.color.list_highlight));
            }
        }else{
            final Event event = addedEvents.get(position - events.length);

            ((TextView) convertView.findViewById(R.id.event_name))
                    .setText(event.getName());
            ((ImageView)convertView.findViewById(R.id.event_picture))
                    .setImageResource(R.drawable.ic_cube_mystery);

            if(currentEvent.getId() == event.getId()){
                convertView.setBackgroundColor(context.getResources().getColor(R.color.list_highlight));
            }
        }

        return convertView;
    }

    public void setCurrentEvent(Event currentEvent){
        this.currentEvent = currentEvent;
        notifyDataSetChanged();
    }
}