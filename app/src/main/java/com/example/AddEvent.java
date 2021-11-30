package com.example;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.entity.Event;
import com.example.myapplication.R;
import com.example.webService.EventAPI;
import com.example.webService.TaskAPI;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddEvent  extends AppCompatActivity {

    Spinner category;
    EditText title;
    Spinner privacy;
    Spinner location;
    EditText description;
    Button addEvent;

    EventAPI eventAPI;

    String titleStr;
    String categoryStr;
    String locationStr;
    String privacyStr;
    String descriptionStr;
    String userToken;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        getSupportActionBar().hide();
        init();
    }

    public void init()
    {
        title = findViewById(R.id.titleevent);
        category = findViewById(R.id.categoryevent);
        location = findViewById(R.id.locationevent);
        privacy = findViewById(R.id.privacyevent);
        description = findViewById(R.id.descript);
        addEvent = findViewById(R.id.addEventButton);

        SharedPreferences sharedPreferences = getSharedPreferences("authentication", MODE_PRIVATE);
        userToken = sharedPreferences.getString("token", "");

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit createTask = new Retrofit.Builder()
                .baseUrl(TaskAPI.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        eventAPI = createTask.create(EventAPI.class);

    }

    public void AddEvent(View view) {
        titleStr = title.getText().toString();
        categoryStr = category.getSelectedItem().toString();
        locationStr = location.getSelectedItem().toString();
        privacyStr = privacy.getSelectedItem().toString();
        descriptionStr = description.getText().toString();

        boolean valid = true;
        if(titleStr.equals(""))
        {
            CustomeAlertDialog titleEmpyt = new CustomeAlertDialog(this,"Error","please fill the title field");
            title.setBackgroundResource(R.drawable.border_event_error);
            valid = false;
        }
        if(categoryStr.equals(""))
        {
            CustomeAlertDialog categortyEmpyt = new CustomeAlertDialog(this,"Error","please fill the category field");
            category.setBackgroundResource(R.drawable.border_event_error);
            valid = false;
        }
        if(locationStr.equals(""))
        {
            CustomeAlertDialog locationEmpyt = new CustomeAlertDialog(this,"Error","please fill the location field");
            location.setBackgroundResource(R.drawable.border_event_error);
            valid = false;
        }
        if(privacyStr.equals(""))
        {
            CustomeAlertDialog privacyEmpyt = new CustomeAlertDialog(this,"Error","please fill the privacy field");
            privacy.setBackgroundResource(R.drawable.border_event_error);
            valid = false;
        }
        if(descriptionStr.equals(""))
        {
            CustomeAlertDialog privacyEmpyt = new CustomeAlertDialog(this,"Error","please fill the descrption field");
            description.setBackgroundResource(R.drawable.border_event_error);
            valid = false;
        }

        if(valid)
        {
            String session = "20_2021-12-14_18:30";
            ArrayList<String> sessions = new ArrayList<>();
            sessions.add(session);
            boolean pv = false;
            if(privacyStr.equals("Public"))
            {
                pv = false;
            }
            else if(privacyStr.equals("Private"))
            {
                pv = true;
            }
            Event newEvent = new Event(userToken, titleStr,pv, categoryStr, descriptionStr, false, locationStr, sessions);
            Call<Event> callBack = eventAPI.event_create("token "+userToken,newEvent);
            callBack.enqueue(new Callback<Event>() {
                @Override
                public void onResponse(Call<Event> call, Response<Event> response) {
                    if(!response.isSuccessful())
                    {
                        CustomeAlertDialog errorConnecting = new CustomeAlertDialog(AddEvent.this,"error","there is a problem connecting to server");
                    }
                    else{
                        String code = Integer.toString(response.code());
                        Event addedEvent = response.body();
                        Toast.makeText(AddEvent.this, code, Toast.LENGTH_SHORT).show();
                        CustomeAlertDialog saved = new CustomeAlertDialog(AddEvent.this,"Successful","event saved");
                        saved.btnOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                finish();
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<Event> call, Throwable t) {
                    CustomeAlertDialog errorConnecting = new CustomeAlertDialog(AddEvent.this,"Error","there is a problem connecting to server");
                }
            });
        }
    }
}
