package com.example.project_android;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchFragment extends Fragment {

    private PhotoAdapter adapter;
    private List<PhotoModel> allPhotos;
    private List<PhotoModel> filteredPhotos;
    private EditText searchBar;
    private FilterCriteria currentCriteria = new FilterCriteria();
    private TextView tvActiveFilters;

    // Launcher reconnaissance vocale
    private final ActivityResultLauncher<Intent> voiceLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == android.app.Activity.RESULT_OK
                        && result.getData() != null) {
                    ArrayList<String> matches = result.getData()
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if (matches != null && !matches.isEmpty()) {
                        String query = matches.get(0);
                        searchBar.setText(query);
                        searchPhotos(query);
                        Toast.makeText(getContext(),
                                "Recherche : " + query, Toast.LENGTH_SHORT).show();
                    }
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        allPhotos      = getAllPhotos();
        filteredPhotos = new ArrayList<>(allPhotos);
        tvActiveFilters = view.findViewById(R.id.tv_active_filters);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_photos);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new PhotoAdapter(filteredPhotos);
        recyclerView.setAdapter(adapter);

        // Clic photo → fiche detail
        adapter.setOnPhotoClickListener(photo -> {
            PhotoDetailFragment detail = PhotoDetailFragment.newInstance(photo);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, detail)
                    .addToBackStack(null)
                    .commit();
        });

        // Passerelle TravelPath
        adapter.setOnPhotoActionListener(location -> {
            String ville = location.contains(",")
                    ? location.split(",")[0].trim() : location;
            TravelPathFragment f = new TravelPathFragment();
            Bundle args = new Bundle();
            args.putString("ville_prefillee", ville);
            f.setArguments(args);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, f)
                    .addToBackStack(null)
                    .commit();
        });

        // Filtres rapides
        view.findViewById(R.id.filter_all).setOnClickListener(v -> resetFilter());
        view.findViewById(R.id.filter_nature).setOnClickListener(v -> filterBy("nature"));
        view.findViewById(R.id.filter_ville).setOnClickListener(v -> filterBy("ville"));
        view.findViewById(R.id.filter_food).setOnClickListener(v -> filterBy("food"));
        view.findViewById(R.id.filter_culture).setOnClickListener(v -> filterBy("culture"));

        // Bouton filtres avances
        view.findViewById(R.id.btn_advanced_filter).setOnClickListener(v -> {
            AdvancedFilterFragment filterFrag = new AdvancedFilterFragment();
            filterFrag.setOnFiltersAppliedListener(criteria -> {
                currentCriteria = criteria;
                applyAdvancedFilters(criteria);
                updateFilterBadge(criteria);
            });
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, filterFrag)
                    .addToBackStack(null)
                    .commit();
        });

        // Recherche texte
        searchBar = view.findViewById(R.id.et_search);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchPhotos(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        // Bouton micro
        ImageButton btnVoice = view.findViewById(R.id.btn_voice_search);
        btnVoice.setOnClickListener(v -> startVoiceSearch());

        // Passerelle depuis TravelPath
        Bundle args = getArguments();
        if (args != null && args.containsKey("filtre_ville")) {
            String ville = args.getString("filtre_ville");
            searchBar.setText(ville);
            searchPhotos(ville);
        }

        return view;
    }

    private void applyAdvancedFilters(FilterCriteria criteria) {
        filteredPhotos.clear();
        for (PhotoModel p : allPhotos) {
            boolean match = true;

            // Filtre auteur
            if (!criteria.auteur.isEmpty() &&
                    !p.getAuthor().toLowerCase().contains(criteria.auteur.toLowerCase())) {
                match = false;
            }

            // Filtre lieu
            if (!criteria.lieu.isEmpty() &&
                    !p.getLocation().toLowerCase().contains(criteria.lieu.toLowerCase())) {
                match = false;
            }

            // Filtre type lieu
            if (!criteria.typeLieu.isEmpty() &&
                    !p.getTitle().toLowerCase().contains(criteria.typeLieu.toLowerCase()) &&
                    !p.getLocation().toLowerCase().contains(criteria.typeLieu.toLowerCase())) {
                match = false;
            }

            // Filtre periode (sur l'annee dans la date)
            if (!criteria.periodeDebut.isEmpty()) {
                try {
                    int anneeDebut = Integer.parseInt(criteria.periodeDebut);
                    int anneeFin   = criteria.periodeFin.isEmpty()
                            ? 9999
                            : Integer.parseInt(criteria.periodeFin);
                    // Extraire l'annee de la date de la photo
                    String dateStr = p.getDate();
                    int anneePhoto = extractYear(dateStr);
                    if (anneePhoto < anneeDebut || anneePhoto > anneeFin) {
                        match = false;
                    }
                } catch (NumberFormatException ignored) {}
            }

            if (match) filteredPhotos.add(p);
        }

        if (filteredPhotos.isEmpty()) {
            filteredPhotos.addAll(allPhotos);
            Toast.makeText(getContext(),
                    "Aucun resultat, affichage de toutes les photos", Toast.LENGTH_SHORT).show();
        }
        adapter.notifyDataSetChanged();
    }

    private int extractYear(String date) {
        // Format attendu : "Mois YYYY" ex: "Jan 2024"
        try {
            String[] parts = date.split(" ");
            return Integer.parseInt(parts[parts.length - 1]);
        } catch (Exception e) {
            return 2024;
        }
    }

    private void updateFilterBadge(FilterCriteria criteria) {
        if (criteria.isEmpty()) {
            tvActiveFilters.setVisibility(View.GONE);
        } else {
            List<String> actifs = new ArrayList<>();
            if (!criteria.auteur.isEmpty())       actifs.add("Auteur: " + criteria.auteur);
            if (!criteria.lieu.isEmpty())         actifs.add("Lieu: " + criteria.lieu);
            if (!criteria.typeLieu.isEmpty())     actifs.add("Type: " + criteria.typeLieu);
            if (!criteria.periodeDebut.isEmpty()) actifs.add("Periode: " + criteria.periodeDebut
                    + (criteria.periodeFin.isEmpty() ? "" : "-" + criteria.periodeFin));
            if (criteria.rayonKm > 0)             actifs.add("Rayon: " + criteria.rayonKm + "km");

            tvActiveFilters.setText("Filtres actifs : " + String.join(" · ", actifs));
            tvActiveFilters.setVisibility(View.VISIBLE);
        }
    }

    private void filterBy(String type) {
        filteredPhotos.clear();
        for (PhotoModel p : allPhotos) {
            if (p.getTitle().toLowerCase().contains(type) ||
                    p.getLocation().toLowerCase().contains(type)) {
                filteredPhotos.add(p);
            }
        }
        if (filteredPhotos.isEmpty()) filteredPhotos.addAll(allPhotos);
        adapter.notifyDataSetChanged();
    }

    private void resetFilter() {
        currentCriteria = new FilterCriteria();
        tvActiveFilters.setVisibility(View.GONE);
        filteredPhotos.clear();
        filteredPhotos.addAll(allPhotos);
        adapter.notifyDataSetChanged();
    }

    private void searchPhotos(String query) {
        filteredPhotos.clear();
        if (query.isEmpty()) {
            filteredPhotos.addAll(allPhotos);
        } else {
            String q = query.toLowerCase();
            for (PhotoModel p : allPhotos) {
                if (p.getTitle().toLowerCase().contains(q) ||
                        p.getAuthor().toLowerCase().contains(q) ||
                        p.getLocation().toLowerCase().contains(q)) {
                    filteredPhotos.add(p);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void startVoiceSearch() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.FRENCH);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Dites un lieu, un auteur ou un theme...");
        try {
            voiceLauncher.launch(intent);
        } catch (Exception e) {
            Toast.makeText(getContext(),
                    "Reconnaissance vocale non disponible", Toast.LENGTH_SHORT).show();
        }
    }

    private List<PhotoModel> getAllPhotos() {
        List<PhotoModel> list = new ArrayList<>();
        list.add(new PhotoModel("Cascade en foret", "Alice B.", "Reunion, France",
                "Fev 2024", android.R.drawable.ic_menu_gallery, 54));
        list.add(new PhotoModel("Rue coloree", "Carlos M.", "Lisbonne, Portugal",
                "Mai 2024", android.R.drawable.ic_menu_gallery, 87));
        list.add(new PhotoModel("Musee du Louvre", "Emma D.", "Paris, France",
                "Juin 2024", android.R.drawable.ic_menu_gallery, 310));
        list.add(new PhotoModel("Sushi bar Shibuya", "Yuki T.", "Tokyo, Japon",
                "Dec 2023", android.R.drawable.ic_menu_gallery, 63));
        list.add(new PhotoModel("Montagne enneigee", "Pierre V.", "Chamonix, France",
                "Jan 2024", android.R.drawable.ic_menu_gallery, 129));
        list.add(new PhotoModel("Marche bio nature", "Laura C.", "Berlin, Allemagne",
                "Avril 2024", android.R.drawable.ic_menu_gallery, 45));
        list.add(new PhotoModel("Plage paradisiaque", "Sophie M.", "Maldives",
                "Mars 2024", android.R.drawable.ic_menu_gallery, 210));
        list.add(new PhotoModel("Temple bouddhiste", "Kenji A.", "Kyoto, Japon",
                "Nov 2023", android.R.drawable.ic_menu_gallery, 178));
        return list;
    }
}