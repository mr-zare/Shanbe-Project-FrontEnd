package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.entity.User;
import com.example.myapplication.webService.UserAPI;
import com.example.myapplication.webService.UserSession;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class forgotpass extends AppCompatActivity {
     CardView reset;
     EditText passf,confirmf ,codeToken;
     String pass,confirm,Codetoken;
     UserAPI userAPI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpass);
        getSupportActionBar().hide();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit SignUpRefrofit = new Retrofit.Builder()
                .baseUrl(UserAPI.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userAPI =SignUpRefrofit.create(UserAPI.class);


        passf = findViewById(R.id.passfg);
        confirmf = findViewById(R.id.confirmpassfg);
        reset =(CardView) findViewById(R.id.cardViewfg);
        codeToken = findViewById(R.id.etoken);


        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //v.setBackgroundColor(getResources().getColor(R.color.darkBlueTheme));
                passf.setBackgroundResource(R.drawable.border);
                confirmf.setBackgroundResource(R.drawable.border);
                pass = passf.getText().toString();
                confirm = confirmf.getText().toString();
                Codetoken = codeToken.getText().toString();


                v.setEnabled(false);
                if(isPassValid(pass,confirm)==true)
                {
                    //Toast.makeText(forgotpass.this,Codetoken,Toast.LENGTH_LONG);
                    User user = new User();
                    user.setCodeToken(Codetoken);
                    user.setPassword(pass);
                    Call<UserSession> callBack = userAPI.resetPassword("application/json",user);
                    callBack.enqueue(new Callback<UserSession>() {
                        @Override
                        public void onResponse(Call<UserSession> call, Response<UserSession> response) {
                            if(!response.isSuccessful())
                            {
                                Toast.makeText(forgotpass.this, "please enter your code again", Toast.LENGTH_LONG).show();
                                codeToken.setBackgroundResource(R.drawable.border_red);
                                v.setEnabled(true);
                            }
                            else{
                                String code = Integer.toString(response.code());
                                UserSession responseSession = response.body();
                                String token = responseSession.getToken();
                                Toast.makeText(forgotpass.this, code +"\n"+token, Toast.LENGTH_LONG).show();
                                Animation animation3 = AnimationUtils.loadAnimation(forgotpass.this, R.anim.blink_anim);
                                reset.startAnimation(animation3);
                                Intent output=new Intent(forgotpass.this,login.class);
                                //output.putExtra("token",token);
                                startActivity(output);
                                v.setEnabled(true);
                            }
                        }

                        @Override
                        public void onFailure(Call<UserSession> call, Throwable t) {
                            Toast.makeText(forgotpass.this, "error is:"+t.getMessage(), Toast.LENGTH_LONG).show();
                            codeToken.setBackgroundResource(R.drawable.border_red);
                            v.setEnabled(true);
                        }
                    });
//                    Animation animation3 = AnimationUtils.loadAnimation(forgotpass.this, R.anim.blink_anim);
//                    reset.startAnimation(animation3);
//                    Intent output=new Intent(forgotpass.this,welcome.class);
//                    startActivity(output);
//                    v.setEnabled(true);
                }
                else
                {
                    //v.getBackground().setColorFilter(Color.RED,PorterDuff.Mode.SRC_ATOP);
                    //Toast.makeText(forgotpass.this, "password and confirm pass don't match", Toast.LENGTH_LONG).show();
                    v.setEnabled(true);
                    passf.setBackgroundResource(R.drawable.border_red);
                    confirmf.setBackgroundResource(R.drawable.border_red);
                }
            }
        });
    }
    public boolean isPassValid(String pass,String confirm)
    {
        if(pass.length()>=8)
        {
            if(checkString(pass))
            {
                if(containsDigit(pass))
                {
                    if(pass.equals(confirm)==true)
                    {
                        return true;
                    }
                    else{
                        Toast.makeText(this, "password and confirmation of the password are not the same", Toast.LENGTH_SHORT).show();

                        return false;
                    }
                }
                else{
                    Toast.makeText(this, "password should have at least one digit", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            else{
                Toast.makeText(this, "password should have at least one capital letter", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        else{
            Toast.makeText(this, "password have to be at least 8 characters", Toast.LENGTH_SHORT).show();
            return false;
        }

    }


    public void ColorFilterForget(View view) {
        passf.getBackground().setColorFilter(null);
        confirmf.getBackground().setColorFilter(null);
        passf.setBackgroundResource(R.drawable.border);
        confirmf.setBackgroundResource(R.drawable.border);
        view.getBackground().setColorFilter(Color.BLACK,PorterDuff.Mode.SRC_ATOP);
    }


    private static boolean checkString(String str) {
        char ch;
        boolean capitalFlag = false;
        boolean lowerCaseFlag = false;
        boolean numberFlag = false;
        for(int i=0;i < str.length();i++) {
            ch = str.charAt(i);
            if( Character.isDigit(ch)) {
                numberFlag = true;
            }
            else if (Character.isUpperCase(ch)) {
                capitalFlag = true;
            } else if (Character.isLowerCase(ch)) {
                lowerCaseFlag = true;
            }
            if(numberFlag && capitalFlag && lowerCaseFlag)
                return true;
        }
        return false;
    }

    public final boolean containsDigit(String s) {
        boolean containsDigit = false;

        if (s != null && !s.isEmpty()) {
            for (char c : s.toCharArray()) {
                if (containsDigit = Character.isDigit(c)) {
                    break;
                }
            }
        }

        return containsDigit;
    }
}
