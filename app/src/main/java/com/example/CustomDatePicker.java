package com.example;

import android.content.Context;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.myapplication.R;

import java.util.Calendar;

public class CustomDatePicker {
    AlertDialog alertDialog;
    Button btnOk;
    DatePicker datePicker;
    String date;
    String YearS;
    String MonthS;
    String DayS;
    int yearNum;
    int monthNum;
    int dayNum;
    public CustomDatePicker(Context context)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);

        View alertView = LayoutInflater.from(context).inflate(R.layout.custome_date_picker,null);
        builder.setView(alertView);

        date = "";

        alertDialog = builder.create();
        alertDialog.show();

        datePicker = alertView.findViewById(R.id.datePicker);
        btnOk = alertView.findViewById(R.id.okBtn);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = datePicker.getYear();
                int month = datePicker.getMonth();
                int day = datePicker.getDayOfMonth();

                String yearStr = Integer.toString(year);
                String monthStr = Integer.toString(month);
                if(monthStr.length() == 1)
                {
                    monthStr = "0" + monthStr;
                }
                String dayStr = Integer.toString(day);
                if(dayStr.length()==1)
                {
                    dayStr = "0" + dayStr;
                }
                date = yearStr+"_"+monthStr+"_"+dayStr;


                if(!checkDate(year,month,day,context))
                {
                    CustomErrorAlertDialog errorDate = new CustomErrorAlertDialog(context,"Error","you can not select a date in past");

                }
                else{
                    YearS = yearStr;
                    MonthS = monthStr;
                    DayS = dayStr;
                    yearNum =  year;
                    monthNum = month;
                    dayNum = day;
                    alertDialog.dismiss();
                }
            }
        });
    }

    public String getDate()
    {
        return date;
    }

    public String getYearS() {
        return YearS;
    }

    public String getMonthS() {
        return MonthS;
    }

    public String getDayS() {
        return DayS;
    }

    public int getYearNum() {
        return yearNum;
    }

    public int getMonthNum() {
        return monthNum;
    }

    public int getDayNum() {
        return dayNum;
    }

    public boolean checkDate(int year, int month , int day, Context context)
    {

        Calendar c = Calendar.getInstance();
        int currentDay = c.get(Calendar.DAY_OF_MONTH);
        int currentMonth = c.get(Calendar.MONTH);
        int currentYear = c.get(Calendar.YEAR);


        String currentDateTime = Integer.toString(currentYear)+Integer.toString(currentMonth)+Integer.toString(currentDay);
        // Toast.makeText(this, currentDateTime, Toast.LENGTH_SHORT).show();
        if(currentYear<year)
        {
            return true;
        }
        else if(currentYear==year && currentMonth<month)
        {
            return true;
        }
        else if(currentYear == year && currentMonth == month && currentDay <= day)
        {
            return true;
        }

        //Toast.makeText(this, "you can set a task for past", Toast.LENGTH_SHORT).show();
        return false;

    }
}
