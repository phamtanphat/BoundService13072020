package com.example.boundservice13072020;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button mBtnService;
    MyService myService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtnService = findViewById(R.id.buttonStartService);

        mBtnService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,MyService.class);
                startService(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, MyService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
           MyService.MyBinder myBinder = (MyService.MyBinder) iBinder;
           myService = myBinder.getService();
           myService.getDataNotification(new OnListenData() {
               @Override
               public void getData(int count) {
                   Log.d("BBB",count + "");
               }
           });
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d("BBB","DisConnect");
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(serviceConnection);
    }
}