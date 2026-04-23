package com.example.project_android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class GroupeAdapter extends RecyclerView.Adapter<GroupeAdapter.GroupeViewHolder> {

    public interface OnGroupeActionListener {
        void onVoirGroupe(GroupeModel groupe);
        void onRejoindreGroupe(GroupeModel groupe);
    }

    private List<GroupeModel> groupes;
    private String currentUser;
    private OnGroupeActionListener listener;

    public GroupeAdapter(List<GroupeModel> groupes,
                         String currentUser,
                         OnGroupeActionListener listener) {
        this.groupes     = groupes;
        this.currentUser = currentUser;
        this.listener    = listener;
    }

    @NonNull
    @Override
    public GroupeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_groupe, parent, false);
        return new GroupeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupeViewHolder holder, int position) {
        GroupeModel g = groupes.get(position);

        holder.tvNom.setText(g.getNom());
        holder.tvDescription.setText(g.getDescription());
        holder.tvCreateur.setText("👤 " + g.getCreateur());
        holder.tvStats.setText("👥 " + g.getNbMembres()
                + " membres  ·  📸 " + g.getNbPhotos() + " photos");
        holder.tvVisibilite.setText(g.isEstPublic() ? "🌍 Public" : "🔒 Prive");

        boolean estMembre = g.getMembres().contains(currentUser);

        if (estMembre) {
            holder.btnRejoindre.setText("✅ Membre");
            holder.btnRejoindre.setEnabled(false);
        } else {
            holder.btnRejoindre.setText("➕ Rejoindre");
            holder.btnRejoindre.setEnabled(true);
        }

        holder.btnRejoindre.setOnClickListener(v -> {
            if (listener != null) listener.onRejoindreGroupe(g);
        });

        holder.btnVoir.setOnClickListener(v -> {
            if (listener != null) listener.onVoirGroupe(g);
        });
    }

    @Override
    public int getItemCount() { return groupes.size(); }

    static class GroupeViewHolder extends RecyclerView.ViewHolder {
        TextView tvNom, tvDescription, tvCreateur, tvStats, tvVisibilite;
        Button btnRejoindre, btnVoir;

        GroupeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNom         = itemView.findViewById(R.id.tv_groupe_nom);
            tvDescription = itemView.findViewById(R.id.tv_groupe_description);
            tvCreateur    = itemView.findViewById(R.id.tv_groupe_createur);
            tvStats       = itemView.findViewById(R.id.tv_groupe_stats);
            tvVisibilite  = itemView.findViewById(R.id.tv_groupe_visibilite);
            btnRejoindre  = itemView.findViewById(R.id.btn_rejoindre_groupe);
            btnVoir       = itemView.findViewById(R.id.btn_voir_groupe);
        }
    }
}