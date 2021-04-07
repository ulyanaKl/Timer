package ru.startandroid.develop.z20timer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CompleteActivity extends AppCompatActivity {
    TextView elapsedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete);
        showTime();
    }

    public void showTime(){
        Intent intent = getIntent();
        int time = intent.getIntExtra("value", 0);
        elapsedTime = findViewById(R.id.time);
        elapsedTime.setText("Elapsed time in seconds: " + time);
    }

}
