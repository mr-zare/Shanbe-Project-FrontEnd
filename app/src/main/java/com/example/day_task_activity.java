package com.example;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.BroadCast.AlarmController;
import com.example.DataBase.tasksDB;
import com.example.adapter.ReservedSessionAdapter;
import com.example.adapter.taskAdapter;
import com.example.entity.Session;
import com.example.entity.Task;
import com.example.myapplication.R;
import com.example.webService.EventAPI;
import com.example.webService.TaskAPI;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class day_task_activity extends AppCompatActivity implements LocationListener {

    String userToken;
    String username;
    EditText searchEditText;
    ListView list;
    taskAdapter tasksAdap;
    TaskAPI taskAPI;
    String FinalDate;
    ListView sessionsListView;
    private ShimmerFrameLayout mFrameLayout;
    LocationManager locationManager;
    ReservedSessionAdapter reservedSessionAdapter;

    @Override
    protected void onResume() {
        super.onResume();
        mFrameLayout.startShimmer();
        fillList();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 1f, this);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_task);
        getSupportActionBar().hide();

        init();
        locationManager = (LocationManager) getSystemService(Service.LOCATION_SERVICE);

        mFrameLayout = findViewById(R.id.shimmerLayout);

        //active alarms..
        AlarmController alarm = new AlarmController(day_task_activity.this);
        alarm.StartAlarm();

        String date = getIntent().getStringExtra("Date").toString();
        String[] dateInfo = date.split(" ");
        String month = dateInfo[1];
        String day = dateInfo[2];
        String year = dateInfo[5];

        String monthNum = "00";

        if (month.equals("Jan")) {
            monthNum = "00";
        } else if (month.equals("Feb")) {
            monthNum = "01";
        } else if (month.equals("Mar")) {
            monthNum = "02";
        } else if (month.equals("Apr")) {
            monthNum = "03";
        } else if (month.equals("May")) {
            monthNum = "04";
        } else if (month.equals("June")) {
            monthNum = "05";
        } else if (month.equals("July")) {
            monthNum = "06";
        } else if (month.equals("Aug")) {
            monthNum = "07";
        } else if (month.equals("Sept")) {
            monthNum = "08";
        } else if (month.equals("Oct")) {
            monthNum = "09";
        } else if (month.equals("Nov")) {
            monthNum = "10";
        } else if (month.equals("Dec")) {
            monthNum = "11";
        }

        FinalDate = year + "-" + monthNum + "-" + day;
        //Toast.makeText(this, FinalDate, Toast.LENGTH_SHORT).show();
        fillList();


        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tasksAdap.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    void init() {
        searchEditText = findViewById(R.id.searchEditText);
        list = findViewById(R.id.tasksLists);
        SharedPreferences sharedPreferences = getSharedPreferences("authentication", MODE_PRIVATE);
        userToken = sharedPreferences.getString("token", "");
        username = sharedPreferences.getString("username", "");
        ConstraintLayout eventsLayout = findViewById(R.id.eventsContainer);
        LinearLayout.LayoutParams eventLayoutParams = (LinearLayout.LayoutParams) eventsLayout.getLayoutParams();
        eventLayoutParams.height = 0;
        sessionsListView = findViewById(R.id.eventsList);
    }


    public void addTaskBtn(View view) {
        Intent intent = new Intent(day_task_activity.this, AddTask.class);
        intent.putExtra("date", FinalDate);
        startActivity(intent);
    }


    public void fillList() {
//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
//
//        Retrofit createTask = new Retrofit.Builder()
//                .baseUrl(TaskAPI.BASE_URL)
//                .client(client)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        taskAPI = createTask.create(TaskAPI.class);


        //todo
//        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("task_token", userToken);
//        jsonObject.addProperty("alarm_check", FinalDate);
//        Call<List<Task>> request = taskAPI.getTasksDay("token "+userToken,jsonObject);
//
//        request.enqueue(new Callback<List<Task>>() {
//            @Override
//            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
//                if(response.isSuccessful()==false)
//                {
//                    CustomeAlertDialog getTasksDayError = new CustomeAlertDialog(day_task_activity.this,"Error","there is a problem with your internet connection");
//                }
//                else{
//                    int responseCode = response.code();
//                    //Toast.makeText(day_task_activity.this, Integer.toString(responseCode), Toast.LENGTH_SHORT).show();
//                    List<Task> listOfTasks = response.body();
//                    tasksAdap = new taskAdapter(day_task_activity.this,listOfTasks);
//                    list.setAdapter(tasksAdap);
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Task>> call, Throwable t) {
//                CustomeAlertDialog getTasksDayError = new CustomeAlertDialog(day_task_activity.this,"Error","there is a problem with your internet connection");
//            }
//        });

        //offline part ....
        EventAPI eventAPI;

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit createTask = new Retrofit.Builder()
                .baseUrl(EventAPI.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        eventAPI = createTask.create(EventAPI.class);


        tasksDB tasksdb = new tasksDB(day_task_activity.this);
        List<Task> listOfTasks = tasksdb.select(FinalDate);
        tasksAdap = new taskAdapter(day_task_activity.this, listOfTasks);
        list.setAdapter(tasksAdap);
        mFrameLayout.startShimmer();
        mFrameLayout.setVisibility(View.GONE);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user_token", userToken);
        jsonObject.addProperty("time", FinalDate);

        Call<List<Session>> request = eventAPI.session_get_day("token " + userToken, jsonObject);
        request.enqueue(new Callback<List<Session>>() {
            @Override
            public void onResponse(Call<List<Session>> call, Response<List<Session>> response) {
                if (!response.isSuccessful()) {
                    CustomErrorAlertDialog internetConnection = new CustomErrorAlertDialog(day_task_activity.this, "Error", "We could'nt get your events , please check your connection and try again");
                }
                List<Session> listOfSessions = response.body();
                reservedSessionAdapter = new ReservedSessionAdapter(day_task_activity.this, listOfSessions);

                sessionsListView.setAdapter(reservedSessionAdapter);
                mFrameLayout.startShimmer();
                mFrameLayout.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<Session>> call, Throwable t) {
                CustomErrorAlertDialog internetConnection = new CustomErrorAlertDialog(day_task_activity.this, "Error", "We could'nt get your events , please check your connection and try again");
            }
        });

    }

    @Override
    protected void onPause() {
        mFrameLayout.stopShimmer();
        super.onPause();
        locationManager.removeUpdates(this);
    }

    public void toggle(View view) {
        ConstraintLayout tasks = findViewById(R.id.tasksContainer);
        ConstraintLayout events = findViewById(R.id.eventsContainer);
        LinearLayout.LayoutParams tasksParams = (LinearLayout.LayoutParams) tasks.getLayoutParams();
        LinearLayout.LayoutParams eventParams = (LinearLayout.LayoutParams) events.getLayoutParams();
        if (tasks.getVisibility() == View.VISIBLE) {
            events.setVisibility(View.VISIBLE);
            tasks.setVisibility(View.INVISIBLE);
            tasksParams.height = 1;
            eventParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        } else {
            events.setVisibility(View.INVISIBLE);
            tasks.setVisibility(View.VISIBLE);
            eventParams.height = 1;
            tasksParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        }
        tasks.setLayoutParams(tasksParams);
        events.setLayoutParams((eventParams));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0 && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.GET_RECEIVERS) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 1f, this);
            }
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

        double lat = location.getLatitude();
        double lnt = location.getLongitude();

        Geocoder geoCoder = new Geocoder(this, Locale.getDefault()); //it is Geocoder
        StringBuilder builder = new StringBuilder();
        try {
            List<Address> address = geoCoder.getFromLocation(lat, lnt, 1);
            int maxLines = address.get(0).getMaxAddressLineIndex();
            for (int i=0; i<maxLines; i++) {
                String addressStr = address.get(0).getAddressLine(i);
                builder.append(addressStr);
                builder.append(" ");
            }

            String fnialAddress = builder.toString();
            Toast.makeText(this, fnialAddress, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {}
        catch (NullPointerException e) {}
    }
}