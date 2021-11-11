package com.example.BroadCast;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.text.format.Time;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.DataBase.tasksDB;
import com.example.entity.Task;
import com.example.myapplication.R;
import android.os.Handler ;
import android.os.IBinder ;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class notificationService extends Service {

    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;

    @Override
    public IBinder onBind (Intent arg0) {
        return null;
    }
    @Override
    public int onStartCommand (Intent intent , int flags , int startId) {
        super .onStartCommand(intent , flags , startId) ;
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        tasksDB tasksdb = new tasksDB(notificationService.this);
                        ArrayList<Task> currentTasks = tasksdb.select();

                        Calendar c = Calendar.getInstance();
                        int currentDay = c.get(Calendar.DAY_OF_MONTH);
                        int currentMonth = c.get(Calendar.MONTH);
                        int currentYear = c.get(Calendar.YEAR);


                        Time now = new Time();
                        now.setToNow();

                        int currentHour = now.hour;
                        int currentMinuate = now.minute;

                        String currentTimeStr = Integer.toString(currentYear)+"-"+Integer.toString(currentMonth)+"-"+Integer.toString(currentMinuate)+"_"+Integer.toString(currentHour)+":"+Integer.toString(currentMinuate);

                        boolean taskTime = false;
                        for(Task task : currentTasks )
                        {
                            if(currentTimeStr == task.getDateTime().toString())
                            {
                                NotificationManager notificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);

                                String channelId = "channelId";
                                String channelName = "channelName";

                                Notification notification;
                                NotificationCompat.Builder builder = new NotificationCompat.Builder(notificationService.this);
                                builder.setContentTitle(task.getTitle().toString());
                                builder.setContentText("time for "+task.getTitle());
                                builder.setSmallIcon(R.drawable.logo_second);
                                builder.setChannelId(channelId);

                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                    NotificationChannel notificationChannel = new NotificationChannel(channelId,channelName,NotificationManager.IMPORTANCE_DEFAULT);
                                    notificationManager.createNotificationChannel(notificationChannel);
                                    builder.setChannelId(channelId);
                                }

                                notification = builder.build();

                                notificationManager.notify(1,notification);

                            }
                        }
                    }
                },
                0,5000
        );
        return START_STICKY ;
    }
    @Override
    public void onCreate () {
        super.onCreate();
    }
    @Override
    public void onDestroy () {
        super .onDestroy() ;
    }
}
