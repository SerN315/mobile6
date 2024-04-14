package com.my.samplemusicplayertest.ui.adapters;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.my.samplemusicplayertest.R;

@Entity(tableName = "song_table")
public class Songs {
    @PrimaryKey(autoGenerate = false)
    private long id;
    private String title;
    private long duration;
    private String data;
    private String albumName;
    private String artistName;
    private String displayName;
    private int image;

    public Songs(long id, String title, long duration, String data, String albumName, String artistName, String displayName, int image) {
        this.id = id;
        this.title = title;
        this.duration = duration;
        this.data = data;
        this.albumName = albumName;
        this.artistName = artistName;
        this.displayName = displayName;
        this.image = image;
    }

    public static Songs emptySong() {
        return new Songs(-1, "", -1, "", "", "", "", R.drawable.ic_default_music);
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public long getDuration() {
        return duration;
    }

    public String getData() {
        return data;
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public int image() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

}
