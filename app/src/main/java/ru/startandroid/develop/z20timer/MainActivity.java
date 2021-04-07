package ru.startandroid.develop.z20timer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText numberOfSeconds;
    Button startButton;
    Button stopButton;
    View.OnClickListener oclStartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);
        numberOfSeconds = findViewById(R.id.numberOfSeconds);
        oclStartButton = new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.startButton:
                        Intent intent = new Intent(MainActivity.this, TimerService.class);
                        intent.putExtra("value", numberOfSeconds.getText().toString());
                        startServiceDependingOnTheAndroidVersion(intent);
                        startButton.setVisibility(View.GONE);
                        stopButton.setVisibility(View.VISIBLE);
                        break;
                    case R.id.stopButton:
                        onClickStop();
                        startButton.setVisibility(View.VISIBLE);
                        stopButton.setVisibility(View.GONE);
                        break;
                }
            }
        };
        startButton.setOnClickListener(oclStartButton);
        stopButton.setOnClickListener(oclStartButton);
    }
    @Override
    public void onStart(){
        super.onStart();
        registerReceiver(broadcastReceiver, new IntentFilter(TimerService.BROADCAST_ACTION));
    }
    public void onClickStop(){
        Intent intent = new Intent(MainActivity.this, TimerService.class);
        stopService(intent);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intentBr) {
            Log.i("MainActivity", "Broadcast Received: " + intentBr.getStringExtra("serviceMessage"));
            String message = intentBr.getStringExtra("serviceMessage");
            Toast.makeText(context, "Received : " + message, Toast.LENGTH_LONG).show();

        }
    };

    private void startServiceDependingOnTheAndroidVersion(Intent intent){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }
}