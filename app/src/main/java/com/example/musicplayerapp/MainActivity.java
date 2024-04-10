package com.example.musicplayerapp;

import android.content.Intent;
import android.os.Bundle;
import java.util.ArrayList;
import com.example.musicplayerapp.model.Song;
import com.example.musicplayerapp.model.SongList;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.musicplayerapp.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    SongList list = new SongList();
    static ArrayList<Song>  savedList= new ArrayList<Song>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void addToSavedList(View view)
    {
        String songID = view.getContentDescription().toString();
        Song song  =  list.searchById(songID);
        savedList.add(song);
    }

    public void gotoSavedListActivity(View view){
        for (int i=0;i<savedList.size();i++)
        {
            Intent intent = new Intent(this,SavedListActivity.class);
            startActivity(intent);
        }
    }
}