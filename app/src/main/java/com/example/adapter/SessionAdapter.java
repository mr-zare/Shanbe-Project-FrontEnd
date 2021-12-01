package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.entity.Session;
import com.example.entity.Task;
import com.example.myapplication.R;

import java.util.List;
import java.util.Locale;

public class SessionAdapter extends BaseAdapter {

    private Context context;
    private List<Session> list;

    public SessionAdapter(Context context, List<Session> list) {
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
            view = LayoutInflater.from(context).inflate(R.layout.session_item,null);
        }
        Session currentSession = list.get(i);

        TextView limit = view.findViewById(R.id.session_limit);
        TextView date = view.findViewById(R.id.date);
        TextView time = view.findViewById(R.id.time);
        Button delete = view.findViewById(R.id.deleteBtn);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.remove(i);
                notifyDataSetChanged();
            }
        });

//        int yearIndex = 0;
//        int monthIndex = 0;
//        int dayIndex = 0;
//        int hourIndex = 0;
//        int minIndex = 0;

//        String[] yearArray = context.getResources().getStringArray(R.array.year);
//        String[] monthArray = context.getResources().getStringArray(R.array.year);
//        String[] dayArray = context.getResources().getStringArray(R.array.year);
//        String[] hourArray = context.getResources().getStringArray(R.array.year);
//        String[] minArray = context.getResources().getStringArray(R.array.year);
//
//        yearIndex = SearchIndex(currentSession.getYear().toString(),yearArray);
//        monthIndex = SearchIndex(currentSession.getMonth().toString(),monthArray);
//        dayIndex = SearchIndex(currentSession.getDay().toString(),dayArray);
//        hourIndex = SearchIndex(currentSession.getHour().toString(),hourArray);
//        minIndex = SearchIndex(currentSession.getMin().toString(),minArray);

        limit.setText(currentSession.getLimit());

        String year = currentSession.getYear().toString();
        String month = currentSession.getMonth().toString();
        String day = currentSession.getDay().toString();
        String dateStr = year+"/"+month+"/"+day;

        String hour = currentSession.getHour().toString();
        String min = currentSession.getMin().toString();
        String timeStr = hour+":"+min;

        date.setText(dateStr);
        time.setText(timeStr);
//        year.setSelection(yearIndex);
//        month.setSelection(monthIndex);
//        day.setSelection(dayIndex);
//        hour.setSelection(hourIndex);
//        min.setSelection(minIndex);

        return view;
    }


//    public int SearchIndex(String str,String [] array)
//    {
//        int index = 0;
//        for(int i=0;i< array.length;i++)
//        {
//            if(str.equals(array[i]))
//            {
//                index =  i;
//                break;
//            }
//        }
//        return index;
//    }
}
