package com.example;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.media.JetPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.entity.Event;
import com.example.entity.User;
import com.example.myapplication.R;
import com.example.webService.EventAPI;
import com.example.webService.TaskAPI;
import com.google.gson.JsonObject;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class JoinEvent extends AppCompatActivity {
    Bundle extras;
    TextView title,location;
    Button joinButton;
    ImageView eventImage;
    EventAPI eventAPI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_join_event);
        getSupportActionBar().hide();
        extras = getIntent().getExtras();
        title = findViewById(R.id.TitleJoinEvnet);
        location = findViewById(R.id.LocationJoinEvent);
        joinButton = findViewById(R.id.joinJoinEvent);
        eventImage = findViewById(R.id.categoryJoinImageItemEventView);
        title.setText(extras.getString("title"));
        location.setText(extras.getString("location"));
        String category = extras.getString("category");
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit createTask = new Retrofit.Builder()
                .baseUrl(TaskAPI.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        eventAPI = createTask.create(EventAPI.class);

        if(category.equals("Sport") || category.equals("sports"))
        {
            eventImage.setImageResource(R.drawable.sport4);
        }
        else if(category.equals("Study")){
            eventImage.setImageResource(R.drawable.study1);
        }
        else if(category.equals("Meeting"))
        {
            eventImage.setImageResource(R.drawable.meeting1);
        }
        else if(category.equals("Work"))
        {
            eventImage.setImageResource(R.drawable.work1);
        }
        else if(category.equals("hang out"))
        {
            eventImage.setImageResource(R.drawable.hang_out2);
        }
        SharedPreferences shP = getSharedPreferences("userInformation", MODE_PRIVATE);
        String token = shP.getString("token", "");
        JsonObject body = new JsonObject();
        body.addProperty("event_token",extras.getString("token"));
        Call<Event> callBack = eventAPI.enter_event_token("token "+ token,body);
        callBack.enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                if(!response.isSuccessful())
                {
                    Toast.makeText(JoinEvent.this, "Some Field Wrong", Toast.LENGTH_SHORT).show();
                }
                else{
                    String code = Integer.toString(response.code());
                    Event event = response.body();
                    Log.i("RESID","RESID");
                }
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                Toast.makeText(JoinEvent.this, "error is :"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}