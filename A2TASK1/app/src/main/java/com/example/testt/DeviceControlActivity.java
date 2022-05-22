package com.example.testt;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DeviceControlActivity extends AppCompatActivity {
    //IGNORE THIS ACTIVITY!!!!!!

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

    private TextView mConnectionState;
    private TextView mDataField;
    private String mDeviceName;
    private String mDeviceAddress;

    private boolean mConnected = false;


    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.characteristics);

        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);

        // Sets up UI references.
        ((TextView) findViewById(R.id.device_address)).setText("address"+mDeviceAddress);
        ((TextView) findViewById(R.id.device_name)).setText("name"+mDeviceName);


    }

    private void updateConnectionState(final int resourceId) {
        /*
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mConnectionState.setText(resourceId);
            }
        });

         */
        Log.e("con","CONNECTED!!!!!");
    }
}
