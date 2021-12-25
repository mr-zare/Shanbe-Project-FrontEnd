package com.example;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;

public class profileAlertdialog {

        AlertDialog alertDialog;
        TextView titleTV;
        TextView messageTV;
        Button btnOk;
        public profileAlertdialog(Context context, String username, String message)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(false);

            View alertView = LayoutInflater.from(context).inflate(R.layout.activity_profile_alertdialog,null);
            builder.setView(alertView);

            alertDialog = builder.create();
            alertDialog.show();

        //    titleTV = alertView.findViewById(R.id.titleTv);
          //  messageTV = alertView.findViewById(R.id.messageTv);
            btnOk = alertView.findViewById(R.id.okProfileDialog);

      //      titleTV.setText(title);
      //      messageTV.setText(message);

            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
        }

}