package com.my.mediaplayer.database;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.my.mediaplayer.model.Song;

@Database(entities = {Song.class}, version = 1)
public abstract class DB extends RoomDatabase {
    private static DB m_vInstance;

    public abstract LibraryDao getLibraryDao();

    public static synchronized DB getInstance(Context context) {
        if (m_vInstance == null) {
            m_vInstance = Room
                    .databaseBuilder(context.getApplicationContext(), DB.class, "root_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(m_vRoomCallback)
                    .build();
        }

        return m_vInstance;
    }

    private static RoomDatabase.Callback m_vRoomCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };
}
