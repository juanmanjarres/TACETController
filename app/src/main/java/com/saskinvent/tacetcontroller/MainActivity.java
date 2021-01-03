package com.saskinvent.tacetcontroller;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    // request code for selecting a music file
    private BluetoothConnection connection;
    private static final int PICK_MUSIC_FILE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connection = new BluetoothConnection();

        //TODO does this work?
        final Activity current = this;

        // initiate the switch
        final Switch power_switch = findViewById(R.id.power_switch);

        // create a listener for the switch when it changes
        power_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if (isChecked) {
                    BluetoothDevice device = connection.useBluetooth(current);
                    System.out.println("Bluetooth enabled");
                    if (device == null){
                        //set the power switch to off and break
                        power_switch.toggle();
                        Toast.makeText(getApplicationContext(), "Bluetooth device not found",
                                Toast.LENGTH_SHORT).show();
                        //return;
                    }
                    else{
                        if (!connection.BTconnect()){
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
                connection.test_bluetooth();
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

}
