package com.example.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.AddEvent;
import com.example.CustomErrorAlertDialog;
import com.example.CustomeAlertDialog;
import com.example.entity.Session;
import com.example.entity.Task;
import com.example.entity.User;
import com.example.myapplication.R;
import com.example.reserverd_session;
import com.example.webService.EventAPI;
import com.example.webService.TaskAPI;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SessionAdapter extends BaseAdapter {

    private Context context;
    private List<Session> list;
    private List<Session> added;
    Bundle extras;
    EventAPI eventAPI;
    String userToken;
    SessionMembersAdapter sessionAdap;

    public SessionAdapter(Context context, List<Session> list,List<Session> added) {
        this.context = context;
        this.list = list;
        this.added = added;
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
        ListView members = view.findViewById(R.id.members);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        Retrofit createTask = new Retrofit.Builder()
                .baseUrl(EventAPI.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        eventAPI = createTask.create(EventAPI.class);
        SharedPreferences shP = context.getSharedPreferences("userInformation", context.MODE_PRIVATE);
        String token2 = shP.getString("token", "");
        JsonObject body2 = new JsonObject();
        body2.addProperty("session_token", currentSession.getSession_token());
        Call<List<User>> callBack2 = eventAPI.session_users("token " + token2, body2);
        callBack2.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (!response.isSuccessful()) {
                    // Toast.makeText(JoinEvent.this, "Some Field Wrong", Toast.LENGTH_SHORT).show();
                } else {
                    String code = Integer.toString(response.code());
                    List<User> sessionusers = response.body();
                    sessionAdap = new SessionMembersAdapter(context, sessionusers, sessionusers);
                    members.setAdapter(sessionAdap);
                    justifyListViewHeightBasedOnChildren(members);
                }
            }
            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                // Toast.makeText(JoinEvent.this, "error is :" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        Button delete=view.findViewById(R.id.deletese);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(added.size()==list.size())
                {
                    list.remove(i);
                    notifyDataSetChanged();
                }
                else{
                    if(added.contains(currentSession))
                    {
                        list.remove(currentSession);
                        added.remove(currentSession);
                        notifyDataSetChanged();
                    }
                    else{
                        SharedPreferences sharedPreferences = context.getSharedPreferences("authentication", Context.MODE_PRIVATE);
                        userToken = sharedPreferences.getString("token", "");

                        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

                        Retrofit sessionDelete = new Retrofit.Builder()
                                .baseUrl(EventAPI.BASE_URL)
                                .client(client)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                        eventAPI = sessionDelete.create(EventAPI.class);

                        JsonObject deleteSessionJson = new JsonObject();
                        deleteSessionJson.addProperty("session_token",currentSession.getSession_token());
                        Call<JsonObject> callBack = eventAPI.session_delete("token "+userToken,deleteSessionJson);
                        callBack.enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                if(!response.isSuccessful())
                                {
                                    CustomErrorAlertDialog errorConnecting = new CustomErrorAlertDialog(context,"Error","there is a problem connecting to server");
                                }
                                else{
                                    list.remove(i);
                                    notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {
                                CustomErrorAlertDialog errorConnecting = new CustomErrorAlertDialog(context,"Error","there is a problem connecting to server");
                            }
                        });
                    }

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
    public static void justifyListViewHeightBasedOnChildren (ListView listView) {

        ListAdapter adapter = listView.getAdapter();

        if (adapter == null) {
            return;
        }
        ViewGroup vg = listView;
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, vg);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams par = listView.getLayoutParams();
        par.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(par);
        listView.requestLayout();
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
