package com.example.myapplication;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.widget.Toast;

public class Functions {

    /**
     * @param service specific service to check
     * @return if service is running or not
     */
    static boolean isServiceRunning(Context context, AppService service) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo runningService : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (service.getClass().getName().equals(runningService.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    static void checkLocationActive(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(context, "Please turn on location", Toast.LENGTH_SHORT).show();

            // Create the object of AlertDialog Builder class
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("This app requires active location to work properly. turn on location?");
            builder.setTitle("Location is off");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", (dialog, which) -> {
                Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            });
            builder.setNegativeButton("No", (dialog, which) -> {
                dialog.cancel();
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

}
