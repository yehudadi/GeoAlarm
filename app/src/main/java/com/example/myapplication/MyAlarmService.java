package com.example.myapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.AlarmClock;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class MyAlarmService extends Service {

    private final int INTERVAL = 1000; // 1 second
    private Handler handler;
    private Runnable runnable;

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                Intent alarm = new Intent(AlarmClock.ACTION_SET_TIMER);
                alarm.putExtra(AlarmClock.EXTRA_LENGTH, 1);
                alarm.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
                alarm.putExtra(AlarmClock.EXTRA_MESSAGE, "You have arrived!");
                alarm.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                try {
                    getApplicationContext().startActivity(alarm);

                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "No alarm app found", Toast.LENGTH_LONG).show();
                }
                stopForeground(true); // Stop the foreground service after showing the toast once.
            }
        };

        // Create a notification channel for Android Oreo and higher.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "timer_channel";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Timer Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, channelId)
                    .setContentTitle("Timer Service")
                    .setSmallIcon(R.drawable.ic_baseline_location_on_24)
                    .build();

            startForeground(1, notification);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler.postDelayed(runnable, INTERVAL);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}


