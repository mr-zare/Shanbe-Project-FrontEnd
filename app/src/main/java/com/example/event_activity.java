package com.example;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.adapter.eventAdapter;
import com.example.adapter.taskAdapter;
import com.example.entity.Event;
import com.example.entity.Task;
import com.example.myapplication.R;
import com.example.webService.EventAPI;
import com.example.webService.TaskAPI;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class event_activity extends AppCompatActivity implements LocationListener {

    boolean IsLocationFound;
    boolean filtered;
    EventAPI eventAPI;
    EditText search;
    String userToken;
    Button filterBtn;
    eventAdapter eventAdap;
    ListView eventsListView;
    LocationManager locationManager;
    private ShimmerFrameLayout mFrameLayout;
    int i = 0;
    double lo;
    double latitude;
    NetworkInfo mWifi;

    Handler hdlr = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_event);
       // Toast.makeText(this, "create", Toast.LENGTH_SHORT).show();
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        init();
        mFrameLayout = findViewById(R.id.shimmerLayout);
    }

    @Override
    protected void onResume() {
        IsLocationFound = false;
        mFrameLayout.startShimmer();
        super.onResume();
        locationManager = (LocationManager) getSystemService(Service.LOCATION_SERVICE);

        // Toast.makeText(this, "resume", Toast.LENGTH_SHORT).show();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 1f, this);
        }

        String locationStr= "";
        String categoryStr = "";
        String date = "";

        locationStr = getIntent().getStringExtra("location");
        categoryStr = getIntent().getStringExtra("category");
        date = getIntent().getStringExtra("s_time");

        JsonObject filter = new JsonObject();
        if(locationStr.length()>=1)
        {
            filter.addProperty("location",locationStr);
        }
        if(categoryStr.length() >=1)
        {
            filter.addProperty("category",categoryStr);
        }
        if(date.length()>=1)
        {
            filter.addProperty("s_time",date);
        }



        filter.addProperty("privacy",false);
        filter.addProperty("isVirtual",false);

        if (mWifi.isConnected()) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            Retrofit searchEvent = new Retrofit.Builder()
                    .baseUrl(TaskAPI.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            eventAPI = searchEvent.create(EventAPI.class);


            filtered = true;
            Call<List<Event>> callBack = eventAPI.event_search("token "+userToken,filter);
            callBack.enqueue(new Callback<List<Event>>() {
                @Override
                public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                    if(!response.isSuccessful())
                    {
                        CustomErrorAlertDialog errorConnecting = new CustomErrorAlertDialog(event_activity.this,"Error","there is a problem connecting to server");
                    }
                    else{
                        String code = Integer.toString(response.code());
                        List<Event> filteredEventList = response.body();
                        //   Toast.makeText(event_activity.this, code, Toast.LENGTH_SHORT).show();

                        eventAdap = new eventAdapter(event_activity.this,filteredEventList);

                        eventsListView.setAdapter(eventAdap);
                        mFrameLayout.startShimmer();
                        mFrameLayout.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<List<Event>> call, Throwable t) {
                    CustomErrorAlertDialog errorConnecting = new CustomErrorAlertDialog(event_activity.this,"Error","there is a problem connecting to server");
                }
            });
            if(locationStr.length()==0 && categoryStr.length()==0&& date.length()==0)
            {
                filtered = false;
                suggestedHandler();
            }
        }
        else{
            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onPause() {
        mFrameLayout.stopShimmer();
        super.onPause();
    }



    public void init()
    {

        search = findViewById(R.id.searchEventEditText);
        SharedPreferences sharedPreferences = getSharedPreferences("authentication", MODE_PRIVATE);
        userToken = sharedPreferences.getString("token", "");
        eventsListView = findViewById(R.id.eventsList);

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
                    CustomErrorAlertDialog getTasksDayError = new CustomErrorAlertDialog(event_activity.this,"Error","there is a problem with your internet connection");
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
                CustomErrorAlertDialog getTasksDayError = new CustomErrorAlertDialog(event_activity.this,"Error","there is a problem with your internet connection");
            }
        });
    }

    public void goTofilterPage(View view) {
        Intent intent = new Intent(event_activity.this,FilterEvents.class);
        startActivity(intent);
        finish();
        filtered = true;
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
        double lat = location.getLatitude();
        double lnt = location.getLongitude();

        latitude = lat;
        lo = lnt;

        Log.i("long",Double.toString(lo));
        Log.i("latitude",Double.toString(latitude));

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit suggestedEvents = new Retrofit.Builder()
                .baseUrl(TaskAPI.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        eventAPI = suggestedEvents.create(EventAPI.class);

        JsonObject geo = new JsonObject();
        geo.addProperty("latitude",Double.toString(latitude));
        geo.addProperty("longitude",Double.toString(lo));
        Call<JsonObject> coordinateRequest = eventAPI.calendar_get_suggestions("token "+userToken,geo);
        coordinateRequest.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(!response.isSuccessful())
                {
                    CustomErrorAlertDialog customErrorAlertDialog = new CustomErrorAlertDialog(event_activity.this,"Error","there is a problem connecting to server");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                CustomErrorAlertDialog customErrorAlertDialog = new CustomErrorAlertDialog(event_activity.this,"Error","there is a problem connecting to server");
            }
        });
        locationManager.removeUpdates(event_activity.this);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        if(!filtered)
        {
            //latitude =35.6;
            //lo = 51.4;
            //IsLocationFound = true;
            //Log.i("long",Double.toString(lo));
            //Log.i("latitude",Double.toString(latitude));
            suggestedHandler();
        }
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

    public void suggestedHandler()
    {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit suggestedEvents = new Retrofit.Builder()
                .baseUrl(TaskAPI.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        eventAPI = suggestedEvents.create(EventAPI.class);

        JsonObject empty = new JsonObject();
        Call<List<Event>> request = eventAPI.event_getSuggestions("token "+userToken,empty);
        request.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                if(!response.isSuccessful())
                {
                    CustomErrorAlertDialog customErrorAlertDialog = new CustomErrorAlertDialog(event_activity.this,"Error","there is a problem connecting to server");
                }
                else{
                    String code = Integer.toString(response.code());
                    List<Event> suggestedEvents = response.body();
                    //   Toast.makeText(event_activity.this, code, Toast.LENGTH_SHORT).show();

                    eventAdap = new eventAdapter(event_activity.this,suggestedEvents);

                    eventsListView.setAdapter(eventAdap);
                    mFrameLayout.startShimmer();
                    mFrameLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                CustomErrorAlertDialog customErrorAlertDialog = new CustomErrorAlertDialog(event_activity.this,"Error","there is a problem connecting to server");
            }
        });
    }
}