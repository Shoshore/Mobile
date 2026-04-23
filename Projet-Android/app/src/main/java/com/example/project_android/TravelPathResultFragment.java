package com.example.project_android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
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

    private String ville;
    private int budget;
    private int duree;
    private boolean effort;
    private RecyclerView recycler;
    private TextView tvTitre;

    public static TravelPathResultFragment newInstance(
            String ville, int budget, int duree,
            boolean restaurant, boolean culture,
            boolean loisirs, boolean decouverte,
            boolean effortFaible) {
        TravelPathResultFragment f = new TravelPathResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_VILLE,  ville);
        args.putInt(ARG_BUDGET,    budget);
        args.putInt(ARG_DUREE,     duree);
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

        Bundle args = getArguments();
        ville  = args != null ? args.getString(ARG_VILLE, "Paris") : "Paris";
        budget = args != null ? args.getInt(ARG_BUDGET, 100) : 100;
        duree  = args != null ? args.getInt(ARG_DUREE, 5) : 5;
        effort = args != null && args.getBoolean(ARG_EFFORT, false);

        tvTitre = view.findViewById(R.id.tv_result_title);
        tvTitre.setText("Parcours pour " + ville);

        recycler = view.findViewById(R.id.recycler_parcours);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setAdapter(new ParcoursAdapter(
                generateParcours(ville, budget, duree, effort, null)));

        // Retour
        view.findViewById(R.id.btn_retour).setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );

        // Voir photos TravelShare
        view.findViewById(R.id.btn_voir_photos).setOnClickListener(v -> {
            SearchFragment sf = new SearchFragment();
            Bundle bundle = new Bundle();
            bundle.putString("filtre_ville", ville);
            sf.setArguments(bundle);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, sf)
                    .addToBackStack(null)
                    .commit();
        });

        // Voir carte
        view.findViewById(R.id.btn_voir_carte).setOnClickListener(v -> {
            TravelPathMapFragment mapFrag =
                    TravelPathMapFragment.newInstance(ville, "Parcours");
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, mapFrag)
                    .addToBackStack(null)
                    .commit();
        });

        // Météo
        view.findViewById(R.id.btn_meteo_result).setOnClickListener(v -> {
            WeatherFragment wf = new WeatherFragment();
            Bundle bundle = new Bundle();
            bundle.putString("ville", ville);
            wf.setArguments(bundle);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, wf)
                    .addToBackStack(null)
                    .commit();
        });

        // --- REGENERER avec ajustements ---
        view.findViewById(R.id.btn_regenerer).setOnClickListener(v -> {
            RegenerateDialogFragment dialog = new RegenerateDialogFragment();
            dialog.setOnRegenerateListener(ajustements -> {
                // Mettre a jour budget et duree
                budget = ajustements.getBudget();
                duree  = ajustements.getDuree();

                // Regenerer les parcours avec les ajustements
                List<ParcourModel> newParcours =
                        generateParcours(ville, budget, duree, effort, ajustements);
                recycler.setAdapter(new ParcoursAdapter(newParcours));
                tvTitre.setText("Parcours pour " + ville
                        + " (" + budget + "€ · " + duree + "h)");

                Toast.makeText(getContext(),
                        "Parcours regeneres !", Toast.LENGTH_SHORT).show();
            });
            dialog.show(getParentFragmentManager(), "regen_dialog");
        });

        return view;
    }

    private List<ParcourModel> generateParcours(String ville, int budget,
                                                int duree, boolean effort, AjustementModel aj) {
        List<ParcourModel> list = new ArrayList<>();

        list.add(new ParcourModel(
                "🟢 Parcours economique", ville,
                (int)(budget * 0.5), (int)(duree * 0.8),
                effort ? "Faible" : "Modere",
                buildEtapes(ville, "economique", aj)));

        list.add(new ParcourModel(
                "🟡 Parcours equilibre", ville,
                (int)(budget * 0.75), duree,
                effort ? "Faible" : "Modere",
                buildEtapes(ville, "equilibre", aj)));

        list.add(new ParcourModel(
                "🔴 Parcours confort", ville,
                budget, (int)(duree * 1.2),
                "Faible",
                buildEtapes(ville, "confort", aj)));

        return list;
    }

    private List<String> buildEtapes(String ville, String type, AjustementModel aj) {
        List<String> etapes = new ArrayList<>();

        // Etapes de base
        switch (type) {
            case "economique":
                etapes.add("🌅 Matin : Visite du centre historique de " + ville);
                etapes.add("🥪 Midi : Pique-nique au parc");
                etapes.add("🏛️ Apres-midi : Musee gratuit");
                break;
            case "equilibre":
                etapes.add("🌅 Matin : Visite guidee de " + ville);
                etapes.add("🍽️ Midi : Restaurant local (~15 EUR)");
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

        // Ajustements supplementaires
        if (aj != null) {
            if (aj.isPlusMusees()) {
                etapes.add("🏛️ Bonus : Musee supplementaire recommande");
            }
            if (aj.isPlusRestos()) {
                etapes.add("🍽️ Bonus : Arret gastronomique local");
            }
            if (aj.isMoinsMarche()) {
                etapes.add("🚌 Transport : Navette entre les etapes");
            }
            if (aj.isEviterFoule()) {
                etapes.add("🧘 Conseil : Visites en debut de journee");
            }
        }

        return etapes;
    }
}