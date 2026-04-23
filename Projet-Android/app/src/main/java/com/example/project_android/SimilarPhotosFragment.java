package com.example.project_android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class SimilarPhotosFragment extends Fragment {

    private static final String ARG_TITLE    = "title";
    private static final String ARG_AUTHOR   = "author";
    private static final String ARG_LOCATION = "location";
    private static final String ARG_DATE     = "date";
    private static final String ARG_IMAGE    = "image";
    private static final String ARG_LIKES    = "likes";

    public static SimilarPhotosFragment newInstance(PhotoModel photo) {
        SimilarPhotosFragment f = new SimilarPhotosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE,    photo.getTitle());
        args.putString(ARG_AUTHOR,   photo.getAuthor());
        args.putString(ARG_LOCATION, photo.getLocation());
        args.putString(ARG_DATE,     photo.getDate());
        args.putInt(ARG_IMAGE,       photo.getImageResId());
        args.putInt(ARG_LIKES,       photo.getLikes());
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_similar_photos, container, false);

        Bundle args     = getArguments();
        String title    = args != null ? args.getString(ARG_TITLE, "")    : "";
        String author   = args != null ? args.getString(ARG_AUTHOR, "")   : "";
        String location = args != null ? args.getString(ARG_LOCATION, "") : "";
        String date     = args != null ? args.getString(ARG_DATE, "")     : "";
        int    imageRes = args != null ? args.getInt(ARG_IMAGE,
                android.R.drawable.ic_menu_gallery)
                : android.R.drawable.ic_menu_gallery;
        int likes = args != null ? args.getInt(ARG_LIKES, 0) : 0;

        PhotoModel reference = new PhotoModel(
                title, author, location, date, imageRes, likes);

        // Afficher la photo de référence
        ImageView ivRef   = view.findViewById(R.id.iv_similar_reference);
        TextView tvRef    = view.findViewById(R.id.tv_similar_reference_title);
        TextView tvRefLoc = view.findViewById(R.id.tv_similar_reference_loc);
        ivRef.setImageResource(imageRes);
        tvRef.setText(title);
        tvRefLoc.setText("📍 " + location + "  ·  📷 " + author);

        // Calculer les photos similaires
        List<PhotoModel> allPhotos = getAllPhotos();
        List<SimilarityEngine.ScoredPhoto> similar =
                SimilarityEngine.findSimilar(reference, allPhotos, 10);

        TextView tvCount = view.findViewById(R.id.tv_similar_count);
        tvCount.setText(similar.size() + " photo(s) similaire(s) trouvee(s)");

        RecyclerView recycler = view.findViewById(R.id.recycler_similar);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setAdapter(new SimilarPhotoAdapter(similar, photo -> {
            PhotoDetailFragment detail = PhotoDetailFragment.newInstance(photo);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, detail)
                    .addToBackStack(null)
                    .commit();
        }));

        view.findViewById(R.id.btn_similar_retour).setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );

        return view;
    }

    private List<PhotoModel> getAllPhotos() {
        List<PhotoModel> list = new ArrayList<>();
        list.add(new PhotoModel("Tour Eiffel au coucher", "Marie L.",
                "Paris, France", "Avril 2024",
                android.R.drawable.ic_menu_gallery, 142));
        list.add(new PhotoModel("Louvre sous la pluie", "Emma D.",
                "Paris, France", "Juin 2024",
                android.R.drawable.ic_menu_gallery, 310));
        list.add(new PhotoModel("Montmartre le matin", "Sophie M.",
                "Paris, France", "Mars 2024",
                android.R.drawable.ic_menu_gallery, 87));
        list.add(new PhotoModel("Plage de Bondi", "Tom R.",
                "Sydney, Australie", "Janvier 2024",
                android.R.drawable.ic_menu_gallery, 98));
        list.add(new PhotoModel("Cascade en foret", "Alice B.",
                "Reunion, France", "Fev 2024",
                android.R.drawable.ic_menu_gallery, 54));
        list.add(new PhotoModel("Rue coloree", "Carlos M.",
                "Lisbonne, Portugal", "Mai 2024",
                android.R.drawable.ic_menu_gallery, 87));
        list.add(new PhotoModel("Temple bouddhiste", "Kenji A.",
                "Kyoto, Japon", "Nov 2023",
                android.R.drawable.ic_menu_gallery, 178));
        list.add(new PhotoModel("Foret de Kyoto", "Kenji A.",
                "Kyoto, Japon", "Nov 2023",
                android.R.drawable.ic_menu_gallery, 215));
        list.add(new PhotoModel("Montagne enneigee", "Pierre V.",
                "Chamonix, France", "Jan 2024",
                android.R.drawable.ic_menu_gallery, 129));
        list.add(new PhotoModel("Marche bio nature", "Laura C.",
                "Berlin, Allemagne", "Avril 2024",
                android.R.drawable.ic_menu_gallery, 45));
        list.add(new PhotoModel("Plage paradisiaque", "Sophie M.",
                "Maldives", "Mars 2024",
                android.R.drawable.ic_menu_gallery, 210));
        list.add(new PhotoModel("Marche de Marrakech", "Sophie M.",
                "Marrakech, Maroc", "Mars 2024",
                android.R.drawable.ic_menu_gallery, 76));
        return list;
    }
}