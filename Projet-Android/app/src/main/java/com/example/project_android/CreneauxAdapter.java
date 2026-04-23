package com.example.project_android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CreneauxAdapter extends RecyclerView.Adapter<CreneauxAdapter.CreneauViewHolder> {

    private List<CreneauModel> creneaux;

    public CreneauxAdapter(List<CreneauModel> creneaux) {
        this.creneaux = creneaux;
    }

    @NonNull
    @Override
    public CreneauViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_creneau, parent, false);
        return new CreneauViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CreneauViewHolder holder, int position) {
        CreneauModel c = creneaux.get(position);
        holder.tvJour.setText(c.getJour());
        holder.tvHoraire.setText(c.getLabel());

        if (c.isFerme()) {
            holder.tvHoraire.setTextColor(0xFFE53935);
        } else {
            holder.tvHoraire.setTextColor(0xFF43A047);
        }

        // Mettre en evidence le jour actuel
        java.util.Calendar cal = java.util.Calendar.getInstance();
        String[] jours = {"Dimanche","Lundi","Mardi","Mercredi",
                "Jeudi","Vendredi","Samedi"};
        String aujourdhui = jours[cal.get(java.util.Calendar.DAY_OF_WEEK) - 1];
        if (c.getJour().equalsIgnoreCase(aujourdhui)) {
            holder.tvJour.setTextColor(0xFF1A1A2E);
            holder.tvJour.setTypeface(null, android.graphics.Typeface.BOLD);
            holder.itemView.setBackgroundColor(0xFFF5F5F5);
        } else {
            holder.tvJour.setTextColor(0xFF555555);
            holder.tvJour.setTypeface(null, android.graphics.Typeface.NORMAL);
            holder.itemView.setBackgroundColor(0xFFFFFFFF);
        }
    }

    @Override
    public int getItemCount() { return creneaux.size(); }

    static class CreneauViewHolder extends RecyclerView.ViewHolder {
        TextView tvJour, tvHoraire;

        CreneauViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJour    = itemView.findViewById(R.id.tv_creneau_jour);
            tvHoraire = itemView.findViewById(R.id.tv_creneau_horaire);
        }
    }
}