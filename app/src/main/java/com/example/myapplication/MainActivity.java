package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements rvInterface {

    final static String DISTANCE_TAG = "DISTANCE_TAG";
    private static final int PERMISSION_REQUEST_CODE = 699;
    static String COORDINATED_TAG = "COORDINATED_TAG";
    static DBHelper DB;
    static String address = "";
    static Geocoder geocoder;
    private FloatingActionButton addBtn;
    private ImageButton settBtn;
    private ArrayList<Item> Items;
    private MyAdaptor adaptor;


    private SharedPref sharedPreferences;

    public static String[] extractAddressAndCoordinates(EditText editLocation) {
        String x, y;
        List<Address> coordinatesOfAddress;

        address = String.valueOf(editLocation.getText());
        try {
            coordinatesOfAddress = geocoder.getFromLocationName(String.valueOf(editLocation.getText()), 1);
        } catch (Exception e) {
            return new String[]{"0", "0"};
        }

        if (notFound(coordinatesOfAddress)) {
            return new String[]{"0", "0"};
        }

        x = String.valueOf(coordinatesOfAddress.get(0).getLatitude());
        y = String.valueOf(coordinatesOfAddress.get(0).getLongitude());

        return new String[]{x, y};

    }

    private static boolean notFound(List<Address> list) {
        return list.size() == 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        geocoder = new Geocoder(this, Locale.getDefault());

        addBtn = findViewById(R.id.button);
        settBtn = findViewById(R.id.imageButton);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        DB = new DBHelper(this);
        Items = DB.selectAllItems();

        sharedPreferences = new SharedPref(this);
        DarkModeManager.setDarkMode(sharedPreferences.read(SharedPref.DARK_MODE, true), sharedPreferences);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adaptor = new MyAdaptor(this, Items, this);
        recyclerView.setAdapter(adaptor);


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CostumeDialog customDialog = new CostumeDialog(MainActivity.this, null, -1);
                customDialog.show();

            }
        });

        settBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSettings(MainActivity.this);
            }
        });

        checkAndAskAllPermissions();
    }

    private void checkAndAskAllPermissions() {
        List<String> permissionsToAsk = new ArrayList<>();

        // Add all permissions declared in the manifest to the list
        String[] permissions = {
                "android.permission.ACCESS_COARSE_LOCATION",
                "android.permission.ACCESS_FINE_LOCATION",
                "android.permission.INTERNET",
                "android.permission.WRITE_EXTERNAL_STORAGE",
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.MANAGE_EXTERNAL_STORAGE",
                "android.permission.ACCESS_BACKGROUND_LOCATION",
                "android.permission.WAKE_LOCK",
                "android.permission.VIBRATE",
                "android.permission.SET_ALARM",
                "com.android.alarm.permission.SET_ALARM", // Custom permission
                "android.permission.RECEIVE_BOOT_COMPLETED",
                "android.permission.FOREGROUND_SERVICE",
                "android.permission.FOREGROUND_SERVICE_DATA_SYNC" // Custom permission
                // Add more permissions as needed
        };

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToAsk.add(permission);
            }
        }

        if (!permissionsToAsk.isEmpty()) {
            // Convert the list to an array and request permissions
            ActivityCompat.requestPermissions(this,
                    permissionsToAsk.toArray(new String[0]),
                    PERMISSION_REQUEST_CODE);
        } else {
            // All permissions have already been granted
            // Proceed with your app logic
        }
    }


    public void openSettings(Context context) {

        Intent intent = new Intent(context, SettingsActivity.class);

        context.startActivity(intent);
    }

    @Override
    public void longClick(int pos) {

        showAlertDialog(pos);
        //what happens when long click on recycler view item
    }

    private void showAlertDialog(int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        long id = Items.get(pos).getId();

        // Set the title and message for the AlertDialog
        builder.setTitle("Confirmation").setMessage("Do you want to delete?");

        // Set the positive button and its OnClickListener
        builder.setPositiveButton("delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle positive button click (e.g., perform action)
                deletItem(id);
                Toast.makeText(getApplicationContext(), "delete", Toast.LENGTH_SHORT).show();
                dialog.dismiss(); // Dismiss the dialog if needed
            }
        });

        // Set the negative button and its OnClickListener
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle negative button click (e.g., cancel action)
                Toast.makeText(getApplicationContext(), "cancel", Toast.LENGTH_SHORT).show();
                dialog.dismiss(); // Dismiss the dialog if needed
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deletItem(Long id) {
        DB.deleteItem(id);
        recreate();
    }

}


