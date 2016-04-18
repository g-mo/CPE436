package edu.calpoly.gmo.finalproject;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private WifiManager wifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        Button wifi = (Button) findViewById(R.id.wifi);
        Button battery = (Button) findViewById(R.id.battery);

        final TextView pwrLvl = (TextView) findViewById(R.id.pwrLvl);

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

    }
}
