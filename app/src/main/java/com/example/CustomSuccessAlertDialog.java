package com.example;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Bundle;
import android.widget.TextView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.myapplication.R;

public class CustomSuccessAlertDialog extends Activity {
    AlertDialog alertDialog;
    TextView titleTV;
    TextView messageTV;
    TextView tokenTV;
    Button btnOk;
    Button btnCopy;
    public CustomSuccessAlertDialog(Context context, String title, String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);

        View alertView = LayoutInflater.from(context).inflate(R.layout.successfull_alert_dialog,null);
        builder.setView(alertView);

        alertDialog = builder.create();
        alertDialog.show();

        titleTV = alertView.findViewById(R.id.titleTv_s);
        messageTV = alertView.findViewById(R.id.messageTv_s);
        btnOk = alertView.findViewById(R.id.okBtn_s);

        titleTV.setText(title);
        messageTV.setText(message);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }

    public CustomSuccessAlertDialog(Context context, String title, String message, String token)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);

        View alertView = LayoutInflater.from(context).inflate(R.layout.created_event_dialog,null);
        builder.setView(alertView);

        alertDialog = builder.create();
        alertDialog.show();

        titleTV = alertView.findViewById(R.id.titleTv_s);
        messageTV = alertView.findViewById(R.id.messageTv_s);
        tokenTV = alertView.findViewById(R.id.tokenTv_s);
        btnOk = alertView.findViewById(R.id.okBtn_s);
        btnCopy = alertView.findViewById(R.id.copy_s);

        titleTV.setText(title);
        messageTV.setText(message);
        tokenTV.setText(token);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }
}
