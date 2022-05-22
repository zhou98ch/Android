package com.example.testt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;
import java.util.UUID;

import javax.crypto.Mac;

//TODO
//0.CONNECT
//1.READ (SHOW ON THE VIEW)
//2.SUBSCRIBE ON CHANGES

//ScanResult{mDevice=F6:B6:2A:79:7B:XX, mScanRecord=ScanRecord [mAdvertiseFlags=6, mServiceUuids=null, mManufacturerSpecificData={}, mServiceData={}, mTxPowerLevel=-2147483648, mDeviceName=IPVSWeather], mRssi=-67, mTimestampNanos=88264268080213}


public class MainActivity extends AppCompatActivity {

    //private LeDeviceListAdapter mLeDeviceListAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    public boolean mScanning;

    private Handler mHandler;

    private static final int REQUEST_ENABLE_BT = 1;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;

    public BluetoothDevice mLeDevice;

    BluetoothLeScanner bluetoothLeScanner;

    BluetoothGatt g;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mHandler = new Handler();

        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "ble_not_supported", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            //Toast.makeText(this, "BLE supported", Toast.LENGTH_SHORT).show();
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        bluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "error_bluetooth_not_supported", Toast.LENGTH_SHORT).show();
            finish();
            return;

        } else {
            //Toast.makeText(this, "BLUETOOTH supported", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_scan:
                Toast.makeText(this, "scan", Toast.LENGTH_SHORT).show();
                //TODO
                //mLeDeviceListAdapter.clear();
                scanLeDevice(true);
                break;
            case R.id.menu_stop:
                Toast.makeText(this, "stop", Toast.LENGTH_SHORT).show();
                //TODO
                scanLeDevice(false);
                break;
            case R.id.menu_connect:
                BluetoothDevice m = mLeDevice;

                //final BluetoothDevice device = mLeDeviceListAdapter.getDevice(position);
                //private
                //break;
        }
        return true;
    }

    private void scanLeDevice(final boolean enable) {


        if (enable) {
            // Stops scanning after a pre-defined scan period.

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;

                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }

                    bluetoothLeScanner.stopScan(mLeScanCallback);
                    //invalidateOptionsMenu();
                }
            }, SCAN_PERIOD);

            mScanning = true;
            bluetoothLeScanner.startScan(mLeScanCallback);
            //Log.e("res", "ahhaha");


        } else {
            mScanning = false;
            bluetoothLeScanner.stopScan(mLeScanCallback);
        }
        //invalidateOptionsMenu();
    }

    private ScanCallback mLeScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            //Log.e("name", String.valueOf(result.getScanRecord().getDeviceName()));
            mLeDevice = result.getDevice();
            if(mLeDevice==null){
                Log.e("dev","device is null!!!");
            }
            //String na = mLeDevice.getName();


            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                //Log.e("dev","no!!!");
                //return;
            }
            //ParcelUuid[] uuid = mLeDevice.getUuids();
            //String name = mLeDevice.getName();
            //Log.e("uuid", String.valueOf(uuid));
            //Log.e("name", String.valueOf(name));
            String name = mLeDevice.getName();
            String mac = mLeDevice.getAddress();
            Log.e("name", String.valueOf(name));
            Log.e("mac", String.valueOf(mac));
            if(name!=null && name.contentEquals("Blood Pressure")){

                Log.e("DETECTED","DETECTED!Blood Pressure!!!!!!!!!!!");

                final Intent intent = new Intent(getApplicationContext(), DeviceControlActivity.class);
                intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_NAME, mLeDevice.getName());
                intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_ADDRESS, mLeDevice.getAddress());
                if (mScanning) {
                    scanLeDevice(false);
                    mScanning = false;
                }
                startActivity(intent);

            }

            if(mac == "F6:B6:2A:79:7B:5D" || name=="IPVSWeather"){
                final Intent intent = new Intent(getApplicationContext(), DeviceControlActivity.class);
                intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_NAME, mLeDevice.getName());
                intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_ADDRESS, mLeDevice.getAddress());
                if (mScanning) {
                    scanLeDevice(false);
                    mScanning = false;
                }
                startActivity(intent);

            }



        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            Log.e("res", "2");
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            Log.e("res", "3");

        }

    };


}