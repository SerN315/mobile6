package com.example.musicplayerapp.model;


public class SongList {

    public Song[] songs = new Song[3];

    public SongList() {


    }



    public Song searchById(String id){
        Song tempSong = null;
        for (int i = 0; i < songs.length; i++) {
            tempSong = songs[i];
            String tempId = tempSong.getId();
            if (tempId.equals(id)){
                return tempSong;
            }
        }
        return tempSong;
    }



}

