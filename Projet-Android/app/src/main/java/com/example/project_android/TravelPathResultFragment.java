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
import java.util.List;

public class TravelPathResultFragment extends Fragment {

    private static final String ARG_VILLE  = "ville";
    private static final String ARG_BUDGET = "budget";
    private static final String ARG_DUREE  = "duree";
    private static final String ARG_EFFORT = "effort";

    public static TravelPathResultFragment newInstance(
            String ville, int budget, int duree,
            boolean restaurant, boolean culture,
            boolean loisirs, boolean decouverte, boolean effortFaible) {
        TravelPathResultFragment f = new TravelPathResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_VILLE, ville);
        args.putInt(ARG_BUDGET, budget);
        args.putInt(ARG_DUREE, duree);
        args.putBoolean(ARG_EFFORT, effortFaible);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_travelpath_result, container, false);

        Bundle args  = getArguments();
        String ville = args != null ? args.getString(ARG_VILLE, "Paris") : "Paris";
        int budget   = args != null ? args.getInt(ARG_BUDGET, 100) : 100;
        int duree    = args != null ? args.getInt(ARG_DUREE, 5) : 5;
        boolean effort = args != null && args.getBoolean(ARG_EFFORT, false);

        TextView tvTitre = view.findViewById(R.id.tv_result_title);
        tvTitre.setText("Parcours pour " + ville);

        List<ParcourModel> parcours = generateParcours(ville, budget, duree, effort);

        RecyclerView recycler = view.findViewById(R.id.recycler_parcours);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setAdapter(new ParcoursAdapter(parcours));

        // Retour au formulaire
        Button btnRetour = view.findViewById(R.id.btn_retour);
        btnRetour.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );
        Button btnMeteoResult = view.findViewById(R.id.btn_meteo_result);
        btnMeteoResult.setOnClickListener(v -> {
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
        // --- PASSERELLE : voir les photos de cette ville dans TravelShare ---
        Button btnVoirPhotos = view.findViewById(R.id.btn_voir_photos);
        btnVoirPhotos.setOnClickListener(v -> {
            SearchFragment searchFragment = new SearchFragment();
            Bundle bundle = new Bundle();
            bundle.putString("filtre_ville", ville);
            searchFragment.setArguments(bundle);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, searchFragment)
                    .addToBackStack(null)
                    .commit();
        });
        Button btnVoirCarte = view.findViewById(R.id.btn_voir_carte);
        btnVoirCarte.setOnClickListener(v -> {
            // Prend le premier parcours (economique par defaut)
            TravelPathMapFragment mapFrag = TravelPathMapFragment.newInstance(ville, "Parcours");
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, mapFrag)
                    .addToBackStack(null)
                    .commit();
        });
        return view;
    }

    private List<ParcourModel> generateParcours(String ville, int budget, int duree, boolean effort) {
        List<ParcourModel> list = new ArrayList<>();
        list.add(new ParcourModel(
                "🟢 Parcours economique", ville,
                (int)(budget * 0.5), (int)(duree * 0.8),
                effort ? "Faible" : "Modere",
                buildEtapes(ville, "economique")
        ));
        list.add(new ParcourModel(
                "🟡 Parcours equilibre", ville,
                (int)(budget * 0.75), duree,
                effort ? "Faible" : "Modere",
                buildEtapes(ville, "equilibre")
        ));
        list.add(new ParcourModel(
                "🔴 Parcours confort", ville,
                budget, (int)(duree * 1.2),
                "Faible",
                buildEtapes(ville, "confort")
        ));
        return list;
    }

    private List<String> buildEtapes(String ville, String type) {
        List<String> etapes = new ArrayList<>();
        switch (type) {
            case "economique":
                etapes.add("🌅 Matin : Visite du centre historique de " + ville);
                etapes.add("🥪 Midi : Pique-nique au parc");
                etapes.add("🏛️ Apres-midi : Musee gratuit");
                break;
            case "equilibre":
                etapes.add("🌅 Matin : Visite guidee de " + ville);
                etapes.add("🍽️ Midi : Restaurant local (~15€)");
                etapes.add("🎨 Apres-midi : Galerie d'art");
                etapes.add("☕ Soir : Cafe en terrasse");
                break;
            case "confort":
                etapes.add("🌅 Matin : Tour en taxi prive de " + ville);
                etapes.add("🍷 Midi : Restaurant gastronomique");
                etapes.add("🏰 Apres-midi : Visite premium avec guide");
                etapes.add("🎭 Soir : Spectacle ou concert");
                break;
        }
        return etapes;
    }
}