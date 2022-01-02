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
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.google.android.gms.tasks.Tasks;
import com.google.gson.JsonObject;
import com.ramijemli.percentagechartview.PercentageChartView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
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

    ConstraintLayout taskInfoPart;
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
    String weatherMain;
    String weatherDesc;
    String city;
    double latitude;
    double lo;
    TextView weatherText,weatherTextConstant;
    PercentageChartView progressBarDone;

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        weatherText.setVisibility(View.INVISIBLE);
        weatherTextConstant.setVisibility(View.INVISIBLE);
    }

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

        updateTodayProgress();
    }

    public void updateTodayProgress()
    {
        tasksDB tasksDB = new tasksDB(day_task_activity.this);
        List<Task> allTodayTasks = tasksDB.select(FinalDate);
        float allTasks = allTodayTasks.size();

        float completedTasks = 0;
        for(int i =0 ;i<allTasks;i++)
        {
            if(allTodayTasks.get(i).getStatus().equals("done"))
            {
                completedTasks++;
            }
        }
        if(allTasks == 0){
            progressBarDone.setProgress(0,true);
        }
        else{
            float progress = (float)((float)((float)((float)completedTasks/allTasks))*100);
            Log.i("Progresssssss:",Float.toString(progress));
            progressBarDone.setProgress(progress,true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_task);
        getSupportActionBar().hide();

        init();


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

        weatherHandle();
    }

    void weatherHandle()
    {
        try {
            locationManager = (LocationManager) getSystemService(Service.LOCATION_SERVICE);
            boolean gps_enabled = false;
            boolean network_enabled = false;

            try {
                gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch(Exception ex) {}

            try {
                network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            } catch(Exception ex) {}

            if(gps_enabled && network_enabled)
            {
                WeatherSelected();
            }
            else{
                weatherText.setVisibility(View.INVISIBLE);
                weatherTextConstant.setVisibility(View.INVISIBLE);
            }
        }
        catch (Exception ex)
        {
            weatherText.setVisibility(View.INVISIBLE);
            weatherTextConstant.setVisibility(View.INVISIBLE);
        }
    }

    void init() {
        taskInfoPart = findViewById(R.id.taskInfoPart);
        progressBarDone = findViewById(R.id.TodayProgress);
        latitude =35.6;
        lo = 51.4;
        weatherText = findViewById(R.id.weatherKindText);
        weatherTextConstant = findViewById(R.id.weather);
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
        Log.i("FinalDate",FinalDate);
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
        locationManager.removeUpdates(this);
        finish();
        Log.i("TAG","pause.............");
        super.onPause();

    }

    public void toggle(View view) {
        com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton btn = findViewById(R.id.toggle);
        ConstraintLayout tasks = findViewById(R.id.tasksContainer);
        ConstraintLayout events = findViewById(R.id.eventsContainer);
        LinearLayout.LayoutParams tasksInfoParams = (LinearLayout.LayoutParams) taskInfoPart.getLayoutParams();
        LinearLayout.LayoutParams tasksParams = (LinearLayout.LayoutParams) tasks.getLayoutParams();
        LinearLayout.LayoutParams eventParams = (LinearLayout.LayoutParams) events.getLayoutParams();
        if (tasks.getVisibility() == View.VISIBLE) {
            events.setVisibility(View.VISIBLE);
            tasks.setVisibility(View.INVISIBLE);
            taskInfoPart.setVisibility(View.INVISIBLE);
            btn.setText("Tasks");
            tasksParams.height = 1;
            tasksInfoParams.height = 1;
            eventParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        } else {
            events.setVisibility(View.INVISIBLE);
            tasks.setVisibility(View.VISIBLE);
            taskInfoPart.setVisibility(View.VISIBLE);
            btn.setText("events");
            eventParams.height = 1;
            tasksParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            tasksInfoParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        }
        tasks.setLayoutParams(tasksParams);
        events.setLayoutParams((eventParams));
        taskInfoPart.setLayoutParams(tasksInfoParams);
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

        latitude = lat;
        lo = lnt;
    }



    public void WeatherSelected() {

        String lat = Double.toString(latitude);
        String lon = Double.toString(lo);

        try{
            DownloadTask task = new DownloadTask();
            task.execute("https://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lon+"&appid=f39369dfbb38f8b754777c98a0038a6e");

        }
        catch(Exception ex)
        {

        }
    }

    public class DownloadTask extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while(data!=-1)
                {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

                return result;
            }
            catch(Exception e){
                e.printStackTrace();
                return null;
            }
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();

            try{
                JSONObject jsonObject = new JSONObject(s);
                String weatherInfo = jsonObject.getString("weather");
                Log.i("weather content",weatherInfo);
                JSONArray arr = new JSONArray(weatherInfo);

                for(int i=0;i<arr.length();i++)
                {
                    JSONObject jsonObject1 = arr.getJSONObject(i);
                    weatherMain = jsonObject1.getString("main");
                    weatherDesc = jsonObject1.getString("description");
                    weatherText.setText(weatherMain);
                }

                JSONObject jsonObject2 = new JSONObject(s);
                String weatherInfo2 = jsonObject2.getString("main");
                Log.i("main",weatherInfo2);
                String split [] = weatherInfo2.split(",");
                String splitTemp [] = split[0].split(":");
                //Toast.makeText(MainActivity.this, splitTemp[1], Toast.LENGTH_SHORT).show();
                //tempTV.setText((splitTemp[1]));
                float temp = Float.parseFloat(splitTemp[1]);
                temp -= 273;

            }catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }
}