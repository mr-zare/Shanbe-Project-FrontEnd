package com.example;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.squareup.picasso.Picasso;

public class profileAlertdialog {

        AlertDialog alertDialog;
        TextView titleTV;
        TextView messageTV;
        Button btnOk;

        String username,  firstName, lastName, email, phoneNumber, avatar;
        public profileAlertdialog(Context context, String username, String firstName,String lastName,String email,String phoneNumber,String avatar)
        {
            this.username = username;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.phoneNumber = phoneNumber;
            this.avatar = avatar;

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

            firstNameLastNameProfileFragment.setText(firstName+ " " + lastName);
            userNameProfileFragment.setText(username);
            emailProfileFragment.setText(email);
            phoneProfileFragment.setText(phoneNumber);

            Picasso.get().load(avatar).placeholder(R.drawable.acount_circle).error(R.drawable.acount_circle).into(profileImage);


            alertDialog = builder.create();
            alertDialog.show();

            btnOk = alertView.findViewById(R.id.okProfileDialog);


            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
        }

}