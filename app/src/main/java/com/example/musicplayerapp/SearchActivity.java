package com.example.musicplayerapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.musicplayerapp.model.Recommended;
import com.example.musicplayerapp.model.Song;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private List<Song> allSongs; // Danh sách tất cả bài hát
    private List<Song> filteredSongs; // Danh sách bài hát sau khi tìm kiếm
    private RecyclerView recyclerView; // RecyclerView cho danh sách bài hát
    private SongAdapter adapter; // Adapter cho RecyclerView
    private EditText searchEditText; // Ô tìm kiếm

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Khởi tạo danh sách tất cả bài hát
        allSongs = new ArrayList<>();
        allSongs.add(new Song("1", "Title 1", "Artist 1", "file_link_1", 3.5, R.drawable.tb1));
        allSongs.add(new Song("2", "Title 2", "Artist 2", "file_link_2", 4.2, R.drawable.tb2));
        allSongs.add(new Song("3", "Title 3", "Artist 3", "file_link_3", 5.1, R.drawable.tb3));

        // Khởi tạo danh sách bài hát sau khi tìm kiếm
        filteredSongs = new ArrayList<>(allSongs);

        // Tạo RecyclerView từ layout
        recyclerView = findViewById(R.id.view1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager1);

        RecyclerView recyclerViewRecommended = findViewById(R.id.view2);
        List<Recommended> recommendeds = new ArrayList<>();
        recommendeds.add(new Recommended("K-Pop", R.drawable.kpop_image));
        recommendeds.add(new Recommended("Pop", R.drawable.pop_image));
        recommendeds.add(new Recommended("V-Pop", R.drawable.vpop_image));
        recommendeds.add(new Recommended("Anime", R.drawable.anime_image));
        recommendeds.add(new Recommended("Rock", R.drawable.rock_image));
        recommendeds.add(new Recommended("Jazz", R.drawable.jazz_image));


        RecommendedAdapter recommendedAdapter = new RecommendedAdapter(recommendeds);
        recyclerViewRecommended.setAdapter(recommendedAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewRecommended.setLayoutManager(layoutManager);
        GridLayoutManager layoutManager2 = new GridLayoutManager(this, 2);
        recyclerViewRecommended.setLayoutManager(layoutManager2);

        // Tạo và thiết lập adapter cho RecyclerView
        adapter = new SongAdapter(filteredSongs);
        recyclerView.setAdapter(adapter);

        // Khởi tạo và thiết lập ô tìm kiếm
        searchEditText = findViewById(R.id.editTextText);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void filter(String query) {
        filteredSongs.clear();
        for (Song song : allSongs) {
            if (song.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    song.getArtist().toLowerCase().contains(query.toLowerCase())) {
                filteredSongs.add(song);
            }
        }
        adapter.notifyDataSetChanged();
    }
}
