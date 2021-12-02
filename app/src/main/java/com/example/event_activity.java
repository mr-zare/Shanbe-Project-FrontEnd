package com.example;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.adapter.eventAdapter;
import com.example.adapter.taskAdapter;
import com.example.entity.Event;
import com.example.entity.Task;
import com.example.myapplication.R;
import com.example.webService.EventAPI;
import com.example.webService.TaskAPI;
import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class event_activity extends AppCompatActivity {

    EventAPI eventAPI;
    EditText search;
    String userToken;
    Button filterBtn;
    eventAdapter eventAdap;
    ListView eventsListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_event);
        init();
        fillList();


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                eventAdap.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void goToAddEvent(View view) {
        Intent intent = new Intent(event_activity.this,AddEvent.class);
        startActivity(intent);
        finish();
    }

    public void init()
    {
        search = findViewById(R.id.searchEventEditText);
        filterBtn = findViewById(R.id.filterBtn);
        SharedPreferences sharedPreferences = getSharedPreferences("authentication", MODE_PRIVATE);
        userToken = sharedPreferences.getString("token", "");
        eventsListView = findViewById(R.id.eventsList);
    }
    public void fillList()
    {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit getEvent = new Retrofit.Builder()
                .baseUrl(TaskAPI.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        eventAPI = getEvent.create(EventAPI.class);



        Call<List<Event>> request = eventAPI.event_get("token "+userToken);

        request.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                if(response.isSuccessful()==false)
                {
                    CustomeAlertDialog getTasksDayError = new CustomeAlertDialog(event_activity.this,"Error","there is a problem with your internet connection");
                }
                else{
                    int responseCode = response.code();
                    //Toast.makeText(day_task_activity.this, Integer.toString(responseCode), Toast.LENGTH_SHORT).show();
                    List<Event> ListOfEvents = response.body();
                    eventAdap = new eventAdapter(event_activity.this,ListOfEvents);

                    eventsListView.setAdapter(eventAdap);

                }
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                CustomeAlertDialog getTasksDayError = new CustomeAlertDialog(event_activity.this,"Error","there is a problem with your internet connection");
            }
        });
    }
}