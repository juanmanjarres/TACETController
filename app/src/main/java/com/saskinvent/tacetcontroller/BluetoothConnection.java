package com.saskinvent.tacetcontroller;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class BluetoothConnection {
    private BluetoothDevice device;
    private BluetoothSocket socket;
    private static final String DEVICE_ADDRESS = "00:18:E4:34:DC:AC"; //device's mac address
    private final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");//
    private OutputStream outputStream;
    private InputStream inputStream;

    /**
     * Connects to the bluetooth device and returns true if connected or false if not.
     * @return: a boolean representing if connected
     */
    protected boolean BTconnect(){

        boolean connected=true;
        try {
            socket = device.createRfcommSocketToServiceRecord(PORT_UUID);
            socket.connect();
        } catch (IOException e) {
            e.printStackTrace();
            connected=false;
        }

        if(connected)
        {
            try {
                outputStream = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                inputStream = socket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return connected;
    }

    protected void test_bluetooth() {

    }


    /**
     *  Checks if the Android device has bluetooth capability and contacts the
     *  Arduino if already paired.
     *
     * @return: a bluetooth device with the adapter for the Arduino
     */
    protected BluetoothDevice useBluetooth(Activity act){
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null){
            // Device does not support bluetooth
            Toast.makeText(act.getApplicationContext(),
                    "Device doesn't support Bluetooth!", Toast.LENGTH_SHORT).show();
            return null;
        }

        else {
            //enable bluetooth if it is not enabled yet
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                act.startActivityForResult(enableBTIntent, 0);
            }

            //get a list of the paired devices
            Set<BluetoothDevice> pairedDevices;
            pairedDevices = bluetoothAdapter.getBondedDevices();


            if (pairedDevices.isEmpty()){
                Toast.makeText(act.getApplicationContext(),
                        "Please pair the device first", Toast.LENGTH_SHORT).show();
                return null;
            }

            else {
                //iterate over the bluetooth devices
                for (BluetoothDevice iterator : pairedDevices) {
                    //if the device address matches, return the bluetooth device
                    if (iterator.getAddress().equals(DEVICE_ADDRESS)) {
                        return iterator;
                    }
                }
            }
            return null;
        }

    }
}
