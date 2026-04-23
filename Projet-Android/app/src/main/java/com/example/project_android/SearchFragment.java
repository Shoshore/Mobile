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

        allPhotos = getAllPhotos();
        filteredPhotos = new ArrayList<>(allPhotos);

        tvActiveFilters = view.findViewById(R.id.tv_active_filters);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_photos);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new PhotoAdapter(filteredPhotos);
        recyclerView.setAdapter(adapter);

        adapter.setOnPhotoClickListener(photo -> {
            PhotoDetailFragment detail = PhotoDetailFragment.newInstance(photo);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, detail)
                    .addToBackStack(null)
                    .commit();
        });

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

        searchBar = view.findViewById(R.id.et_search);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchPhotos(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

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

        return view;
    }

    // ✅ AJOUT : filtre avancé avec similarité
    private void applyAdvancedFilters(FilterCriteria criteria) {

        // 🔥 PRIORITÉ : filtre par similarité
        if (criteria.photoReferenceTitle != null &&
                !criteria.photoReferenceTitle.isEmpty()) {
            filterBySimilarity(criteria.photoReferenceTitle);
            return;
        }

        filteredPhotos.clear();

        for (PhotoModel p : allPhotos) {
            boolean match = true;

            if (!criteria.auteur.isEmpty() &&
                    !p.getAuthor().toLowerCase().contains(criteria.auteur.toLowerCase())) {
                match = false;
            }

            if (!criteria.lieu.isEmpty() &&
                    !p.getLocation().toLowerCase().contains(criteria.lieu.toLowerCase())) {
                match = false;
            }

            if (match) filteredPhotos.add(p);
        }

        if (filteredPhotos.isEmpty()) {
            filteredPhotos.addAll(allPhotos);
        }

        adapter.notifyDataSetChanged();
    }

    // ✅ AJOUT : filtre par similarité
    private void filterBySimilarity(String referenceTitle) {

        PhotoModel reference = null;

        for (PhotoModel p : allPhotos) {
            if (p.getTitle().equalsIgnoreCase(referenceTitle)) {
                reference = p;
                break;
            }
        }

        if (reference == null) return;

        List<SimilarityEngine.ScoredPhoto> scored =
                SimilarityEngine.findSimilar(reference, allPhotos, 20);

        filteredPhotos.clear();

        for (SimilarityEngine.ScoredPhoto sp : scored) {
            filteredPhotos.add(sp.getPhoto());
        }

        adapter.notifyDataSetChanged();
    }

    private void searchPhotos(String query) {
        filteredPhotos.clear();

        if (query.isEmpty()) {
            filteredPhotos.addAll(allPhotos);
        } else {
            String q = query.toLowerCase();
            for (PhotoModel p : allPhotos) {
                if (p.getTitle().toLowerCase().contains(q)
                        || p.getAuthor().toLowerCase().contains(q)
                        || p.getLocation().toLowerCase().contains(q)) {
                    filteredPhotos.add(p);
                }
            }
        }

        adapter.notifyDataSetChanged();
    }

    private void updateFilterBadge(FilterCriteria criteria) {
        if (criteria.isEmpty()) {
            tvActiveFilters.setVisibility(View.GONE);
        } else {
            tvActiveFilters.setVisibility(View.VISIBLE);
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
        return list;
    }
}