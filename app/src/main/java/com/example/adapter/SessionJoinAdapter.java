package com.example.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.entity.Session;
import com.example.entity.Task;
import com.example.myapplication.R;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

public class SessionJoinAdapter extends BaseAdapter {

    private Context context;
    private List<Session> list;
    private List<Session> list_selected;

    public SessionJoinAdapter(Context context, List<Session> list) {
        this.context = context;
        this.list = list;
        list_selected = new List<Session>() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean contains(@Nullable Object o) {
                return false;
            }

            @NonNull
            @Override
            public Iterator<Session> iterator() {
                return null;
            }

            @NonNull
            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @NonNull
            @Override
            public <T> T[] toArray(@NonNull T[] a) {
                return null;
            }

            @Override
            public boolean add(Session session) {
                return false;
            }

            @Override
            public boolean remove(@Nullable Object o) {
                return false;
            }

            @Override
            public boolean containsAll(@NonNull Collection<?> c) {
                return false;
            }

            @Override
            public boolean addAll(@NonNull Collection<? extends Session> c) {
                return false;
            }

            @Override
            public boolean addAll(int index, @NonNull Collection<? extends Session> c) {
                return false;
            }

            @Override
            public boolean removeAll(@NonNull Collection<?> c) {
                return false;
            }

            @Override
            public boolean retainAll(@NonNull Collection<?> c) {
                return false;
            }

            @Override
            public void clear() {

            }

            @Override
            public Session get(int index) {
                return null;
            }

            @Override
            public Session set(int index, Session element) {
                return null;
            }

            @Override
            public void add(int index, Session element) {

            }

            @Override
            public Session remove(int index) {
                return null;
            }

            @Override
            public int indexOf(@Nullable Object o) {
                return 0;
            }

            @Override
            public int lastIndexOf(@Nullable Object o) {
                return 0;
            }

            @NonNull
            @Override
            public ListIterator<Session> listIterator() {
                return null;
            }

            @NonNull
            @Override
            public ListIterator<Session> listIterator(int index) {
                return null;
            }

            @NonNull
            @Override
            public List<Session> subList(int fromIndex, int toIndex) {
                return null;
            }
        };
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
            view = LayoutInflater.from(context).inflate(R.layout.sessionjoin_item,null);
        }
        Session currentSession = list.get(i);

        TextView limit = view.findViewById(R.id.limitJoin);
        TextView date = view.findViewById(R.id.dateJoin);
        TextView time = view.findViewById(R.id.timeJoin);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!list_selected.contains(currentSession)) {
                    view.setBackgroundTintList(context.getResources().getColorStateList(R.color.recycler_background));
                    list_selected.add(currentSession);
                    notifyDataSetChanged();
                }
                else{
                    view.setBackgroundTintList(context.getResources().getColorStateList(R.color.session_join_item));
                    Log.i("BARGASH","RANG");
                    list_selected.remove(currentSession);
                    notifyDataSetChanged();
                }
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

        limit.setText(String.valueOf(currentSession.getLimit()));

        String dateTime = currentSession.getTime();
        String [] Temp = dateTime.split("_");
        date.setText(Temp[0]);
        time.setText(Temp[1]);
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
