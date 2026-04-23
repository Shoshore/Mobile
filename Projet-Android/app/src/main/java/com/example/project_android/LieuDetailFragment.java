package com.example.project_android;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class LieuDetailFragment extends Fragment {

    private static final String ARG_VILLE     = "ville";
    private static final String ARG_LIEU_INDEX = "lieu_index";

    public static LieuDetailFragment newInstance(String ville, int lieuIndex) {
        LieuDetailFragment f = new LieuDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_VILLE, ville);
        args.putInt(ARG_LIEU_INDEX, lieuIndex);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lieu_detail, container, false);

        Bundle args    = getArguments();
        String ville   = args != null ? args.getString(ARG_VILLE, "Paris") : "Paris";
        int lieuIndex  = args != null ? args.getInt(ARG_LIEU_INDEX, 0) : 0;

        List<LieuModel> lieux = LieuRepository.getInstance().getlieuxForVille(ville);
        if (lieuIndex >= lieux.size()) lieuIndex = 0;
        LieuModel lieu = lieux.get(lieuIndex);

        TextView tvNom      = view.findViewById(R.id.tv_lieu_nom);
        TextView tvType     = view.findViewById(R.id.tv_lieu_type);
        TextView tvAdresse  = view.findViewById(R.id.tv_lieu_adresse);
        TextView tvTel      = view.findViewById(R.id.tv_lieu_tel);
        TextView tvStatut   = view.findViewById(R.id.tv_lieu_statut);
        Button btnItineraire = view.findViewById(R.id.btn_lieu_itineraire);
        Button btnRetour     = view.findViewById(R.id.btn_lieu_retour);
        RecyclerView recyclerCreneaux = view.findViewById(R.id.recycler_creneaux);

        tvNom.setText(lieu.getNom());
        tvType.setText(getTypeEmoji(lieu.getType()) + " " + lieu.getType());
        tvAdresse.setText("📍 " + lieu.getAdresse());
        tvTel.setText(lieu.getTelephone().isEmpty() ? "" : "📞 " + lieu.getTelephone());

        // Statut ouverture en temps reel
        boolean isOpen = LieuRepository.isOpenNow(lieu);
        String statut  = LieuRepository.getStatutOuverture(lieu);
        tvStatut.setText(statut);
        tvStatut.setTextColor(isOpen ? 0xFF43A047 : 0xFFE53935);
        tvStatut.setBackgroundColor(isOpen ? 0xFFE8F5E9 : 0xFFFFEBEE);

        // Creneaux
        recyclerCreneaux.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerCreneaux.setAdapter(new CreneauxAdapter(lieu.getCreneaux()));

        // Itineraire
        btnItineraire.setOnClickListener(v -> {
            String query = Uri.encode(lieu.getAdresse());
            Uri mapUri = Uri.parse("google.navigation:q=" + query);
            Intent intent = new Intent(Intent.ACTION_VIEW, mapUri);
            intent.setPackage("com.google.android.apps.maps");
            if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Uri browserUri = Uri.parse(
                        "https://www.google.com/maps/search/?api=1&query=" + query);
                startActivity(new Intent(Intent.ACTION_VIEW, browserUri));
            }
        });

        btnRetour.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );

        return view;
    }

    private String getTypeEmoji(String type) {
        switch (type.toLowerCase()) {
            case "musee":      return "🏛️";
            case "monument":   return "🏰";
            case "restaurant": return "🍽️";
            case "parc":       return "🌿";
            case "temple":     return "⛩️";
            case "marche":     return "🛒";
            default:           return "📍";
        }
    }
}