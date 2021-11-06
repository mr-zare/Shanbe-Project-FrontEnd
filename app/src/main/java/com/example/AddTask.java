package com.example;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.entity.User;
import com.example.myapplication.R;


import java.util.Calendar;
import java.util.Date;

public class AddTask extends AppCompatActivity {

    DatePicker datePicker;
    TimePicker timePicker;
    EditText title;
    Spinner category;
    EditText desc;
    String userToken;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        init();

        desc.setBackgroundResource(R.drawable.border_red_task_error);
        title.setBackgroundResource(R.drawable.border_red_task_error);
        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(title.getText().toString()!="")
                {
                    title.setBackgroundResource(R.drawable.border_task);
                }
                else
                {
                    title.setBackgroundResource(R.drawable.border_red_task_error);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(title.getText().toString()=="")
                {
                    title.setBackgroundResource(R.drawable.border_red_task_error);
                }
            }
        });

        desc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(desc.getText().toString()!="")
                {
                    desc.setBackgroundResource(R.drawable.border_task);
                }
                else{
                    desc.setBackgroundResource(R.drawable.border_red_task_error);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(desc.getText().toString()=="")
                {
                    desc.setBackgroundResource(R.drawable.border_red_task_error);
                }
            }
        });
    }

    public void init()
    {
        datePicker = findViewById(R.id.DateTimeDatePicker);
        timePicker = findViewById(R.id.time_picker);
        title = findViewById(R.id.titleEt);
        category = findViewById(R.id.Spinner_category);
        desc = findViewById(R.id.descriptionEt);
        SharedPreferences sharedPreferences = getSharedPreferences("authentication", MODE_PRIVATE);
        userToken = sharedPreferences.getString("token", "");
        username = sharedPreferences.getString("username","");
    }

    public void addTask(View view) {
        int year = datePicker.getYear();
        int month = datePicker.getMonth();
        int day = datePicker.getDayOfMonth();
        int hour = timePicker.getCurrentHour();
        int min = timePicker.getCurrentMinute();

        String titleStr = title.getText().toString();
        String descStr = desc.getText().toString();
        if(titleStr.equals(""))
        {
            Toast.makeText(this, "fill the title field", Toast.LENGTH_SHORT).show();
            title.setBackgroundResource(R.drawable.border_red_task_error);
        }
        if(descStr.equals(""))
        {
            desc.setBackgroundResource(R.drawable.border_red_task_error);
            Toast.makeText(this, "fill the description field", Toast.LENGTH_SHORT).show();
        }

        if(checkDate(year,month,day,hour,min) && !titleStr.equals("") && !descStr.equals(""))
        {
            Toast.makeText(this,"task saved",Toast.LENGTH_SHORT).show();
            //todo
            //have to send this information to backEnd...
            //
            //
            //
            //

        }
    }




    public boolean checkDate(int year, int month , int day,int hour,int min)
    {

        Calendar c = Calendar.getInstance();
        int currentDay = c.get(Calendar.DAY_OF_MONTH);
        int currentMonth = c.get(Calendar.MONTH);
        int currentYear = c.get(Calendar.YEAR);

        
        Time now = new Time();
        now.setToNow();

        int currentHour = now.hour;
        int currentMinuate = now.minute;

        if(currentYear<year)
        {
            return true;
        }
        else if(currentYear==year && currentMonth<month)
        {
            return true;
        }
        else if(currentYear == year && currentMonth == month && currentDay < day)
        {
            return true;
        }
        else if (currentYear == year && currentMonth == month && currentDay==day && currentHour<hour)
        {
            return true;
        }
        else if(currentYear == year && currentMonth == month && currentDay==day && currentHour==hour && currentMinuate<min)
        {
            return true;
        }
        Toast.makeText(this, "you can set a task for past", Toast.LENGTH_SHORT).show();
        return false;

    }
}