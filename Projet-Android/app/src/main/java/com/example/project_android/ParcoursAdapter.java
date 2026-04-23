package com.example.project_android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ParcoursAdapter extends RecyclerView.Adapter<ParcoursAdapter.ParcourViewHolder> {

    private List<ParcourModel> parcoursList;

    public ParcoursAdapter(List<ParcourModel> parcoursList) {
        this.parcoursList = parcoursList;
    }

    @NonNull
    @Override
    public ParcourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_parcours, parent, false);
        return new ParcourViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParcourViewHolder holder, int position) {
        ParcourModel p = parcoursList.get(position);

        holder.tvTitre.setText(p.getTitre());
        holder.tvMeta.setText("💰 " + p.getBudget() + "€  ·  ⏱ "
                + p.getDuree() + "h  ·  💪 " + p.getEffort());

        // Etapes
        holder.layoutEtapes.removeAllViews();
        for (String etape : p.getEtapes()) {
            TextView tv = new TextView(holder.itemView.getContext());
            tv.setText(etape);
            tv.setTextSize(13f);
            tv.setPadding(0, 6, 0, 6);
            holder.layoutEtapes.addView(tv);
        }

        // Like
        holder.btnLike.setText(p.isLiked() ? "❤️ Aime" : "🤍 J'aime");
        holder.btnLike.setOnClickListener(v -> {
            p.toggleLike();
            holder.btnLike.setText(p.isLiked() ? "❤️ Aime" : "🤍 J'aime");
        });

        // Sauvegarder
        holder.btnSave.setOnClickListener(v ->
                Toast.makeText(v.getContext(), "Parcours sauvegarde", Toast.LENGTH_SHORT).show()
        );

        // Partager
        holder.btnShare.setOnClickListener(v ->
                Toast.makeText(v.getContext(), "Partage - a implementer", Toast.LENGTH_SHORT).show()
        );

        // Export PDF
        holder.btnPdf.setOnClickListener(v -> {
            try {
                File pdfFile = PdfExportHelper.generateParcoursPdf(v.getContext(), p);
                Toast.makeText(v.getContext(), "PDF genere !", Toast.LENGTH_SHORT).show();
                PdfExportHelper.openPdf(v.getContext(), pdfFile);
            } catch (IOException e) {
                Toast.makeText(v.getContext(),
                        "Erreur : " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        // Passerelle vers TravelShare
        holder.btnVoirPhotos.setOnClickListener(v -> {
            SearchFragment searchFragment = new SearchFragment();
            Bundle bundle = new Bundle();
            bundle.putString("filtre_ville", p.getVille());
            searchFragment.setArguments(bundle);
            ((androidx.fragment.app.FragmentActivity) v.getContext())
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, searchFragment)
                    .addToBackStack(null)
                    .commit();
        });

        // Voir le détail avec galerie par étape
        holder.btnDetail.setOnClickListener(v -> {
            ParcoursDetailFragment detailFrag =
                    ParcoursDetailFragment.newInstance(p);
            ((androidx.fragment.app.FragmentActivity) v.getContext())
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, detailFrag)
                    .addToBackStack(null)
                    .commit();
        });

        // Déplier/replier étapes
        holder.tvTitre.setOnClickListener(v -> {
            if (holder.layoutEtapes.getVisibility() == View.VISIBLE) {
                holder.layoutEtapes.setVisibility(View.GONE);
            } else {
                holder.layoutEtapes.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() { return parcoursList.size(); }

    static class ParcourViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitre, tvMeta, btnLike, btnSave, btnShare;
        TextView btnVoirPhotos, btnPdf, btnDetail;
        LinearLayout layoutEtapes;

        ParcourViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitre       = itemView.findViewById(R.id.tv_parcours_titre);
            tvMeta        = itemView.findViewById(R.id.tv_parcours_meta);
            layoutEtapes  = itemView.findViewById(R.id.layout_etapes);
            btnLike       = itemView.findViewById(R.id.btn_parcours_like);
            btnSave       = itemView.findViewById(R.id.btn_parcours_save);
            btnShare      = itemView.findViewById(R.id.btn_parcours_share);
            btnVoirPhotos = itemView.findViewById(R.id.btn_vers_photos);
            btnPdf        = itemView.findViewById(R.id.btn_parcours_pdf);
            btnDetail     = itemView.findViewById(R.id.btn_parcours_detail);
        }
    }
}