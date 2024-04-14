package com.my.samplemusicplayertest.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.my.samplemusicplayertest.R;

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {

    private List<Songs> songs;
    private Context context;

    public SongAdapter(List<Songs> songs) {
        this.context = context;
        this.songs = songs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Songs songs = this.songs.get(position);
        holder.tvTitle.setText(songs.getTitle());
        holder.tvArtist.setText(songs.getArtistName());
        holder.ivSongImage.setImageResource(songs.image());
    }


    @Override
    public int getItemCount() {
        return songs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvArtist;
        ImageView ivSongImage;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.text_view_title);
            tvArtist = itemView.findViewById(R.id.text_view_artist);
            ivSongImage = itemView.findViewById(R.id.image_view_song);
        }
    }
}
