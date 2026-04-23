package com.example.project_android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class EtapeAdapter extends RecyclerView.Adapter<EtapeAdapter.EtapeViewHolder> {

    private List<EtapeModel> etapes;

    public EtapeAdapter(List<EtapeModel> etapes) {
        this.etapes = etapes;
    }

    @NonNull
    @Override
    public EtapeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_etape, parent, false);
        return new EtapeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EtapeViewHolder holder, int position) {
        EtapeModel etape = etapes.get(position);

        holder.tvNumero.setText(String.valueOf(position + 1));
        holder.tvTitre.setText(etape.getTitre());
        holder.tvHoraire.setText("🕐 " + etape.getHoraire());
        holder.tvDescription.setText(etape.getDescription());
        holder.tvDuree.setText("⏱ " + etape.getDuree());
        holder.tvDistance.setText("📏 " + etape.getDistance());

        // Galerie photos
        EtapePhotoAdapter photoAdapter = new EtapePhotoAdapter(etape.getPhotos());
        holder.recyclerPhotos.setLayoutManager(
                new LinearLayoutManager(holder.itemView.getContext(),
                        LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerPhotos.setAdapter(photoAdapter);

        // Bouton carte étape
        holder.btnCarteEtape.setOnClickListener(v -> {
            EtapeMapFragment mapFrag = EtapeMapFragment.newInstance(etape);
            ((androidx.fragment.app.FragmentActivity) v.getContext())
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, mapFrag)
                    .addToBackStack(null)
                    .commit();
        });

        // Déplier/replier
        holder.tvTitre.setOnClickListener(v -> {
            if (holder.layoutDetails.getVisibility() == View.VISIBLE) {
                holder.layoutDetails.setVisibility(View.GONE);
            } else {
                holder.layoutDetails.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() { return etapes.size(); }

    static class EtapeViewHolder extends RecyclerView.ViewHolder {
        TextView tvNumero, tvTitre, tvHoraire, tvDescription, tvDuree, tvDistance;
        RecyclerView recyclerPhotos;
        View layoutDetails;
        Button btnCarteEtape;

        EtapeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNumero       = itemView.findViewById(R.id.tv_etape_numero);
            tvTitre        = itemView.findViewById(R.id.tv_etape_titre);
            tvHoraire      = itemView.findViewById(R.id.tv_etape_horaire);
            tvDescription  = itemView.findViewById(R.id.tv_etape_description);
            tvDuree        = itemView.findViewById(R.id.tv_etape_duree);
            tvDistance     = itemView.findViewById(R.id.tv_etape_distance);
            recyclerPhotos = itemView.findViewById(R.id.recycler_etape_photos);
            layoutDetails  = itemView.findViewById(R.id.layout_etape_details);
            btnCarteEtape  = itemView.findViewById(R.id.btn_etape_carte);
        }
    }
}