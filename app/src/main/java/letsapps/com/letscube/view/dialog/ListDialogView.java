package letsapps.com.letscube.view.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

import letsapps.com.letscube.R;
import letsapps.com.letscube.listener.EventClickListener;
import letsapps.com.letscube.util.cube.Event;

public class ListDialogView extends LinearLayout {

    private ViewDialogTitle titleV;
    private ListView listV;

    private BaseAdapter listAdapter;

    public ListDialogView(Context context, int titleResId, BaseAdapter listAdapter) {
        super(context);

        final LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rootView = inflater.inflate(R.layout.dialog_list, this);

        titleV = (ViewDialogTitle)rootView.findViewById(R.id.title);
        listV = (ListView)rootView.findViewById(R.id.list);

        this.listAdapter = listAdapter;

        titleV.setTitle(context.getString(titleResId));
        listV.setAdapter(listAdapter);

    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener clickItemList){
        listV.setOnItemClickListener(clickItemList);
    }
}