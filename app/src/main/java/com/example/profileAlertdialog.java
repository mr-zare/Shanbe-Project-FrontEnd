package com.example;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.entity.User;
import com.example.myapplication.R;
import com.example.webService.UserAPI;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class profileAlertdialog extends android.app.Activity{

        AlertDialog alertDialog;
        TextView titleTV;
        TextView messageTV;
        Button btnOk;
        UserAPI userAPI;

    public profileAlertdialog() {
    }

    String username,  firstName, lastName, email, phoneNumber, avatar;
        public profileAlertdialog(Context context, String username)
        {
            this.username = username;

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(true);

            View alertView = LayoutInflater.from(context).inflate(R.layout.activity_profile_alertdialog,null);
            builder.setView(alertView);

            TextView firstNameLastNameProfileFragment;
            TextView userNameProfileFragment;
            TextView emailProfileFragment;
            TextView phoneProfileFragment;
            ImageView profileImage;

            profileImage = alertView.findViewById(R.id.profileImageSource);
            firstNameLastNameProfileFragment = alertView.findViewById(R.id.firstNameLastNameTextView);
            userNameProfileFragment = alertView.findViewById(R.id.userNameTextViewProfileFragment);
            emailProfileFragment = alertView.findViewById(R.id.emailTextViewProfileFragment);
            phoneProfileFragment = alertView.findViewById(R.id.phoneNumberProfileFragment);

            Retrofit LoginRetrofit = new Retrofit.Builder()
                    .baseUrl(UserAPI.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            userAPI =LoginRetrofit.create(UserAPI.class);

            SharedPreferences shP = context.getSharedPreferences("userInformation", Context.MODE_PRIVATE);
            String token = shP.getString("token", "");

            JsonObject userObject = new JsonObject();
            userObject.addProperty("username",username);
            Call<User> callBackUser = userAPI.getProfile("token "+token,userObject);
            callBackUser.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if(response.isSuccessful())
                    {
                        User creator = response.body();
                        firstName = creator.getFirst_name();
                        lastName = creator.getLast_name();
                        email = creator.getEmail();
                        phoneNumber = creator.getPhone_number();
                        avatar = creator.getAvatar();

                        firstNameLastNameProfileFragment.setText(firstName+ " " + lastName);
                        userNameProfileFragment.setText(username);
                        emailProfileFragment.setText(email);
                        phoneProfileFragment.setText(phoneNumber);

                        Picasso.get().load(avatar).placeholder(R.drawable.acount_circle).error(R.drawable.acount_circle).into(profileImage);

                        alertDialog = builder.create();
                        alertDialog.show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });

            btnOk = alertView.findViewById(R.id.okProfileDialog);


            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
        }

}