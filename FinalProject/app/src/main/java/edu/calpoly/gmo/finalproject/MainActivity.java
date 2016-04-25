package edu.calpoly.gmo.finalproject;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import im.delight.android.location.SimpleLocation;

public class MainActivity extends AppCompatActivity implements LocationListener{

    private WifiManager wifiManager;
    private LocationManager locationManager;
    private Button wifi, battery, gps, showCoord;
    private TextView pwrLvl, latitude, longitude;
    private LocationListener locationListener;
    //private SimpleLocation location;
    private String provider;
    private FileOutputStream output;
    private FileInputStream input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initManagers();
        initViews();
        initListeners();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }

        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location= locationManager.getLastKnownLocation(provider);

        // Initialize the location fields
        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        } else {
            latitude.setText("Location not available");
            longitude.setText("Location not available");
        }


    }

    public void initManagers() {
        wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        //locManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        //location = new SimpleLocation(this, true, true, 1000);
    }

    public void initViews() {
        wifi = (Button) findViewById(R.id.wifi);
        battery = (Button) findViewById(R.id.battery);
        gps = (Button) findViewById(R.id.gps);
        showCoord = (Button) findViewById(R.id.showCoord);
        pwrLvl = (TextView) findViewById(R.id.pwrLvl);
        latitude = (TextView) findViewById(R.id.latitude);
        longitude = (TextView) findViewById(R.id.longitude);
    }

    public void initListeners() {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        final Intent batteryStatus = this.registerReceiver(null, ifilter);

        wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wifiManager.isWifiEnabled())
                    wifiManager.setWifiEnabled(false);
                else
                    wifiManager.setWifiEnabled(true);
            }
        });

        battery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer level =  batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);

                pwrLvl.setText(level.toString() + "%");
            }
        });

        gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String coords = new String("(" + latitude.getText() + ", " + longitude.getText() + ")");
                    output = MainActivity.this.openFileOutput("tasks", MODE_APPEND);

                    output.write(coords.getBytes());
                    output.close();
                    Toast.makeText(MainActivity.this, "Saved Coords", Toast.LENGTH_SHORT).show();
                }
                catch(FileNotFoundException e) {
                    System.err.println("FileNotFoundException: " + e.getMessage());
                }
                catch(IOException e) {
                    System.err.println("IOException: " + e.getMessage());
                }
            }
        });

        showCoord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    byte[] buffer = new byte[50];
                    String coord;
                    input = MainActivity.this.openFileInput("tasks");

                    input.read(buffer, 0, 50);
                    coord = buffer.toString();
                    input.close();
                    Toast.makeText(MainActivity.this, coord, Toast.LENGTH_SHORT).show();
                }
                catch(FileNotFoundException e) {
                    System.err.println("FileNotFoundException: " + e.getMessage());
                }
                catch(IOException e) {
                    System.err.println("IOException: " + e.getMessage());
                }
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {

        latitude.setText(((Double) location.getLatitude()).toString());
        longitude.setText(((Double) location.getLongitude()).toString());
    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle b) {

    }

    @Override
    protected void onResume() {
        super.onResume();

        // make the device update its location
        //location.beginUpdates();
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
        locationManager.requestLocationUpdates(provider, 400, 1, this);
        // ...
    }

    @Override
    protected void onPause() {
        // stop location updates (saves battery)
        //location.endUpdates();

        // ...
        super.onPause();
        locationManager.removeUpdates(this);
    }

//    private class MyLocationListener implements LocationListener {
//        @Override
//        public void onLocationChanged(Location location) {
//            latitude.setText(((Double) location.getLatitude()).toString());
//            longitude.setText(((Double) location.getLongitude()).toString());
//        }
//
//        @Override
//        public void onProviderDisabled(String s) {
//
//        }
//
//        @Override
//        public void onProviderEnabled(String s) {
//
//        }
//
//        @Override
//        public void onStatusChanged(String s, int i, Bundle b) {
//
//        }
//    }
}


