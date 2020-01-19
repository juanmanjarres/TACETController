package com.saskinvent.tacetcontroller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Switch;

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
        if (state_powerswitch) {
            // code to start the machine
            System.out.println("Machine is on");
        }


    }
}
