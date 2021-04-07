package ru.startandroid.develop.z20timer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TimerService extends Service {
    final String LOG_TAG = "myLogs";
    ExecutorService es;
    Object someRes;
    boolean isRunning = false;
    public static final String BROADCAST_ACTION = "ru.startandroid.develop.z20timer.MainActivity";

    public void onCreate() {
        super.onCreate();
        es = Executors.newFixedThreadPool(1);
        someRes = new Object();
        isRunning = true;
    }

    public IBinder onBind(Intent arg0) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "onStartCommand");
        String strSec = intent.getStringExtra("value");
        MyRun mr = new MyRun(strSec);
        es.execute(mr);
        return super.onStartCommand(intent, flags, startId);
    }

    class MyRun implements Runnable {
        int seconds;


        public MyRun(String strSec) {
            this.seconds = Integer.parseInt(strSec);

        }

        @Override
        public void run() {

            for (int i = seconds; i >= 0; i--) {
                Log.d(LOG_TAG, "i = " + i);
                try {
                    TimeUnit.SECONDS.sleep(1);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!isRunning) {
                    stopSelf();
                    break;
                }
                if (i == 0) {
                    broadcastReceiver();
                    startCompleteActivity(seconds);
                    stopSelf();
                }
            }
        }

        void broadcastReceiver() {
            Intent intentBr = new Intent(BROADCAST_ACTION);
            intentBr.putExtra("serviceMessage", "Service is finished");
            sendBroadcast(intentBr);
        }
    }

    @Override
    public void onDestroy() {
        isRunning = false;
        Toast.makeText(this, "MyService Completed or Stopped.", Toast.LENGTH_SHORT).show();
    }

    void startCompleteActivity(int seconds) {
        Intent intent = new Intent(this, CompleteActivity.class);
        intent.putExtra("value", seconds);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
