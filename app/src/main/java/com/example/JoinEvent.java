package com.example;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.JetPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapter.SessionJoinAdapter;
import com.example.adapter.eventAdapter;
import com.example.entity.Event;
import com.example.entity.Session;
import com.example.entity.User;
import com.example.myapplication.R;
import com.example.webService.EventAPI;
import com.example.webService.TaskAPI;
import com.example.webService.UserAPI;
import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class JoinEvent extends AppCompatActivity {
    Bundle extras;
    TextView title, location , creator , link;
    Button joinButton;
    ImageView eventImage;
    EventAPI eventAPI;
    List<Session> sessionList;
    SessionJoinAdapter sessionAdap;
    ListView listView;
    Context mContext;
    CustomLoadingDialog loadingDialog;
    String creatorUsername,creatorFirstName,creatorLastName,creatorEmail,creatorPhoneNumber,creatorAvatar;
    UserAPI userAPI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_join_event);
        getSupportActionBar().hide();
        extras = getIntent().getExtras();
        title = findViewById(R.id.TitleJoinEvnet);
        creator = findViewById(R.id.usernamejoinevent);
        location = findViewById(R.id.LocationJoinEvent);
        link = findViewById(R.id.linkEvent);
        joinButton = findViewById(R.id.joinJoinEvent);
        eventImage = findViewById(R.id.categoryJoinImageItemEventView);
        listView = findViewById(R.id.sessionsJoinListView);
        title.setText(extras.getString("title"));
        location.setText(extras.getString("location"));
        link.setText(extras.getString("link"));
        String category = extras.getString("category");
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        mContext = this;

        creatorFirstName = "";
        creatorLastName = "";
        creatorEmail = "";
        creatorPhoneNumber = "";
        creatorAvatar = "";

        SharedPreferences shP = getSharedPreferences("userInformation", MODE_PRIVATE);
        String token = shP.getString("token", "");

        Retrofit createTask = new Retrofit.Builder()
                .baseUrl(TaskAPI.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        eventAPI = createTask.create(EventAPI.class);

        JsonObject body = new JsonObject();
        body.addProperty("event_token", extras.getString("token"));
        Call<Event> callBack = eventAPI.enter_event_token("token " + token, body);
        callBack.enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                if (!response.isSuccessful()) {
                    // Toast.makeText(JoinEvent.this, "Some Field Wrong", Toast.LENGTH_SHORT).show();
                } else {
                    String code = Integer.toString(response.code());
                    Event event = response.body();
                    sessionList = event.getSessionsArr();
                    sessionAdap = new SessionJoinAdapter(JoinEvent.this, sessionList);
                    location.setText("Address :       " +event.getAddress().toString());
                    link.setText("Link :       " +event.getLink().toString());
                    creator.setText(event.getUsername());
                    creatorUsername = event.getUsername();
                    listView.setAdapter(sessionAdap);
                    loadingDialog.dismisDialog();
                }
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                // Toast.makeText(JoinEvent.this, "error is :" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



        if (category.equals("Sport") || category.equals("sports")) {
            eventImage.setImageResource(R.drawable.sport4);
        } else if (category.equals("Study")) {
            eventImage.setImageResource(R.drawable.study1);
        } else if (category.equals("Meeting")) {
            eventImage.setImageResource(R.drawable.meeting1);
        } else if (category.equals("Work")) {
            eventImage.setImageResource(R.drawable.work1);
        } else if (category.equals("hang out")) {
            eventImage.setImageResource(R.drawable.hang_out2);
        }


        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionAdap != null) {
                    if (sessionAdap.getSelected().getSession_token() != "") {
                        if(sessionAdap.getSelected().getFilled() >= sessionAdap.getSelected().getLimit())
                        {
                            CustomErrorAlertDialog error = new CustomErrorAlertDialog(JoinEvent.this, "Error", "Choose another session. This session is full.");
                        }
                        else{
                            JsonObject session_item = new JsonObject();
                            session_item.addProperty("session_token", sessionAdap.getSelected().getSession_token());
                            Call<Session> callBack = eventAPI.join_session("token " + token, session_item);
                            callBack.enqueue(new Callback<Session>() {
                                @Override
                                public void onResponse(Call<Session> call, Response<Session> response) {
                                    if (!response.isSuccessful()) {
                                        if(response.code() == 400)
                                        {
                                            CustomErrorAlertDialog customErrorAlertDialog = new CustomErrorAlertDialog(JoinEvent.this,"Error","user already in session.");

                                        }
                                        else{
                                            CustomErrorAlertDialog customErrorAlertDialog = new CustomErrorAlertDialog(JoinEvent.this,"Error","there is a problem with your connection");

                                        }

                                        // Toast.makeText(JoinEvent.this, "Some Field Wrong", Toast.LENGTH_SHORT).show();
                                    } else {
                                        String code = Integer.toString(response.code());
                                        Session session = response.body();
                                        CustomSuccessAlertDialog saved = new CustomSuccessAlertDialog(JoinEvent.this,"Alert!","Successfully joined");
                                        saved.btnOk.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                saved.alertDialog.dismiss();
                                                finish();
                                            }
                                        });
                                        //  Toast.makeText(JoinEvent.this, response.message(), Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Session> call, Throwable t) {
                                    // Toast.makeText(JoinEvent.this, "error is :" + t.getMessage(), Toast.LENGTH_SHORT).show();
                                    CustomErrorAlertDialog customErrorAlertDialog = new CustomErrorAlertDialog(JoinEvent.this,"Error",t.getMessage());
                                }
                            });
                        }
                    }
                    else {
                        CustomErrorAlertDialog error = new CustomErrorAlertDialog(JoinEvent.this, "Error", "You should select a session");
                    }
                }
            }
        });

    }
    public void openLoadingDialog()
    {
        loadingDialog = new CustomLoadingDialog(JoinEvent.this);
        loadingDialog.startLoadingDialog();
    }
    @Override
    protected void onResume() {
        super.onResume();
        openLoadingDialog();
    }



    public void ViewCreatorProfile(View view) {
        profileAlertdialog profile_alertdialog = new profileAlertdialog(JoinEvent.this,creatorUsername);

    }


}