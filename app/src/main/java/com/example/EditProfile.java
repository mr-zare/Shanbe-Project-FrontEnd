package com.example;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.entity.User;
import com.example.myapplication.R;
import com.example.webService.UserAPI;
import com.example.webService.UserSession;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditProfile extends AppCompatActivity {
    EditText emailEditText;
    EditText firstNameEditText;
    EditText lastNameEditText;
    EditText phoneNumberEditText;
    UserAPI userAPI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_edit_profile);
        emailEditText = findViewById(R.id.eemailid);
        firstNameEditText = findViewById(R.id.firstNameId);
        lastNameEditText = findViewById(R.id.lastNameId);
        phoneNumberEditText = findViewById(R.id.phoneNumberId);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit LoginRetrofit = new Retrofit.Builder()
                .baseUrl(UserAPI.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userAPI =LoginRetrofit.create(UserAPI.class);
        SharedPreferences shP = getSharedPreferences("sideBarSharedPreferences", MODE_PRIVATE);
        String token = shP.getString("token", "");
        Toast.makeText(this, token, Toast.LENGTH_SHORT).show();
        Call<User> userSessionCall = userAPI.showProfile("token "+ token);
        userSessionCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(!response.isSuccessful())
                {
                    Toast.makeText(EditProfile.this, "Some Field Wrong", Toast.LENGTH_SHORT).show();
                }
                else{
                    String code = Integer.toString(response.code());
                    User user = response.body();
                    String email = user.getEmail();
                    emailEditText.setHint(user.getEmail());
                    firstNameEditText.setHint(user.getFirst_name());
                    lastNameEditText.setHint(user.getLast_name());
                    phoneNumberEditText.setHint(user.getPhone_number());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(EditProfile.this, "error is :"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}