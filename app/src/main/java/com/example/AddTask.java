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

import com.example.entity.Task;
import com.example.entity.User;
import com.example.myapplication.R;
import com.example.webService.TaskAPI;


import java.util.Calendar;
import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddTask extends AppCompatActivity {

    DatePicker datePicker;
    TimePicker timePicker;
    EditText title;
    Spinner category;
    EditText desc;
    String userToken;
    String username;
    TaskAPI taskAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        init();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit createTask = new Retrofit.Builder()
                .baseUrl(TaskAPI.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        taskAPI = createTask.create(TaskAPI.class);


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

        String categoryStr =  category.getSelectedItem().toString();

        if(categoryStr.equals(""))
        {
            CustomeAlertDialog categortyEmpyt = new CustomeAlertDialog(this,"error","please fill the category field");
        }

        String titleStr = title.getText().toString();
        String descStr = desc.getText().toString();
        if(titleStr.equals(""))
        {
            CustomeAlertDialog titleError = new CustomeAlertDialog(this,"Error","fill the title field");
            //Toast.makeText(this, "fill the title field", Toast.LENGTH_SHORT).show();
            title.setBackgroundResource(R.drawable.border_red_task_error);
        }
        if(descStr.equals(""))
        {
            desc.setBackgroundResource(R.drawable.border_red_task_error);
            //Toast.makeText(this, "fill the description field", Toast.LENGTH_SHORT).show();
            CustomeAlertDialog messageError = new CustomeAlertDialog(this,"Error","fill the description field");
        }

        if(checkDate(year,month,day,hour,min) && !titleStr.equals("") && !descStr.equals("")&& !categoryStr.equals(""))
        {
            Toast.makeText(this,"task saved",Toast.LENGTH_SHORT).show();
            String datetime = Integer.toString(year)+"-"+Integer.toString(month)+"-"+Integer.toString(day)+" "+Integer.toString(hour)+":"+Integer.toString(min);
            Task newTask = new Task(titleStr,descStr, datetime, categoryStr, "time for "+categoryStr, userToken);
            Call<Task> callBack =taskAPI.createTask("application/json",newTask);
            callBack.enqueue(new Callback<Task>() {
                @Override
                public void onResponse(Call<Task> call, Response<Task> response) {
                    if(!response.isSuccessful())
                    {
                        CustomeAlertDialog errorConnecting = new CustomeAlertDialog(AddTask.this,"error","there is a problem connecting to server");
                    }
                    else{
                        String code = Integer.toString(response.code());
                        Task savedTask = response.body();
                        String task_token = savedTask.getTaskToken();
                        String title = savedTask.getTitle();
                        String dateTime = savedTask.getDateTime();
                        Toast.makeText(AddTask.this, code, Toast.LENGTH_SHORT).show();

                        //Todo
                        //saving the fields in db
                    }
                }

                @Override
                public void onFailure(Call<Task> call, Throwable t) {

                }
            });
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
        CustomeAlertDialog dateAlert = new CustomeAlertDialog(this,"error","you can set a task for past");
        //Toast.makeText(this, "you can set a task for past", Toast.LENGTH_SHORT).show();
        return false;

    }
}