package com.saskinvent.tacetcontroller;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    // request code for selecting a music file
    private BluetoothConnection connection;
    ArrayList<MusicFile> musicFileList;
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
                readMusic();
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
    private void readMusic(){/*
        ContentResolver contentResolver = getContentResolver();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        Cursor cursor = contentResolver.query(uri, null, selection, null, sortOrder);

        if (cursor != null && cursor.getCount() > 0) {
            musicFileList = new ArrayList<>();
            while (cursor.moveToNext()) {
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));

                // Save to audioList
                musicFileList.add(new MusicFile(data, title, album, artist));
            }
        }

        cursor.close();
        */

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("audio/*");

        startActivityForResult(intent, PICK_MUSIC_FILE);

    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        String path = data.getDataString();
        Uri musicPath = Uri.parse(path);
        //Uri musicPath = data.getData();

        musicFileList = new ArrayList<>();
        String[] proj = { MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Artists.ARTIST };

        System.out.println(musicPath);
        ContentResolver contentResolver = getContentResolver();

        Cursor tempCursor = contentResolver.query(musicPath,
                proj, null, null, null);
        tempCursor.moveToFirst();
        String title;
        String artist;

        int col_index=-1;
        //int numSongs=tempCursor.getCount();
        int currentNum=0;

        do{
            col_index = tempCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
            title = tempCursor.getString(col_index);
            col_index = tempCursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.ARTIST);
            artist = tempCursor.getString(col_index);

            currentNum++;
        }while(tempCursor.moveToNext());


        System.out.println(title);
        TextView song_name = (TextView)findViewById(R.id.currentSong);
        song_name.setText(title);

        musicFileList.add(new MusicFile(musicPath, title, artist));

        musicFileList.get(0);
    }

}
