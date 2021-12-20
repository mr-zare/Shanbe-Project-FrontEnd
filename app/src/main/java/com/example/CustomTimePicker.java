package com.example;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AlertDialog;

import com.example.myapplication.R;

public class CustomTimePicker {
    AlertDialog alertDialog;
    TimePicker timePicker;
    Button btnOk;
    String Time;
    String HourS;
    String MinS;
    int hourNum;
    int minNum;
    public CustomTimePicker(Context context)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);

        View alertView = LayoutInflater.from(context).inflate(R.layout.custome_time_picker,null);
        builder.setView(alertView);

        alertDialog = builder.create();
        alertDialog.show();

        btnOk = alertView.findViewById(R.id.okBtn);
        timePicker = alertView.findViewById(R.id.timePicker);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hour = timePicker.getCurrentHour();
                int min = timePicker.getCurrentMinute();

                HourS = Integer.toString(hour);
                if(HourS.length()==1)
                {
                    HourS = "0"+HourS;
                }
                MinS = Integer.toString(min);
                if(MinS.length()==1)
                {
                    MinS = "0" + MinS;
                }

                hourNum = hour;
                minNum = min;

                alertDialog.dismiss();
            }
        });
    }

    public int getHourNum() {
        return hourNum;
    }

    public int getMinNum() {
        return minNum;
    }

    public String getHourS() {
        return HourS;
    }

    public String getMinS() {
        return MinS;
    }

    public String getTime()
    {
        return Time;
    }
}
