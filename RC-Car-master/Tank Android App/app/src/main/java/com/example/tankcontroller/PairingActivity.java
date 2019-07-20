package com.example.tankcontroller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class PairingActivity extends AppCompatActivity {

    private ListView bluetoothDevices;
    private BluetoothAdapter bluetoothAdapter = null;
    private ArrayList<String> addresses = new ArrayList<>();
    public static String EXTRA_ADDRESS = "DEVICE_ADDRESS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bluetoothDevices = findViewById(R.id.device_list);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter == null){
            Toast.makeText(getApplicationContext(), "Bluetooth Device Not Available", Toast.LENGTH_LONG).show();
            finish();
        }
        else if(!bluetoothAdapter.isEnabled()){
            //Ask to the user turn the bluetooth on
            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnBTon,1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        ArrayList<String> list = new ArrayList<>();

        if (pairedDevices.size() > 0)
            for (BluetoothDevice bt : pairedDevices) {
                list.add(bt.getName() + "\n" + bt.getAddress()); //Get the device's name and the address
                addresses.add(bt.getAddress());
            }
        else
            Toast.makeText(getApplicationContext(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();

        bluetoothDevices.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list));
        bluetoothDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(PairingActivity.this, ControllerActivity.class);
                i.putExtra(EXTRA_ADDRESS, addresses.get(position));
                startActivity(i);
            }
        });
    }
}
