package com.example;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class FilterEvents extends AppCompatActivity {
    Spinner category;
    Spinner locationfilter;
    DatePicker datefilter;
    Button filterBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filters_evant);
        getSupportActionBar().hide();

        init();




    }


    public void init()
    {
        category = findViewById(R.id.categoryfilter);
        locationfilter = findViewById(R.id.locationfilter);
        datefilter = findViewById(R.id.datefilter);
        filterBtn = findViewById(R.id.filterBtn);
    }

    public void filter(View view) {
        String categoryStr = category.getSelectedItem().toString();
        if(categoryStr.equals("category"))
        {
            categoryStr = "";
        }

        String locationStr = locationfilter.getSelectedItem().toString();
        if(locationStr.equals("location"))
        {
            locationStr="";
        }

        int year = datefilter.getYear();
        int month = datefilter.getMonth();
        int day = datefilter.getDayOfMonth();

        String date = year+"_"+month+"_"+day;
        //todo
        //first we have to send these values
        //then get a list of events and sent the list of events to the previous page ...

        //*********** => the date picker by default returns the current day .....
        Intent intent = new Intent(FilterEvents.this,event_activity.class);
        startActivity(intent);
        finish();
    }
}
