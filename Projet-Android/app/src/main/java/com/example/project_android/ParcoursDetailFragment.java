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

        Bundle args   = getArguments();
        String titre  = args != null ? args.getString(ARG_TITRE, "") : "";
        String ville  = args != null ? args.getString(ARG_VILLE, "") : "";
        int budget    = args != null ? args.getInt(ARG_BUDGET, 0) : 0;
        int duree     = args != null ? args.getInt(ARG_DUREE, 0) : 0;
        String effort = args != null ? args.getString(ARG_EFFORT, "") : "";

        TextView tvTitre  = view.findViewById(R.id.tv_detail_parcours_titre);
        TextView tvMeta   = view.findViewById(R.id.tv_detail_parcours_meta);
        Button btnRetour  = view.findViewById(R.id.btn_parcours_detail_retour);
        Button btnCarte   = view.findViewById(R.id.btn_parcours_detail_carte);
        Button btnMeteo   = view.findViewById(R.id.btn_parcours_detail_meteo);

        tvTitre.setText(titre);
        tvMeta.setText("📍 " + ville
                + "   💰 " + budget + "€"
                + "   ⏱ " + duree + "h"
                + "   💪 " + effort);

        RecyclerView recycler = view.findViewById(R.id.recycler_etapes_detail);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setAdapter(new EtapeAdapter(buildEtapes(ville)));

        btnRetour.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );

        btnCarte.setOnClickListener(v -> {
            TravelPathMapFragment mapFrag =
                    TravelPathMapFragment.newInstance(ville, titre);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, mapFrag)
                    .addToBackStack(null)
                    .commit();
        });

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
        List<Integer> photos = Arrays.asList(
                android.R.drawable.ic_menu_gallery,
                android.R.drawable.ic_menu_camera,
                android.R.drawable.ic_menu_gallery
        );

        // Coordonnées selon la ville
        double[][] coords = getCoordsForVille(ville);

        List<EtapeModel> list = new ArrayList<>();
        list.add(new EtapeModel(
                "Centre historique de " + ville,
                "9h00 - 10h30",
                "Decouvrez le coeur historique de la ville.",
                "1h30", "Depart", photos,
                coords[0][0], coords[0][1]));
        list.add(new EtapeModel(
                "Musee principal",
                "11h00 - 12h30",
                "Visitez le musee incontournable.",
                "1h30", "500m", photos,
                coords[1][0], coords[1][1]));
        list.add(new EtapeModel(
                "Dejeuner local",
                "12h30 - 14h00",
                "Pause dejeuner dans un restaurant typique.",
                "1h30", "200m", photos,
                coords[2][0], coords[2][1]));
        list.add(new EtapeModel(
                "Parc et jardins",
                "14h00 - 15h30",
                "Promenade dans les parcs de la ville.",
                "1h30", "300m", photos,
                coords[3][0], coords[3][1]));
        list.add(new EtapeModel(
                "Quartier artistique",
                "16h00 - 17h30",
                "Explorez les galeries et rues artistiques.",
                "1h30", "400m", photos,
                coords[4][0], coords[4][1]));
        return list;
    }

    private double[][] getCoordsForVille(String ville) {
        switch (ville.toLowerCase()) {
            case "paris":
                return new double[][]{
                        {48.8534, 2.3488},  // Hotel de Ville
                        {48.8606, 2.3376},  // Louvre
                        {48.8559, 2.3516},  // Marais
                        {48.8462, 2.3372},  // Jardin du Luxembourg
                        {48.8867, 2.3431}   // Montmartre
                };
            case "tokyo":
                return new double[][]{
                        {35.6762, 139.6503}, // Shinjuku
                        {35.7148, 139.7967}, // Senso-ji
                        {35.6654, 139.7707}, // Tsukiji
                        {35.6586, 139.7454}, // Tokyo Tower
                        {35.7023, 139.7759}  // Ueno
                };
            case "rome":
                return new double[][]{
                        {41.8902, 12.4922},  // Colisee
                        {41.9022, 12.4539},  // Vatican
                        {41.8986, 12.4768},  // Forum
                        {41.9009, 12.4833},  // Trevi
                        {41.9054, 12.4822}   // Piazza Navona
                };
            default:
                return new double[][]{
                        {48.8566, 2.3522},
                        {48.8606, 2.3376},
                        {48.8559, 2.3516},
                        {48.8462, 2.3372},
                        {48.8867, 2.3431}
                };
        }
    }
}