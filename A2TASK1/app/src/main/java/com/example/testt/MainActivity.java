package com.example.testt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.crypto.Mac;

//TODO 1.READ (how to read correctly)  2.show on the view 3.SUBSCRIBE ON CHANGES (use update in oncharChanged? how to use update button)
//3.onresume onpause ondestroy




public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainAvtivity";
    private TextView temprature_text;

    //private LeDeviceListAdapter mLeDeviceListAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt mBluetoothGatt;
    public boolean mScanning;

    private Handler mHandler;

    private static final int REQUEST_ENABLE_BT = 1;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 300000;

    public BluetoothDevice mLeDevice;

    BluetoothLeScanner bluetoothLeScanner;

    BluetoothGatt g;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        temprature_text = (TextView) findViewById(R.id.temprature_text);


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
    protected void onPause() {
        super.onPause();
        scanLeDevice(false);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        scanLeDevice(false);
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
            //sample "result"
            //ScanResult{mDevice=F6:B6:2A:79:7B:XX, mScanRecord=ScanRecord [mAdvertiseFlags=6, mServiceUuids=null, mManufacturerSpecificData={}, mServiceData={}, mTxPowerLevel=-2147483648, mDeviceName=IPVSWeather], mRssi=-67, mTimestampNanos=88264268080213}

            mLeDevice = result.getDevice();
            if (mLeDevice == null) {
                Log.e("dev", "device is null!!!");
            }


            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
               //permission check
            }

            String name = mLeDevice.getName();
            String mac = mLeDevice.getAddress();
            Log.e("name", String.valueOf(name));
            Log.e("mac", String.valueOf(mac));
            /*
            if (false && name != null && name.contentEquals("Blood Pressure")) {

                Log.e("DETECTED", "DETECTED!Blood Pressure!!!!!!!!!!!");
                connectToDevice(mLeDevice);
                scanLeDevice(false);
                /*
                final Intent intent = new Intent(getApplicationContext(), DeviceControlActivity.class);
                intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_NAME, mLeDevice.getName());
                intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_ADDRESS, mLeDevice.getAddress());
                if (mScanning) {
                    scanLeDevice(false);
                    mScanning = false;
                }
                startActivity(intent);
            }
            */

            //if(mac == "F6:B6:2A:79:7B:5D" || name=="IPVSWeather"){
            if (name != null && name.contentEquals("IPVSWeather")) {
                Log.e("DETECTED", "DETECTED!Blood Pressure!!!!!!!!!!!");
                scanLeDevice(false);
                connectToDevice(mLeDevice);

                /*
                final Intent intent = new Intent(getApplicationContext(), DeviceControlActivity.class);
                intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_NAME, mLeDevice.getName());
                intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_ADDRESS, mLeDevice.getAddress());
                if (mScanning) {
                    scanLeDevice(false);
                    mScanning = false;
                }
                startActivity(intent);
                 */

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

    public void connectToDevice(BluetoothDevice device) {
        //if (mBluetoothGatt == null) {
        Log.e("TAG", "Attempting to connect to device " + " (" + device.getAddress() + ")");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            //check permission
        }
        mBluetoothGatt = device.connectGatt(this, true, gattCallback);
        Log.e("TAG", "weather connectGatt");
        scanLeDevice(false);// will stop after  device detection

    }

    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            Log.e(TAG, "Status: " + status);
            switch (newState) {
                case BluetoothProfile.STATE_CONNECTED:
                    Log.e(TAG, "STATE_CONNECTED");
                    //BluetoothDevice device = gatt.getDevice(); // Get device
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {

                    }
                    gatt.discoverServices();
                    break;
                case BluetoothProfile.STATE_DISCONNECTED:
                    Log.e(TAG, "STATE_DISCONNECTED");
                    break;
                default:
                    Log.e(TAG, "STATE_OTHER");
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            Log.e(TAG, "onServicesDiscovered");
            //List<BluetoothGattService> services = gatt.getServices(SERVICE_UUID);
            UUID SERVICE_UUID = UUID.fromString("00000002-0000-0000-FDFD-FDFDFDFDFDFD");
            UUID WEATHER_CH_UUID = UUID.fromString("00002A1C-0000-1000-8000-00805F9B34FB");

            BluetoothGattCharacteristic weaVal = gatt.getService(SERVICE_UUID).getCharacteristic(WEATHER_CH_UUID);
            //Log.e("GattCharacteristic", weaVal.toString());
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {

            }
            gatt.readCharacteristic(weaVal);

            //gatt.readCharacteristic(services.get(0).getCharacteristics().get(0));

        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {

            byte[] charValue = characteristic.getValue();

            //byte flag = charValue[0];
            Log.e(TAG, "Characteristic: " + Arrays.toString(charValue));

            //E/MainAvtivity: Characteristic: [B@8487b0d
            //E/MainAvtivity: Characteristic: [0, -50, 9, 0, -2] ?????
            //TODO gatt.disconnect();
            //


        }
    };


}