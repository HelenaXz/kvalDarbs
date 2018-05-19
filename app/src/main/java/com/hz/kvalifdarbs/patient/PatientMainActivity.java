package com.hz.kvalifdarbs.patient;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hz.kvalifdarbs.ListAdaptors.DeviceListAdapter;
import com.hz.kvalifdarbs.utils.Intents;
import com.hz.kvalifdarbs.utils.MethodHelper;
import com.hz.kvalifdarbs.R;
import com.hz.kvalifdarbs.utils.PreferenceUtils;

import java.util.ArrayList;

public class PatientMainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    DatabaseReference rootRef, userRef;
    Button discoverDevices, btnLogout, btnChangePass, btnToggleBluetooth;
    String patientId, patientNrString;
    TextView patientNr;
    Context context;
    BluetoothAdapter mBluetoothAdapter;
    private static final String TAG = "MainActivity";
    public ArrayList<BluetoothDevice> mBTDevices = new ArrayList<>();
    public DeviceListAdapter mDeviceListAdapter;
    ListView deviceListView;

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: called.");
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver1);
        unregisterReceiver(mBroadcastReceiver2);
        unregisterReceiver(mBroadcastReceiver4);
        //mBluetoothAdapter.cancelDiscovery();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_main_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = getApplicationContext();
        final Intents intents = new Intents(this);
        Intent i = getIntent();
        patientId = i.getStringExtra("userId");

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mBroadcastReceiver4, filter);

        rootRef = FirebaseDatabase.getInstance().getReference();
        userRef = rootRef.child("Patients").child(patientId);

        patientNr = findViewById(R.id.patientId);
        patientNrString = "Patient Nr. " + patientId;
        patientNr.setText(patientNrString);

        deviceListView = findViewById(R.id.discoveredDevices);
        //TODO set action on button click
        discoverDevices = findViewById(R.id.btnConnectDevice);
        btnChangePass = findViewById(R.id.btnChangePassword);
        btnLogout = findViewById(R.id.btnLogout);
        btnToggleBluetooth = findViewById(R.id.btnToggleBluetooth);

        deviceListView.setOnItemClickListener(PatientMainActivity.this);

        discoverDevices.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                //TODO clear list View
                btnDiscover(v);
            }
        });

        btnToggleBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableDisableBT();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceUtils.saveId("", context);
                PreferenceUtils.savePassword("", context);
                PreferenceUtils.saveUserType("", context);
                startActivity(intents.userSelect);
            }
        });

        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String currDbPassString = PreferenceUtils.getPassword(context);
                AlertDialog.Builder builder = new AlertDialog.Builder(PatientMainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_pass_change, null);
                final EditText currentPass, newPass, newPassRepeat;
                currentPass = mView.findViewById(R.id.oldPass);
                newPass = mView.findViewById(R.id.newPass);
                newPassRepeat = mView.findViewById(R.id.newPassRep);

                Button changePass = mView.findViewById(R.id.btnChangePass);
                Button cancel = mView.findViewById(R.id.btnCancel);

                builder.setView(mView);
                final AlertDialog changePassDialog = builder.create();
                changePassDialog.show();
                changePass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MethodHelper.changePassword(newPass, newPassRepeat, currentPass, currDbPassString, changePassDialog, context, userRef);
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changePassDialog.dismiss();
                    }
                });

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void btnDiscover(View view) {
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
            checkBTPermissions();
            mBluetoothAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiver2, discoverDevicesIntent);
        }
        if (!mBluetoothAdapter.isDiscovering()) {
            checkBTPermissions();
            mBluetoothAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiver2, discoverDevicesIntent);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkBTPermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0) {

                this.requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
            }
        } else {
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }

    public void enableDisableBT() {
        if (mBluetoothAdapter == null) {
            Toast toast = Toast.makeText(getApplicationContext(), "Your device can not use BlueTooth", Toast.LENGTH_SHORT);
            toast.show();
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBT);

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, BTIntent);
            Toast toast = Toast.makeText(getApplicationContext(), "Bluetooth turned ON", Toast.LENGTH_SHORT);
            toast.show();
        }
        if (mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.disable();
            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, BTIntent);
            Toast toast = Toast.makeText(getApplicationContext(), "BlueTooth turned OFF", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(mBluetoothAdapter.ACTION_STATE_CHANGED)) {

                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, mBluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG, "onReceiver; STATE OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "onReceiver; STATE TURNING OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG, "onReceiver; STATE ON");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG, "onReceiver; STATE TURNING ON");
                        break;
                }
            }
        }
    };

    private final BroadcastReceiver mBroadcastReceiver2 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d(TAG, "onReceive: ACTION FOUND.");

            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mBTDevices.add(device);
                Log.d(TAG, "onReceive: " + device.getName() + ": " + device.getAddress());
                mDeviceListAdapter = new DeviceListAdapter(context, R.layout.device_adapter_view, mBTDevices);
                deviceListView.setAdapter(mDeviceListAdapter);
            }
        }
    };

    private final BroadcastReceiver mBroadcastReceiver4 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {

                BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //3 cases
                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDED) {

                }
                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDING) {

                }
                if (mDevice.getBondState() == BluetoothDevice.BOND_NONE) {

                }
            }
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mBluetoothAdapter.cancelDiscovery();
        String deviceName = mBTDevices.get(position).getName();
        String deviceAdress = mBTDevices.get(position).getAddress();

        mBTDevices.get(position).createBond();
        Toast toast = Toast.makeText(PatientMainActivity.this, "Bond created", Toast.LENGTH_SHORT);
        toast.show();

    }
}