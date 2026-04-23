package com.example.project_android;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PhotoDetailFragment extends Fragment {

    private static final String ARG_TITLE    = "title";
    private static final String ARG_AUTHOR   = "author";
    private static final String ARG_LOCATION = "location";
    private static final String ARG_DATE     = "date";
    private static final String ARG_LIKES    = "likes";
    private static final String ARG_IMAGE    = "image";

    private List<CommentModel> commentList = new ArrayList<>();
    private CommentAdapter commentAdapter;

    public static PhotoDetailFragment newInstance(PhotoModel photo) {
        PhotoDetailFragment f = new PhotoDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE,    photo.getTitle());
        args.putString(ARG_AUTHOR,   photo.getAuthor());
        args.putString(ARG_LOCATION, photo.getLocation());
        args.putString(ARG_DATE,     photo.getDate());
        args.putInt(ARG_LIKES,       photo.getLikes());
        args.putInt(ARG_IMAGE,       photo.getImageResId());
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_detail, container, false);

        Bundle args     = getArguments();
        String title    = args != null ? args.getString(ARG_TITLE, "")    : "";
        String author   = args != null ? args.getString(ARG_AUTHOR, "")   : "";
        String location = args != null ? args.getString(ARG_LOCATION, "") : "";
        String date     = args != null ? args.getString(ARG_DATE, "")     : "";
        int    likes    = args != null ? args.getInt(ARG_LIKES, 0)        : 0;
        int    imageRes = args != null ? args.getInt(ARG_IMAGE,
                android.R.drawable.ic_menu_gallery)
                : android.R.drawable.ic_menu_gallery;

        // Vues principales
        ImageView ivPhoto      = view.findViewById(R.id.iv_detail_photo);
        TextView tvTitle       = view.findViewById(R.id.tv_detail_title);
        TextView tvAuthor      = view.findViewById(R.id.tv_detail_author);
        TextView tvLocation    = view.findViewById(R.id.tv_detail_location);
        TextView tvDate        = view.findViewById(R.id.tv_detail_date);
        TextView tvLikes       = view.findViewById(R.id.tv_detail_likes);
        Button   btnItineraire = view.findViewById(R.id.btn_itineraire);
        Button   btnRetour     = view.findViewById(R.id.btn_detail_retour);
        Button   btnParcours   = view.findViewById(R.id.btn_detail_vers_parcours);
        Button btnSimilaires = view.findViewById(R.id.btn_similar_photos);
        // Vues commentaires
        RecyclerView recyclerComments  = view.findViewById(R.id.recycler_comments);
        EditText etNewComment          = view.findViewById(R.id.et_new_comment);
        Button   btnSendComment        = view.findViewById(R.id.btn_send_comment);
        View     layoutCommentInput    = view.findViewById(R.id.layout_comment_input);

        ivPhoto.setImageResource(imageRes);
        tvTitle.setText(title);
        tvAuthor.setText("📷 " + author);
        tvLocation.setText("📍 " + location);
        tvDate.setText("🗓 " + date);
        tvLikes.setText("❤️ " + likes + " likes");

        // Commentaires de demo
        commentList.add(new CommentModel("Alice B.",
                "Superbe endroit, j'y suis allee l'an dernier !", "Il y a 2j"));
        commentList.add(new CommentModel("Carlos M.",
                "La lumiere est magnifique sur cette photo.", "Il y a 5j"));
        commentList.add(new CommentModel("Emma D.",
                "Je reve d'y aller un jour...", "Il y a 1 sem"));

        commentAdapter = new CommentAdapter(commentList);
        recyclerComments.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerComments.setAdapter(commentAdapter);

        // Afficher champ commentaire seulement si connecte
        MainActivity activity = (MainActivity) requireActivity();
        if (activity.isLoggedIn()) {
            layoutCommentInput.setVisibility(View.VISIBLE);
        } else {
            layoutCommentInput.setVisibility(View.GONE);
        }

        // Envoyer un commentaire
        btnSendComment.setOnClickListener(v -> {
            String text = etNewComment.getText().toString().trim();
            if (text.isEmpty()) {
                etNewComment.setError("Ecrivez un commentaire");
                return;
            }
            String now = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
                    .format(new Date());
            String userName = activity.getLoggedUserName();
            commentList.add(0, new CommentModel(userName, text, now));
            commentAdapter.notifyItemInserted(0);
            recyclerComments.scrollToPosition(0);
            etNewComment.setText("");
            Toast.makeText(getContext(), "Commentaire publie !", Toast.LENGTH_SHORT).show();
        });

        // Itineraire Google Maps
        btnItineraire.setOnClickListener(v -> {
            String query = Uri.encode(location);
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

        // Retour
        btnRetour.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );

        // Passerelle TravelPath
        btnParcours.setOnClickListener(v -> {
            String ville = location.contains(",")
                    ? location.split(",")[0].trim() : location;
            TravelPathFragment f = new TravelPathFragment();
            Bundle bundle = new Bundle();
            bundle.putString("ville_prefillee", ville);
            f.setArguments(bundle);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, f)
                    .addToBackStack(null)
                    .commit();
        });

        btnSimilaires.setOnClickListener(v -> {
            PhotoModel ref = new PhotoModel(
                    title, author, location, date, imageRes, likes);
            SimilarPhotosFragment similar =
                    SimilarPhotosFragment.newInstance(ref);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, similar)
                    .addToBackStack(null)
                    .commit();
        });
        return view;
    }
}