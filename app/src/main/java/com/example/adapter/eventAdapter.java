package com.example.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.entity.Event;
import com.example.myapplication.R;

import java.util.List;

public class eventAdapter extends BaseAdapter {
    private Context context;
    private List<Event> list;

    public eventAdapter(Context context, List<Event> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null)
        {
            view = LayoutInflater.from(context).inflate(R.layout.event_view,null);
        }

        Event currentEvent = list.get(i);

        TextView title = view.findViewById(R.id.titleEventView);
        TextView date = view.findViewById(R.id.dateTimeEventView);
        TextView location = view.findViewById(R.id.LocationEventView);
        TextView desc = view.findViewById(R.id.descEventView);
        Button joinBtn = view.findViewById(R.id.joinBtn);

        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(context,);
                //todo
            }
        });

        return view;
    }
}
