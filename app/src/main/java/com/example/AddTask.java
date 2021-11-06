package com.example;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.myapplication.R;

import java.util.Date;

public class AddTask extends AppCompatActivity {

    DatePicker datePicker;
    TimePicker timePicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        datePicker = findViewById(R.id.DateTimeDatePicker);
        timePicker = findViewById(R.id.time_picker);

    }

    public void addTask(View view) {
        int year = datePicker.getYear();
        int month = datePicker.getMonth();
        int day = datePicker.getDayOfMonth();
        int hour = timePicker.getCurrentHour();
        int min = timePicker.getCurrentMinute();

        Date d = new Date();
        CharSequence s = DateFormat.format("MMMM d, yyyy ", d.getTime());

    }
}