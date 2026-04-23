package com.example.project_android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class LieuxAdapter extends RecyclerView.Adapter<LieuxAdapter.LieuViewHolder> {

    public interface OnLieuClickListener {
        void onLieuClick(int index);
    }

    private List<LieuModel> lieux;
    private OnLieuClickListener listener;

    public LieuxAdapter(List<LieuModel> lieux, OnLieuClickListener listener) {
        this.lieux    = lieux;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LieuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lieu, parent, false);
        return new LieuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LieuViewHolder holder, int position) {
        LieuModel lieu   = lieux.get(position);
        boolean isOpen   = LieuRepository.isOpenNow(lieu);
        String statut    = LieuRepository.getStatutOuverture(lieu);

        holder.tvNom.setText(lieu.getNom());
        holder.tvType.setText(lieu.getType());
        holder.tvAdresse.setText(lieu.getAdresse());
        holder.tvStatut.setText(statut);
        holder.tvStatut.setTextColor(isOpen ? 0xFF43A047 : 0xFFE53935);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onLieuClick(position);
        });
    }

    @Override
    public int getItemCount() { return lieux.size(); }

    static class LieuViewHolder extends RecyclerView.ViewHolder {
        TextView tvNom, tvType, tvAdresse, tvStatut;

        LieuViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNom     = itemView.findViewById(R.id.tv_lieu_nom_item);
            tvType    = itemView.findViewById(R.id.tv_lieu_type_item);
            tvAdresse = itemView.findViewById(R.id.tv_lieu_adresse_item);
            tvStatut  = itemView.findViewById(R.id.tv_lieu_statut_item);
        }
    }
}