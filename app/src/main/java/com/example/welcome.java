package com.example;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.myapplication.R;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;

public class welcome extends AppCompatActivity {

    ImageView logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getSupportActionBar().hide();
        ProgressBar progressBar = (ProgressBar)findViewById(R.id.spin_kit);
        Sprite doubleBounce = new Wave();
        progressBar.setIndeterminateDrawable(doubleBounce);
        Animation animation = AnimationUtils.loadAnimation(welcome.this, R.anim.zoomin);
        logo = findViewById(R.id.logo2);
        logo.startAnimation(animation);
       Intent in = getIntent();
//        String token = in.getStringExtra("token");
        String username = in.getStringExtra("username");

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                SharedPreferences shP = getSharedPreferences("userInformation", MODE_PRIVATE);
                String token = shP.getString("token", "");
                if(token.equals("")){
                    Intent out = new Intent(welcome.this, login.class);
                    startActivity(out);
                    finish();
                }
                else {
                    Intent out = new Intent(welcome.this, MainActivity.class);
                    out.putExtra("token", token);
                    out.putExtra("username", username);
                    startActivity(out);
                    finish();
                }
            }
        }, 5000);

    }
}