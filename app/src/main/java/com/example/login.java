package com.example;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
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

public class login extends AppCompatActivity {
    CardView logincv;
    TextView forgetpass;
    TextView gosignup;
    EditText enamelog,epasslog;
    UserAPI userAPI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        init();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit LoginRetrofit = new Retrofit.Builder()
                .baseUrl(UserAPI.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userAPI =LoginRetrofit.create(UserAPI.class);


        //added
        enamelog.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                enamelog.setBackgroundResource(R.drawable.border);
                epasslog.setBackgroundResource(R.drawable.border);
                enamelog.setBackgroundResource(R.drawable.border_selected);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        epasslog.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                enamelog.setBackgroundResource(R.drawable.border);
                epasslog.setBackgroundResource(R.drawable.border);
                epasslog.setBackgroundResource(R.drawable.border_selected);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        logincv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //v.setBackgroundColor(getResources().getColor(R.color.darkBlueTheme));
                v.setEnabled(false);
                //String names1="hi",pass1="hello";
                String namel=enamelog.getText().toString();
                String passl=epasslog.getText().toString();

                enamelog.setBackgroundResource(R.drawable.border);
                epasslog.setBackgroundResource(R.drawable.border);

                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink_anim);
                logincv.startAnimation(animation1);

                User user = new User(namel,passl);
                Call<UserSession> userSessionCall = userAPI.UserLogin("application/json",user);
                userSessionCall.enqueue(new Callback<UserSession>() {
                    @Override
                    public void onResponse(Call<UserSession> call, Response<UserSession> response) {
                        if(!response.isSuccessful())
                        {
                            Toast.makeText(login.this, "username or password is not correct!", Toast.LENGTH_SHORT).show();
                            v.setEnabled(true);
                            v.clearAnimation();
                            enamelog.setBackgroundResource(R.drawable.border_red);
                            epasslog.setBackgroundResource(R.drawable.border_red);
                        }
                        else{
                            String code = Integer.toString(response.code());
                            UserSession userSession = response.body();
                            userSession.setUsername(namel);
                            String token = userSession.getToken();
                            Toast.makeText(login.this, code+"   "+namel , Toast.LENGTH_SHORT).show();

                            //SystemClock.sleep(200);
                            Intent output=new Intent(login.this,welcome.class);
                            output.putExtra("token",token);
                            output.putExtra("username",namel);
                            startActivity(output);
                            v.clearAnimation();
                            v.setEnabled(true);
                        }
                    }

                    @Override
                    public void onFailure(Call<UserSession> call, Throwable t) {
                        Toast.makeText(login.this, "error is :"+t.getMessage(), Toast.LENGTH_SHORT).show();
                        enamelog.setBackgroundResource(R.drawable.border_red);
                        epasslog.setBackgroundResource(R.drawable.border_red);
                        v.setEnabled(true);
                    }
                });
               // }
//                if(names1==namel && passl==pass1) {
//                    Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
//                    logincv.startAnimation(animation1);
//                    Intent output=new Intent(login.this,welcome.class);
//                    startActivity(output);
//                }
//                else
//                {
//                    Toast.makeText(com.example.login.this, "username or pass is in correct", Toast.LENGTH_SHORT).show();
//                }
            }
        });
        forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Animation animation2= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fadein);
               forgetpass.startAnimation(animation2);
                Intent fp=new Intent(login.this,forgetPass_email.class);
                startActivity(fp);
            }
        });
        gosignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation3= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fadein);
                gosignup.startAnimation(animation3);
                Intent sup=new Intent(login.this, SignUp.class);
                startActivity(sup);
                finish();
            }
        });
    }

    public void init()
    {
        logincv=(CardView) findViewById(R.id.cardViewlogin);
        epasslog=(EditText)findViewById(R.id.passidlog);
        enamelog=(EditText)findViewById(R.id.nameidlog);
        forgetpass=(TextView) findViewById(R.id.forgetpass);
        gosignup=(TextView) findViewById(R.id.gosignup);
    }

    public void colorFilterLogin(View view) {
        epasslog.getBackground().setColorFilter(null);
        enamelog.getBackground().setColorFilter(null);
        epasslog.setBackgroundResource(R.drawable.border);
        enamelog.setBackgroundResource(R.drawable.border);
        //view.getBackground().setColorFilter(Color.BLACK,PorterDuff.Mode.SRC_ATOP);
        view.setBackgroundResource(R.drawable.border_selected);
    }
}