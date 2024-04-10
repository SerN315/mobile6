package com.my.samplemusicplayertest.ui.adapters.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.my.mediaplayer.database.LibraryRepository;
import com.my.mediaplayer.model.Song;

public class LibraryViewModel extends AndroidViewModel {
    private LibraryRepository m_vRepository;
    private final LiveData<PagedList<Song>> m_vSongs;

    public LibraryViewModel(Application application) {
        super(application);

        this.m_vRepository = new LibraryRepository(application.getApplicationContext());
        this.m_vSongs = new LivePagedListBuilder<>(this.m_vRepository.getPagedSongs(), 30).build();
    }

    public LiveData<PagedList<Song>> getSongs() {
        return this.m_vSongs;
    }
}
