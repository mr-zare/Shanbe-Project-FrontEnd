package com.example;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.webService.EventAPI;
import com.example.webService.TaskAPI;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
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
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
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

        titleStr = title.toString();
        categoryStr = category.toString();
        locationStr = location.toString();
        privacyStr = privacy.toString();
        descriptionStr = description.toString();

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

        }
    }
}
