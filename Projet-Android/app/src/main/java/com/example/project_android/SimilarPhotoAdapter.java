package com.example.project_android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SimilarPhotoAdapter extends
        RecyclerView.Adapter<SimilarPhotoAdapter.SimilarViewHolder> {

    public interface OnPhotoClickListener {
        void onClick(PhotoModel photo);
    }

    private List<SimilarityEngine.ScoredPhoto> items;
    private OnPhotoClickListener listener;

    public SimilarPhotoAdapter(List<SimilarityEngine.ScoredPhoto> items,
                               OnPhotoClickListener listener) {
        this.items    = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SimilarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_similar_photo, parent, false);
        return new SimilarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SimilarViewHolder holder, int position) {
        SimilarityEngine.ScoredPhoto sp = items.get(position);
        PhotoModel p = sp.getPhoto();

        holder.ivPhoto.setImageResource(p.getImageResId());
        holder.tvTitle.setText(p.getTitle());
        holder.tvAuthor.setText("📷 " + p.getAuthor());
        holder.tvLocation.setText("📍 " + p.getLocation());
        holder.tvScore.setText(sp.getScoreLabel());

        // Couleur selon score
        if (sp.getScore() >= 0.7) {
            holder.tvScore.setBackgroundColor(0xFF4CAF50);
        } else if (sp.getScore() >= 0.4) {
            holder.tvScore.setBackgroundColor(0xFFFF9800);
        } else {
            holder.tvScore.setBackgroundColor(0xFF9E9E9E);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClick(p);
        });
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class SimilarViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPhoto;
        TextView tvTitle, tvAuthor, tvLocation, tvScore;

        SimilarViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPhoto    = itemView.findViewById(R.id.iv_similar_photo);
            tvTitle    = itemView.findViewById(R.id.tv_similar_title);
            tvAuthor   = itemView.findViewById(R.id.tv_similar_author);
            tvLocation = itemView.findViewById(R.id.tv_similar_location);
            tvScore    = itemView.findViewById(R.id.tv_similar_score);
        }
    }
}