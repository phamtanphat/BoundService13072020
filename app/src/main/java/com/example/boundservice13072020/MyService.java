package com.example.boundservice13072020;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class MyService extends Service {

    Notification mNotification;
    String CHANNEL_ID = "CHANNEL_ID";
    Handler handler;
    int count = 0;
    Context mContext;
    NotificationManager mNotificationManager;
    OnListenData onListenData;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        mNotification = createNotification(CHANNEL_ID,mContext,"Ban co thong bao moi");
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        handler = new Handler();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(1,mNotification);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (count >= 5){
                    stopSelf();
                }else{
                    handler.postDelayed(this,1000);
                    count++;
                    mNotificationManager.notify(1,createNotification(CHANNEL_ID,mContext,"Running : " + count));
                }
                onListenData.getData(count);

            }
        },1000);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("BBB", "onDestroy");
    }

    private Notification createNotification(String CHANNEL_ID, Context context , String text) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(
                        context,
                        CHANNEL_ID)
                        .setContentTitle("Ban co thong bao moi")
                        .setContentText(text)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setShowWhen(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "channel",
                    NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        return builder.build();
    }

    class MyBinder extends Binder{
        MyService getService() {
            return MyService.this;
        }
    }

    public void getDataNotification(OnListenData onListenData){
        this.onListenData = onListenData;
    }
}