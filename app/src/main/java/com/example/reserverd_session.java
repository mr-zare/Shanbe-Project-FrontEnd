package com.example;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.example.DataBase.tasksDB;
import com.example.adapter.SessionJoinAdapter;
import com.example.adapter.SessionMembersAdapter;
import com.example.entity.Session;
import com.example.entity.User;
import com.example.myapplication.R;
import com.example.webService.EventAPI;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class reserverd_session extends AppCompatActivity {
    Bundle extras;
    TextView title,category , location , desc , date , time , limit ,address,link;
    String titleStr,categoryStr , locationStr , descStr , dateStr , timeStr , limitStr , dateTimeStr ,sessionTokenStr,addressStr,linkStr;
    Button delete;
    String userToken;
    EventAPI eventAPI;
    List<Session> sessionList;
    SessionMembersAdapter sessionAdap;
    ListView session_users;
    CustomLoadingDialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_reserverd_session);

        init();
    }

    public void init()
    {
        titleStr = getIntent().getStringExtra("title");
        categoryStr = getIntent().getStringExtra("category");
        locationStr = getIntent().getStringExtra("location");
        descStr = getIntent().getStringExtra("desc");
        dateTimeStr = getIntent().getStringExtra("datetime");
        addressStr = getIntent().getStringExtra("address");
        linkStr = getIntent().getStringExtra("link");
        session_users = findViewById(R.id.sessionsMembers);
//        String [] dateInfo = dateTimeStr.split("-");
//        String month = dateInfo [1];
//        int monthNum = Integer.parseInt(month);
//        monthNum++;
//        month = Integer.toString(monthNum);
//        dateTimeStr = dateInfo[0]+"-"+month+"-"+dateInfo[2];
        sessionTokenStr= getIntent().getStringExtra("session_token");
        limitStr = getIntent().getStringExtra("limit");

        String [] dateTimeInfo = dateTimeStr.split("_");
        dateStr = dateTimeInfo[0];
        timeStr = dateTimeInfo[1];

        title = findViewById(R.id.titleevent);
        category = findViewById(R.id.categoryevent);
        location = findViewById(R.id.locationevent);
        desc = findViewById(R.id.descript);
        date = findViewById(R.id.dateJoin);
        time = findViewById(R.id.timeJoin);
        limit = findViewById(R.id.limitJoin);
        delete = findViewById(R.id.delete);
        address = findViewById(R.id.addressEvent);
        link = findViewById(R.id.linkEvent);

        desc.setScroller(new Scroller(reserverd_session.this));
        desc.setMaxLines(1);
        desc.setVerticalScrollBarEnabled(true);
        desc.setMovementMethod(new ScrollingMovementMethod());

        SharedPreferences sharedPreferences = getSharedPreferences("authentication", Context.MODE_PRIVATE);
        userToken = sharedPreferences.getString("token", "");

        fill();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        Retrofit createTask = new Retrofit.Builder()
                .baseUrl(EventAPI.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        eventAPI = createTask.create(EventAPI.class);
        extras = getIntent().getExtras();
        SharedPreferences shP = getSharedPreferences("userInformation", MODE_PRIVATE);
        String token2 = shP.getString("token", "");
        JsonObject body2 = new JsonObject();
        body2.addProperty("session_token", extras.getString("session_token"));
        Call<List<User>> callBack2 = eventAPI.session_users("token " + token2, body2);
        if(callBack2 != null)
        {
            callBack2.enqueue(new Callback<List<User>>() {
                @Override
                public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                    if(response.errorBody() != null)
                    {
                        loadingDialog.dismisDialog();
                    }
                    if (!response.isSuccessful()) {
                        // Toast.makeText(JoinEvent.this, "Some Field Wrong", Toast.LENGTH_SHORT).show();
                    } else {
                        String code = Integer.toString(response.code());
                        List<User> sessionusers = response.body();
                        sessionAdap = new SessionMembersAdapter(reserverd_session.this, sessionusers, sessionusers);
                        session_users.setAdapter(sessionAdap);
                        justifyListViewHeightBasedOnChildren(session_users);
                        loadingDialog.dismisDialog();
                    }
                }
                @Override
                public void onFailure(Call<List<User>> call, Throwable t) {
                    loadingDialog.dismisDialog();
                    // Toast.makeText(JoinEvent.this, "error is :" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    public static void justifyListViewHeightBasedOnChildren (ListView listView) {

        ListAdapter adapter = listView.getAdapter();

        if (adapter == null) {
            return;
        }
        ViewGroup vg = listView;
        int totalHeight = 60;
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
    public void fill()
    {
        title.setText(titleStr);
        category.setText(categoryStr);
        location.setText(locationStr);
        desc.setText(descStr);
        date.setText(dateStr);
        time.setText(timeStr);
        limit.setText(limitStr);
        address.setText(addressStr);
        link.setText(linkStr);
    }
    public void openLoadingDialog()
    {
        loadingDialog = new CustomLoadingDialog(reserverd_session.this);
        loadingDialog.startLoadingDialog();
    }
    @Override
    protected void onResume() {
        super.onResume();
        openLoadingDialog();
    }


    public void session_cancel(View view) {
        ConfirmationAlertDialog confirmCancel = new ConfirmationAlertDialog(reserverd_session.this,"Confirmation","Do you want to cancel this session?");
        confirmCancel.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmCancel.alertDialog.dismiss();
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

                Retrofit cancelSession = new Retrofit.Builder()
                        .baseUrl(EventAPI.BASE_URL)
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                eventAPI = cancelSession.create(EventAPI.class);
                JsonObject sessionToken = new JsonObject();
                sessionToken.addProperty("session_token",sessionTokenStr);
                Call<JsonObject> callBack = eventAPI.session_cancel("token "+userToken,sessionToken);
                callBack.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if(!response.isSuccessful())
                        {
                            CustomErrorAlertDialog getTasksDayError = new CustomErrorAlertDialog(reserverd_session.this,"Error","there is a problem with your internet connection");
                        }
                        else{
                            // Toast.makeText(reserverd_session.this, Integer.toString(response.code()), Toast.LENGTH_SHORT).show();
                            CustomSuccessAlertDialog getTasksDayError = new CustomSuccessAlertDialog(reserverd_session.this,"Successful","session canceled");
                            getTasksDayError.btnOk.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    getTasksDayError.alertDialog.dismiss();
                                    Intent event = new Intent(reserverd_session.this, my_sessions.class);
                                    startActivity(event);
                                    finish();
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        CustomErrorAlertDialog getTasksDayError = new CustomErrorAlertDialog(reserverd_session.this,"Error","there is a problem with your internet connection");
                    }
                });
            }
        });

        confirmCancel.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmCancel.alertDialog.dismiss();
            }
        });
    }
}