package com.saskinvent.tacetcontroller;

import android.net.Uri;

import java.io.Serializable;

public class MusicFile implements Serializable {


    private Uri data;
    private String title;
    private String artist;

    public MusicFile(Uri data, String title, String artist) {
        this.data = data;
        this.title = title;
        this.artist = artist;
    }

    public Uri getData() {
        return data;
    }

    public void setData(Uri data) {
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

}
