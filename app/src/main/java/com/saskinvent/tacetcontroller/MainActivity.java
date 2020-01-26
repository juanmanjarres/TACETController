package com.saskinvent.tacetcontroller;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initiate the switch
        Switch power_switch = (Switch) findViewById(R.id.power_switch);

        // check state of the switch
        boolean state_powerswitch = power_switch.isChecked();

        // if the powerswitch is on, the machine starts
        // this does not work if the switch is turned off, maybe set an onClickListener?
        if (state_powerswitch) {
            // code to start the machine


            // int bluetoothWorks = useBluetooth(); this might not be the best way, maybe
            // change return type of the function


            System.out.println("Machine is on");
        }

        Button openfile_btn = (Button) findViewById(R.id.openfile_btn);

        //openfile_btn.setOnClickListener(new OnClickListener() {

        //});
    }


    protected int useBluetooth(){
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null){
            // Device does not support bluetooth
            return -1;
        }
        else {
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBTIntent, 0);
            }
            Set<BluetoothDevice> pairedDevices;
            pairedDevices = bluetoothAdapter.getBondedDevices();
            // here implement to access the bluetooth of the machine
            return 0;
        }

    }
}
