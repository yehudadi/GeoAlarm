package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.Timer;
import java.util.TimerTask;

public class ActiveTracking extends AppCompatActivity {

    String coordinates;
    int distanceAlert;

    Timer timer = new Timer();
    Button stopTracking;
    TextView destinationTextView;
    TextView distanceTextView;
    TextView timeTextView;
    private int distance;
    private long time = 0;
    private String destination;
    private BroadcastReceiver timeBroadcastReceiver;
    private BroadcastReceiver distanceBroadcastReceiver;
    private Intent broadcastIntent = new Intent("com.example.locationalarm.time");
    private Intent locationIntent;
    private boolean arrived = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_tracking_layout);
        Intent intent = getIntent();
        // get extras and init variables
        destination = intent.getStringExtra("name");
        coordinates = intent.getStringExtra(MainActivity.COORDINATED_TAG);
        distanceAlert = intent.getIntExtra("DISTANCE_TAG", 100);

        initViews();
        startTimeUpdater();
        startDistanceUpdater();
        initializeStopServicesBroadcast();

        Functions.checkLocationActive(this);


        locationIntent = new Intent(getApplicationContext(), AppService.class);
        locationIntent.setAction(AppService.START_TRACKING);

        locationIntent.putExtra(MainActivity.COORDINATED_TAG, coordinates);
        locationIntent.putExtra(MainActivity.DISTANCE_TAG, distanceAlert);
        startService(locationIntent);
    }
    private void initViews() {
        destinationTextView = findViewById(R.id.locationName);
        distanceTextView = findViewById(R.id.selctedDistanceName);
        timeTextView = findViewById(R.id.timerText);

        destinationTextView.setText(destination);
        distanceTextView.setText("100");
        updateDistanceText(0);
        updateTimeText();

        stopTracking = (Button) findViewById(R.id.stopTrackerBtn);
        stopTracking.setOnClickListener(v -> {
            // stop the service
            timer.cancel();
            stopService(locationIntent);
            // stop the activity
            finish();
        });

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                sendBroadcast(broadcastIntent);
            }
        }, 1000, 1000);

    }

    public void initializeStopServicesBroadcast() {
//        IntentFilter intentFilter = new IntentFilter("com.example.locationalarm.hasArrived");
//        timeBroadcastReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                timer.cancel();
//                stopService(locationIntent);
//                Toast.makeText(getApplicationContext(), "You have arrived", Toast.LENGTH_LONG).show();
//                Context cont = getApplicationContext();
//                arrived = true;
//                finish();
//            }
//        };
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            registerReceiver(timeBroadcastReceiver, intentFilter, Context.RECEIVER_NOT_EXPORTED);
//        }
    }

    public void startTimeUpdater() {
        new Thread(() -> {
            while (!arrived) {
                updateTimeText();
                try {
                    Thread.sleep(1000);
                } catch (Exception ignored) {
                }
            }
        }).start();
    }

    public void startDistanceUpdater() {
        IntentFilter intentFilter = new IntentFilter("com.example.locationalarm.distance");
        distanceBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("debuging", "dist recieved: " + distance);
                distance = intent.getIntExtra("distance", 0);
                updateDistanceText(distance);
            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(distanceBroadcastReceiver, intentFilter);
        }
    }

    public void updateDistanceText(int distance) {
        this.distance = distance;
        if (distance >= 1000) {
            float dist = (float) distance / 1000;
            distanceTextView.setText("Distance: " + dist + " kilometers");
        } else {
            distanceTextView.setText("Distance: " + distance + " meters");
        }
    }

    public void updateTimeText() {
        time++;
        String time_formated;

        int hours = (int) time / 3600;
        int minutes = (int) (time - (hours * 3600)) / 60;
        int seconds = (int) time - (hours * 3600) - (minutes * 60);

        String str_minutes = String.valueOf(minutes);
        String str_seconds = String.valueOf(seconds);
        String str_hours = String.valueOf(hours);

        if (hours < 10) {
            str_hours = "0" + hours;
        }
        if (minutes < 10) {
            str_minutes = "0" + minutes;
        }
        if (seconds < 10) {
            str_seconds = "0" + seconds;
        }

        time_formated = str_hours + ":" + str_minutes + ":" + str_seconds;


        runOnUiThread(() -> {
            timeTextView.setText("Active time: " + time_formated);
        });
    }

}

