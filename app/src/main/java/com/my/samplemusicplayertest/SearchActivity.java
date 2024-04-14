package com.my.samplemusicplayertest;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.my.samplemusicplayertest.R;
import com.my.samplemusicplayertest.ui.adapters.Recommended;
import com.my.samplemusicplayertest.ui.adapters.RecommendedAdapter;
import com.my.samplemusicplayertest.ui.adapters.Songs;
import com.my.samplemusicplayertest.ui.adapters.SongAdapter;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private List<Songs> allSongs; // Danh sách tất cả bài hát
    private List<Songs> filteredSongs; // Danh sách bài hát sau khi tìm kiếm
    private RecyclerView recyclerView; // RecyclerView cho danh sách bài hát
    private SongAdapter adapter; // Adapter cho RecyclerView
    private EditText searchEditText; // Ô tìm kiếm

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_search);

        // Khởi tạo danh sách tất cả bài hát
        allSongs = new ArrayList<>();
        allSongs.add(new Songs(1, "Title 1", 5, "", "","","",R.drawable.tb1));
        allSongs.add(new Songs(2, "Title 2", 1, "", "","","",R.drawable.tb2));
        allSongs.add(new Songs(3, "Title 3", 5, "", "","","",R.drawable.tb3));

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
        for (Songs songs : allSongs) {
            if (songs.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    songs.getArtistName().toLowerCase().contains(query.toLowerCase())) {
                filteredSongs.add(songs);
            }
        }
        adapter.notifyDataSetChanged();
    }
}
