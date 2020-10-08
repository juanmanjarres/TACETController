package com.saskinvent.tacetcontroller;

import androidx.appcompat.app.AppCompatActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import java.util.Set;


public class MainActivity extends AppCompatActivity {

    // request code for selecting a music file
    private static final int PICK_MUSIC_FILE = 2;
    private static final String DEVICE_ADDRESS = "00:18:E4:34:DC:AC"; //device's mac address
    private final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");//
    private BluetoothDevice device;
    private BluetoothSocket socket;
    private OutputStream outputStream;
    private InputStream inputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initiate the switch
        final Switch power_switch = findViewById(R.id.power_switch);

        // create a listener for the switch when it changes
        power_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if (isChecked) {
                    device = useBluetooth();
                    System.out.println("Bluetooth enabled");
                    if (device == null){
                        //set the power switch to off and break
                        power_switch.toggle();
                        Toast.makeText(getApplicationContext(), "Bluetooth device not found",
                                Toast.LENGTH_SHORT).show();
                        //return;
                    }
                    else{
                        if (!BTconnect()){
                            Toast.makeText(getApplicationContext(), "Bluetooth device not connected",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else {
                    System.out.println("Bluetooth not enabled");
                }
            }
        });


        // Blank variables for now, implement later
        Button openfile_btn = findViewById(R.id.openfile_btn);
        Button test_btn = findViewById(R.id.test_button);

        // makes a listener for the open file button
        openfile_btn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v){
                openMusicFile();
            }
        });


        //to test that the bluetooth is working
        test_btn.setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v){
                test_bluetooth();
            }
        });
    }

    /**
     * Initiates a new activity for the user to open a music file.
     */
    // Note: should it maybe return the music file afterwards?
    private void openMusicFile(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("audio/*");

        startActivityForResult(intent, PICK_MUSIC_FILE);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

    }

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
    protected BluetoothDevice useBluetooth(){
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null){
            // Device does not support bluetooth
            Toast.makeText(getApplicationContext(),
                    "Device doesn't support Bluetooth!", Toast.LENGTH_SHORT).show();
            return null;
        }

        else {
            //enable bluetooth if it is not enabled yet
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBTIntent, 0);
            }

            //get a list of the paired devices
            Set<BluetoothDevice> pairedDevices;
            pairedDevices = bluetoothAdapter.getBondedDevices();


            if (pairedDevices.isEmpty()){
                Toast.makeText(getApplicationContext(),
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
