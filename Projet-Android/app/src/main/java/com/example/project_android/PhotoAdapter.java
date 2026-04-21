package com.example.project_android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {

    private List<PhotoModel> photoList;

    public PhotoAdapter(List<PhotoModel> photoList) {
        this.photoList = photoList;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_photo, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        PhotoModel photo = photoList.get(position);
        holder.tvTitle.setText(photo.getTitle());
        holder.tvAuthor.setText("📷 " + photo.getAuthor());
        holder.tvLocation.setText("📍 " + photo.getLocation());
        holder.tvDate.setText("🗓 " + photo.getDate());
        holder.tvLikes.setText(String.valueOf(photo.getLikes()));
        holder.ivPhoto.setImageResource(photo.getImageResId());

        // Like button
        holder.btnLike.setOnClickListener(v -> {
            photo.toggleLike();
            holder.tvLikes.setText(String.valueOf(photo.getLikes()));
            holder.btnLike.setText(photo.isLikedByUser() ? "❤️" : "🤍");
        });

        // Report button
        holder.btnReport.setOnClickListener(v ->
                Toast.makeText(v.getContext(), "Photo signalée", Toast.LENGTH_SHORT).show()
        );
    }

    @Override
    public int getItemCount() { return photoList.size(); }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPhoto;
        TextView tvTitle, tvAuthor, tvLocation, tvDate, tvLikes;
        TextView btnLike, btnReport;

        PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPhoto = itemView.findViewById(R.id.iv_photo);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvAuthor = itemView.findViewById(R.id.tv_author);
            tvLocation = itemView.findViewById(R.id.tv_location);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvLikes = itemView.findViewById(R.id.tv_likes);
            btnLike = itemView.findViewById(R.id.btn_like);
            btnReport = itemView.findViewById(R.id.btn_report);
        }
    }
}