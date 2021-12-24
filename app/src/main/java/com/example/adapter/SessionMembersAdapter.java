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
import com.example.entity.User;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

public class SessionMembersAdapter extends BaseAdapter {

    private Context context;
    private boolean flag;
    private List<User> list;
    private User selected;
    private List<User> sessionUsers;
    View selected_view;

    public SessionMembersAdapter(Context context, List<User>users, List<User> list) {
        this.context = context;
        this.list = list;
        this.flag = false;
        this.sessionUsers = users;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    public User getSelected() {
        return selected;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null)
        {
            view = LayoutInflater.from(context).inflate(R.layout.session_members_layout,null);
        }
        User currentUser = list.get(i);

        TextView username = view.findViewById(R.id.username_m);
        TextView firstname = view.findViewById(R.id.firstName_m);
        TextView lastname = view.findViewById(R.id.lastName_m);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!flag){
                    selected = currentUser;
                    view.setBackgroundTintList(context.getResources().getColorStateList(R.color.recycler_background));
                    selected_view = view;
                    flag = true;
                }
                else{
                    selected = currentUser;
                    selected_view.setBackgroundTintList(context.getResources().getColorStateList(R.color.session_join_item));
                    selected_view = view;
                    view.setBackgroundTintList(context.getResources().getColorStateList(R.color.recycler_background));
                }
            }
        });

        lastname.setText(String.valueOf(currentUser.getLast_name()));
        firstname.setText(currentUser.getFirst_name());
        username.setText(currentUser.getUsername());
        return view;
    }
}
