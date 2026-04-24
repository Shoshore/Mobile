package com.example.project_android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MyPhotosAdapter extends
        RecyclerView.Adapter<MyPhotosAdapter.MyPhotoViewHolder> {

    public interface OnMyPhotoActionListener {
        void onVoir(PhotoModel photo);
        void onSupprimer(int index);
    }

    private List<PhotoModel> photos;
    private OnMyPhotoActionListener listener;

    public MyPhotosAdapter(List<PhotoModel> photos,
                           OnMyPhotoActionListener listener) {
        this.photos   = photos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyPhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_my_photo, parent, false);
        return new MyPhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPhotoViewHolder holder, int position) {
        PhotoModel photo = photos.get(position);

        holder.ivPhoto.setImageResource(photo.getImageResId());
        holder.tvTitle.setText(photo.getTitle());
        holder.tvLocation.setText("📍 " + photo.getLocation());
        holder.tvDate.setText("🗓 " + photo.getDate());
        holder.tvLikes.setText("❤️ " + photo.getLikes());

        // Clic → voir la photo
        holder.ivPhoto.setOnClickListener(v -> {
            if (listener != null) listener.onVoir(photo);
        });
        holder.tvTitle.setOnClickListener(v -> {
            if (listener != null) listener.onVoir(photo);
        });

        // Suppression avec confirmation
        holder.btnSupprimer.setOnClickListener(v -> {
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Supprimer la photo")
                    .setMessage("Voulez-vous vraiment supprimer \"" +
                            photo.getTitle() + "\" ?")
                    .setPositiveButton("Supprimer", (dialog, which) -> {
                        if (listener != null) {
                            listener.onSupprimer(holder.getAdapterPosition());
                        }
                    })
                    .setNegativeButton("Annuler", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() { return photos.size(); }

    static class MyPhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPhoto;
        TextView tvTitle, tvLocation, tvDate, tvLikes, btnSupprimer;

        MyPhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPhoto      = itemView.findViewById(R.id.iv_my_photo);
            tvTitle      = itemView.findViewById(R.id.tv_my_photo_title);
            tvLocation   = itemView.findViewById(R.id.tv_my_photo_location);
            tvDate       = itemView.findViewById(R.id.tv_my_photo_date);
            tvLikes      = itemView.findViewById(R.id.tv_my_photo_likes);
            btnSupprimer = itemView.findViewById(R.id.btn_my_photo_supprimer);
        }
    }
}