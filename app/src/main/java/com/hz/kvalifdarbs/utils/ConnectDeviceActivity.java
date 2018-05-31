package com.hz.kvalifdarbs.utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.hz.kvalifdarbs.Objects.Examination;
import com.hz.kvalifdarbs.Objects.Movement;
import com.hz.kvalifdarbs.R;
import com.movesense.mds.Mds;
import com.movesense.mds.MdsConnectionListener;
import com.movesense.mds.MdsException;
import com.movesense.mds.MdsNotificationListener;
import com.movesense.mds.MdsSubscription;
import com.polidea.rxandroidble.RxBleClient;
import com.polidea.rxandroidble.RxBleDevice;
import com.polidea.rxandroidble.scan.ScanSettings;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import rx.Subscription;

public class ConnectDeviceActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener, NavigationView.OnNavigationItemSelectedListener  {


    private static final String LOG_TAG = ConnectDeviceActivity.class.getSimpleName();
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    Context context;
    Toolbar toolbar;
    Double xd, yd, zd;
    Button btnCalibrate;
    Movement lastMovement;
    DatabaseReference rootRef, childRef;
    DrawerLayout drawer;
    Boolean calibrated;

    // MDS
    private Mds mMds;
    public static final String URI_CONNECTEDDEVICES = "suunto://MDS/ConnectedDevices";
    public static final String URI_EVENTLISTENER = "suunto://MDS/EventListener";
    public static final String SCHEME_PREFIX = "suunto://";

    // BleClient singleton
    static private RxBleClient mBleClient;

    // UI
    private ListView mScanResultListView;
    private ArrayList<MyScanResult> mScanResArrayList = new ArrayList<>();
    ArrayAdapter<MyScanResult> mScanResArrayAdapter;

    // Sensor subscription
    static private String URI_MEAS_ACC_13 = "/Meas/Acc/13";
    private MdsSubscription mdsSubscription;
    private String subscribedDeviceSerial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_device);
        context = getApplicationContext();
        calibrated = false;


        rootRef = FirebaseDatabase.getInstance().getReference();
        childRef = rootRef.child("Patients").child(PreferenceUtils.getId(context)).child("lastMovement");
        childRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lastMovement = dataSnapshot.getValue(Movement.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        //Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Drawer menu
        drawer = findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);

        String userId = PreferenceUtils.getId(context);
        String fullName = PreferenceUtils.getUserName(context);
        String userType = PreferenceUtils.getUserType(context);
        MethodHelper.setUpNavigationMenu(navigationView, userId, fullName, userType);

        navigationView.setNavigationItemSelectedListener(this);

        btnCalibrate = findViewById(R.id.buttonCalibrate);

        btnCalibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calibrate(xd, yd, zd);

            }
        });

        // Init Scan UI
        mScanResultListView = (ListView)findViewById(R.id.listScanResult);
        mScanResArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, mScanResArrayList);
        mScanResultListView.setAdapter(mScanResArrayAdapter);
        mScanResultListView.setOnItemLongClickListener(this);
        mScanResultListView.setOnItemClickListener(this);

        // Make sure we have all the permissions this app needs
        requestNeededPermissions();

        // Initialize Movesense MDS library
        initMds();
    }

    private RxBleClient getBleClient() {
        // Init RxAndroidBle (Ble helper library) if not yet initialized
        if (mBleClient == null)
        {
            mBleClient = RxBleClient.create(this);
        }

        return mBleClient;
    }

    private void initMds() {
        mMds = Mds.builder().build(this);
    }

    void requestNeededPermissions()
    {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);

        }
    }

    Subscription mScanSubscription;
    public void onScanClicked(View view) {
        findViewById(R.id.buttonScan).setVisibility(View.GONE);
        findViewById(R.id.buttonScanStop).setVisibility(View.VISIBLE);

        // Start with empty list
        mScanResArrayList.clear();
        mScanResArrayAdapter.notifyDataSetChanged();

        mScanSubscription = getBleClient().scanBleDevices(
                new ScanSettings.Builder()
                        // .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY) // change if needed
                        // .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES) // change if needed
                        .build()
                // add filters if needed
        )
                .subscribe(
                        scanResult -> {
                            Log.d(LOG_TAG,"scanResult: " + scanResult);

                            // Process scan result here. filter movesense devices.
                            if (scanResult.getBleDevice()!=null &&
                                    scanResult.getBleDevice().getName() != null &&
                                    scanResult.getBleDevice().getName().startsWith("Movesense")) {

                                // replace if exists already, add otherwise
                                MyScanResult msr = new MyScanResult(scanResult);
                                if (mScanResArrayList.contains(msr))
                                    mScanResArrayList.set(mScanResArrayList.indexOf(msr), msr);
                                else
                                    mScanResArrayList.add(0, msr);

                                mScanResArrayAdapter.notifyDataSetChanged();
                            }
                        },
                        throwable -> {
                            Log.e(LOG_TAG,"scan error: " + throwable);
                            // Handle an error here.

                            // Re-enable scan buttons, just like with ScanStop
                            onScanStopClicked(null);
                        }
                );
    }

    public void onScanStopClicked(View view) {
        if (mScanSubscription != null)
        {
            mScanSubscription.unsubscribe();
            mScanSubscription = null;
        }

        findViewById(R.id.buttonScan).setVisibility(View.VISIBLE);
        findViewById(R.id.buttonScanStop).setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position < 0 || position >= mScanResArrayList.size())
            return;

        MyScanResult device = mScanResArrayList.get(position);
        if (!device.isConnected()) {
            // Stop scanning
            onScanStopClicked(null);

            // And connect to the device
            connectBLEDevice(device);
            toolbar.setVisibility(View.GONE);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
        else {
            // Device is connected, trigger showing /Info
            subscribeToSensor(device.connectedSerial);
        }
    }

    private void calibrate(Double xd, Double yd, Double zd){
        calibrated = true;
        Integer x, y, z;
        x = xd.intValue();
        y = yd.intValue();
        z = zd.intValue();
        String calibrationString = String.format("%d, %d, %d", x,y,z) ;
        String s = "calibration state at: " + calibrationString;
        ((TextView)findViewById(R.id.calibrationState)).setText(s);

        //save calibration state as first movement in firebase
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentTime = Calendar.getInstance().getTime();
        final String time  = sdf.format(currentTime);

        Movement newMovement = new Movement(time, x, y, z);
        String userId = PreferenceUtils.getId(context);
        DatabaseReference thisPatientRef = FirebaseDatabase.getInstance().getReference().child("Patients").child(userId);
        thisPatientRef.child("Movements").child(time).setValue(newMovement);
        thisPatientRef.child("lastMovement").setValue(newMovement);
        MethodHelper.showToast(getApplicationContext(), "Calibration done");
        lastMovement = newMovement;

        //disable this button after click
        btnCalibrate.setEnabled(false);
    }

    private void subscribeToSensor(String connectedSerial) {
        // Clean up existing subscription (if there is one)
        if (mdsSubscription != null) {
            unsubscribe();
        }

        // Build JSON doc that describes what resource and device to subscribe
        // Here we subscribe to 13 hertz accelerometer data
        StringBuilder sb = new StringBuilder();
        String strContract = sb.append("{\"Uri\": \"").append(connectedSerial).append(URI_MEAS_ACC_13).append("\"}").toString();
        Log.d(LOG_TAG, strContract);
        final View sensorUI = findViewById(R.id.sensorUI);

        subscribedDeviceSerial = connectedSerial;

        mdsSubscription = mMds.builder().build(this).subscribe(URI_EVENTLISTENER,
                strContract, new MdsNotificationListener() {
                    @Override
                    public void onNotification(String data) {
                        Log.d(LOG_TAG, "onNotification(): " + data);

                        // If UI not enabled, do it now
                        if (sensorUI.getVisibility() == View.GONE)
                            sensorUI.setVisibility(View.VISIBLE);

                        AccDataResponse accResponse = new Gson().fromJson(data, AccDataResponse.class);
                        if (accResponse != null && accResponse.body.array.length > 0) {
                            xd = accResponse.body.array[0].x;
                            yd = accResponse.body.array[0].y;
                            zd = accResponse.body.array[0].z;
                            String accStr =
                                    String.format("%.02f, %.02f, %.02f", accResponse.body.array[0].x, accResponse.body.array[0].y, accResponse.body.array[0].z);
                        if(calibrated == true){
                            if(lastMovement != null){
                                Integer x = xd.intValue();
                                Integer y = yd.intValue();
                                Integer z = zd.intValue();

                                if(z*(-1) == lastMovement.getZ()){
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    Date currentTime = Calendar.getInstance().getTime();
                                    final String time  = sdf.format(currentTime);

                                    Movement newMovement = new Movement(time, x, y, z);
                                    String userId = PreferenceUtils.getId(context);
                                    DatabaseReference thisPatientRef = FirebaseDatabase.getInstance().getReference().child("Patients").child(userId);
                                    thisPatientRef.child("Movements").child(time).setValue(newMovement);
                                    thisPatientRef.child("lastMovement").setValue(newMovement);
                                    lastMovement = newMovement;
                                }
                            }
                        }



                            ((TextView)findViewById(R.id.sensorMsg)).setText(accStr);
                        }
                    }

                    @Override
                    public void onError(MdsException error) {
                        Log.e(LOG_TAG, "subscription onError(): ", error);
                        unsubscribe();
                    }
                });

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (position < 0 || position >= mScanResArrayList.size())
            return false;

        MyScanResult device = mScanResArrayList.get(position);

        // unsubscribe if there
        Log.d(LOG_TAG, "onItemLongClick, " + device.connectedSerial + " vs " + subscribedDeviceSerial);
        if (device.connectedSerial.equals(subscribedDeviceSerial))
            unsubscribe();

        Log.i(LOG_TAG, "Disconnecting from BLE device: " + device.macAddress);
        mMds.disconnect(device.macAddress);
        toolbar.setVisibility(View.VISIBLE);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        ((TextView)findViewById(R.id.calibrationState)).setText(null);
        btnCalibrate.setEnabled(true);

        return true;
    }

    private void connectBLEDevice(MyScanResult device) {
        RxBleDevice bleDevice = getBleClient().getBleDevice(device.macAddress);

        Log.i(LOG_TAG, "Connecting to BLE device: " + bleDevice.getMacAddress());
        mMds.connect(bleDevice.getMacAddress(), new MdsConnectionListener() {

            @Override
            public void onConnect(String s) {
                Log.d(LOG_TAG, "onConnect:" + s);
            }

            @Override
            public void onConnectionComplete(String macAddress, String serial) {
                for (MyScanResult sr : mScanResArrayList) {
                    if (sr.macAddress.equalsIgnoreCase(macAddress)) {
                        sr.markConnected(serial);
                        break;
                    }
                }
                mScanResArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(MdsException e) {
                Log.e(LOG_TAG, "onError:" + e);

                showConnectionError(e);
            }

            @Override
            public void onDisconnect(String bleAddress) {

                Log.d(LOG_TAG, "onDisconnect: " + bleAddress);
                for (MyScanResult sr : mScanResArrayList) {
                    if (bleAddress.equals(sr.macAddress))
                    {
                        // unsubscribe if was subscribed
                        if (sr.connectedSerial != null && sr.connectedSerial.equals(subscribedDeviceSerial))
                            unsubscribe();

                        sr.markDisconnected();
                    }
                }
                mScanResArrayAdapter.notifyDataSetChanged();
            }
        });
    }

    private void showConnectionError(MdsException e) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Connection Error:")
                .setMessage(e.getMessage());

        builder.create().show();
    }

    private void unsubscribe() {
        if (mdsSubscription != null) {
            mdsSubscription.unsubscribe();
            mdsSubscription = null;
        }

        subscribedDeviceSerial = null;

        // If UI not invisible, do it now
        final View sensorUI = findViewById(R.id.sensorUI);
        if (sensorUI.getVisibility() != View.GONE)
            sensorUI.setVisibility(View.GONE);

    }
    public void onUnsubscribeClicked(View view) {
        unsubscribe();
        toolbar.setVisibility(View.VISIBLE);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

    }

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        final Intents intents = new Intents(this);

        if (id == R.id.nav_my_doctors) {
            startActivity(intents.patientDoctorListView.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        } else if (id == R.id.nav_pat_exams) {
            startActivity(intents.patientExamListView.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        } else if (id == R.id.nav_pat_movements) {
            //TODO
            startActivity(intents.patientMovementListView.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        } else if (id == R.id.nav_profile) {
            startActivity(intents.patientMainMenu.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        } else if (id == R.id.nav_BT_device){
            startActivity(intents.patientDeviceManage.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        } else if (id == R.id.nav_logout) {
            MethodHelper.logOut(context, intents);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}