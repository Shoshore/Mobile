package com.example.project_android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_home_photos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<PhotoModel> photos = getSamplePhotos();
        PhotoAdapter adapter = new PhotoAdapter(photos);

        // Passerelle photo → TravelPath
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

        // Clic photo → fiche detail
        adapter.setOnPhotoClickListener(photo -> {
            PhotoDetailFragment detail = PhotoDetailFragment.newInstance(photo);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, detail)
                    .addToBackStack(null)
                    .commit();
        });

        recyclerView.setAdapter(adapter);
        return view;
    }

    private List<PhotoModel> getSamplePhotos() {
        List<PhotoModel> list = new ArrayList<>();
        list.add(new PhotoModel("Tour Eiffel au coucher", "Marie L.", "Paris, France",
                "Avril 2024", android.R.drawable.ic_menu_gallery, 142));
        list.add(new PhotoModel("Plage de Bondi", "Tom R.", "Sydney, Australie",
                "Janvier 2024", android.R.drawable.ic_menu_gallery, 98));
        list.add(new PhotoModel("Marche de Marrakech", "Sophie M.", "Marrakech, Maroc",
                "Mars 2024", android.R.drawable.ic_menu_gallery, 76));
        list.add(new PhotoModel("Foret de Kyoto", "Kenji A.", "Kyoto, Japon",
                "Novembre 2023", android.R.drawable.ic_menu_gallery, 215));
        return list;
    }
}