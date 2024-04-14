package com.example.musicplayerapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayerapp.model.Recommended;

import java.util.List;

public class RecommendedAdapter extends RecyclerView.Adapter<RecommendedAdapter.ViewHolder> {

    private List<Recommended> recommendeds;

    public RecommendedAdapter(List<Recommended> recommendeds) {
        this.recommendeds = recommendeds;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommended, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Recommended recommended = recommendeds.get(position);
        holder.textViewRecommendedTitle.setText(recommended.getTitle());
        holder.imageViewRecommended.setImageResource(recommended.getImageResource());
    }

    @Override
    public int getItemCount() {
        return recommendeds.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewRecommended;
        TextView textViewRecommendedTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            imageViewRecommended = itemView.findViewById(R.id.image_view_recommended);
            textViewRecommendedTitle = itemView.findViewById(R.id.text_view_recommended_title);
        }
    }
}
