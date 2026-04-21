package com.example.project_android;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private PhotoAdapter adapter;
    private List<PhotoModel> allPhotos;
    private List<PhotoModel> filteredPhotos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        allPhotos = getAllPhotos();
        filteredPhotos = new ArrayList<>(allPhotos);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_photos);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new PhotoAdapter(filteredPhotos);
        recyclerView.setAdapter(adapter);

        // Filtres rapides
        view.findViewById(R.id.filter_nature).setOnClickListener(v -> filterBy("nature"));
        view.findViewById(R.id.filter_ville).setOnClickListener(v -> filterBy("ville"));
        view.findViewById(R.id.filter_food).setOnClickListener(v -> filterBy("food"));
        view.findViewById(R.id.filter_culture).setOnClickListener(v -> filterBy("culture"));
        view.findViewById(R.id.filter_all).setOnClickListener(v -> resetFilter());

        // Recherche texte
        EditText searchBar = view.findViewById(R.id.et_search);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchPhotos(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        return view;
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

    private List<PhotoModel> getAllPhotos() {
        List<PhotoModel> list = new ArrayList<>();
        list.add(new PhotoModel("Cascade en forêt", "Alice B.", "Réunion, France",
                "Fév 2024", android.R.drawable.ic_menu_gallery, 54));
        list.add(new PhotoModel("Rue colorée", "Carlos M.", "Lisbonne, Portugal",
                "Mai 2024", android.R.drawable.ic_menu_gallery, 87));
        list.add(new PhotoModel("Musée du Louvre", "Emma D.", "Paris, France",
                "Juin 2024", android.R.drawable.ic_menu_gallery, 310));
        list.add(new PhotoModel("Sushi bar Shibuya", "Yuki T.", "Tokyo, Japon",
                "Déc 2023", android.R.drawable.ic_menu_gallery, 63));
        list.add(new PhotoModel("Montagne enneigée", "Pierre V.", "Chamonix, France",
                "Jan 2024", android.R.drawable.ic_menu_gallery, 129));
        list.add(new PhotoModel("Marché bio nature", "Laura C.", "Berlin, Allemagne",
                "Avril 2024", android.R.drawable.ic_menu_gallery, 45));
        return list;
    }
}