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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GroupeDetailFragment extends Fragment {

    private static final String ARG_GROUPE_ID = "groupe_id";

    public static GroupeDetailFragment newInstance(String groupeId) {
        GroupeDetailFragment f = new GroupeDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_GROUPE_ID, groupeId);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_groupe_detail, container, false);

        Bundle args  = getArguments();
        String id    = args != null ? args.getString(ARG_GROUPE_ID, "") : "";
        GroupeModel groupe = GroupeRepository.getInstance().getGroupeById(id);

        if (groupe == null) {
            requireActivity().getSupportFragmentManager().popBackStack();
            return view;
        }

        MainActivity activity = (MainActivity) requireActivity();
        String currentUser = activity.isLoggedIn()
                ? activity.getLoggedUserName() : "Anonyme";

        TextView tvNom        = view.findViewById(R.id.tv_gd_nom);
        TextView tvDesc       = view.findViewById(R.id.tv_gd_description);
        TextView tvCreateur   = view.findViewById(R.id.tv_gd_createur);
        TextView tvStats      = view.findViewById(R.id.tv_gd_stats);
        TextView tvMembres    = view.findViewById(R.id.tv_gd_membres);
        Button btnPublier     = view.findViewById(R.id.btn_gd_publier);
        Button btnRetour      = view.findViewById(R.id.btn_gd_retour);
        RecyclerView recycler = view.findViewById(R.id.recycler_gd_photos);

        tvNom.setText(groupe.getNom());
        tvDesc.setText(groupe.getDescription());
        tvCreateur.setText("👤 Cree par " + groupe.getCreateur());
        tvStats.setText("👥 " + groupe.getNbMembres()
                + " membres  ·  📸 " + groupe.getNbPhotos() + " photos");

        // Liste membres
        StringBuilder membresStr = new StringBuilder("Membres : ");
        for (int i = 0; i < groupe.getMembres().size(); i++) {
            membresStr.append(groupe.getMembres().get(i));
            if (i < groupe.getMembres().size() - 1) membresStr.append(", ");
        }
        tvMembres.setText(membresStr.toString());

        // Photos du groupe
        recycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
        PhotoAdapter photoAdapter = new PhotoAdapter(groupe.getPhotos());
        photoAdapter.setOnPhotoClickListener(photo -> {
            PhotoDetailFragment detail = PhotoDetailFragment.newInstance(photo);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, detail)
                    .addToBackStack(null)
                    .commit();
        });
        recycler.setAdapter(photoAdapter);

        // Publier dans le groupe
        boolean estMembre = groupe.getMembres().contains(currentUser);
        btnPublier.setEnabled(estMembre && activity.isLoggedIn());
        btnPublier.setOnClickListener(v -> {
            if (!activity.isLoggedIn()) {
                Toast.makeText(getContext(),
                        "Connectez-vous pour publier", Toast.LENGTH_SHORT).show();
                return;
            }
            // Publier une photo de demo dans le groupe
            PhotoModel newPhoto = new PhotoModel(
                    "Photo de " + currentUser,
                    currentUser,
                    "Lieu inconnu",
                    "Aujourd'hui",
                    android.R.drawable.ic_menu_gallery,
                    0);
            GroupeRepository.getInstance()
                    .publierDansGroupe(groupe.getId(), newPhoto);
            photoAdapter.notifyDataSetChanged();
            tvStats.setText("👥 " + groupe.getNbMembres()
                    + " membres  ·  📸 " + groupe.getNbPhotos() + " photos");
            Toast.makeText(getContext(),
                    "Photo publiee dans " + groupe.getNom(),
                    Toast.LENGTH_SHORT).show();
        });

        btnRetour.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );

        return view;
    }
}