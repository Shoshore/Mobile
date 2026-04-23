package com.example.project_android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class EtapePhotoAdapter extends RecyclerView.Adapter<EtapePhotoAdapter.PhotoViewHolder> {

    private List<Integer> photoResIds;

    public EtapePhotoAdapter(List<Integer> photoResIds) {
        this.photoResIds = photoResIds;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_etape_photo, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        holder.ivEtapePhoto.setImageResource(photoResIds.get(position));
    }

    @Override
    public int getItemCount() { return photoResIds.size(); }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView ivEtapePhoto;

        PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            ivEtapePhoto = itemView.findViewById(R.id.iv_etape_photo);
        }
    }
}