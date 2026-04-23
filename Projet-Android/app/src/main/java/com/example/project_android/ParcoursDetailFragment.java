package com.example.project_android;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParcoursDetailFragment extends Fragment {

    private static final String ARG_TITRE  = "titre";
    private static final String ARG_VILLE  = "ville";
    private static final String ARG_BUDGET = "budget";
    private static final String ARG_DUREE  = "duree";
    private static final String ARG_EFFORT = "effort";

    public static ParcoursDetailFragment newInstance(ParcourModel parcours) {
        ParcoursDetailFragment f = new ParcoursDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITRE,  parcours.getTitre());
        args.putString(ARG_VILLE,  parcours.getVille());
        args.putInt(ARG_BUDGET,    parcours.getBudget());
        args.putInt(ARG_DUREE,     parcours.getDuree());
        args.putString(ARG_EFFORT, parcours.getEffort());
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parcours_detail, container, false);

        Bundle args  = getArguments();
        String titre  = args != null ? args.getString(ARG_TITRE, "") : "";
        String ville  = args != null ? args.getString(ARG_VILLE, "") : "";
        int budget    = args != null ? args.getInt(ARG_BUDGET, 0) : 0;
        int duree     = args != null ? args.getInt(ARG_DUREE, 0) : 0;
        String effort = args != null ? args.getString(ARG_EFFORT, "") : "";

        TextView tvTitre  = view.findViewById(R.id.tv_detail_parcours_titre);
        TextView tvMeta   = view.findViewById(R.id.tv_detail_parcours_meta);
        Button   btnRetour = view.findViewById(R.id.btn_parcours_detail_retour);
        Button   btnCarte  = view.findViewById(R.id.btn_parcours_detail_carte);
        Button   btnMeteo  = view.findViewById(R.id.btn_parcours_detail_meteo);

        tvTitre.setText(titre);
        tvMeta.setText("📍 " + ville
                + "   💰 " + budget + "€"
                + "   ⏱ " + duree + "h"
                + "   💪 " + effort);

        // Charger les étapes avec galerie
        List<EtapeModel> etapes = buildEtapes(ville);
        RecyclerView recycler = view.findViewById(R.id.recycler_etapes_detail);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setAdapter(new EtapeAdapter(etapes));

        btnRetour.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );

        // Bouton carte
        btnCarte.setOnClickListener(v -> {
            TravelPathMapFragment mapFrag =
                    TravelPathMapFragment.newInstance(ville, titre);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, mapFrag)
                    .addToBackStack(null)
                    .commit();
        });

        // Bouton météo
        btnMeteo.setOnClickListener(v -> {
            WeatherFragment weatherFrag = new WeatherFragment();
            Bundle bundle = new Bundle();
            bundle.putString("ville", ville);
            weatherFrag.setArguments(bundle);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, weatherFrag)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    private List<EtapeModel> buildEtapes(String ville) {
        List<Integer> photosSample = Arrays.asList(
                android.R.drawable.ic_menu_gallery,
                android.R.drawable.ic_menu_camera,
                android.R.drawable.ic_menu_gallery
        );

        List<EtapeModel> list = new ArrayList<>();
        list.add(new EtapeModel(
                "Centre historique de " + ville,
                "9h00 - 10h30",
                "Decouvrez le coeur historique de la ville avec ses monuments emblematiques.",
                "1h30", "Depart", photosSample));
        list.add(new EtapeModel(
                "Musee principal",
                "11h00 - 12h30",
                "Visitez le musee incontournable de la ville, riche en histoire et culture.",
                "1h30", "500m", photosSample));
        list.add(new EtapeModel(
                "Dejeuner local",
                "12h30 - 14h00",
                "Pause dejeuner dans un restaurant typique de la region.",
                "1h30", "200m", photosSample));
        list.add(new EtapeModel(
                "Parc et jardins",
                "14h00 - 15h30",
                "Promenade dans les parcs et jardins de la ville.",
                "1h30", "300m", photosSample));
        list.add(new EtapeModel(
                "Quartier artistique",
                "16h00 - 17h30",
                "Explorez les galeries et rues artistiques du quartier.",
                "1h30", "400m", photosSample));
        return list;
    }
}