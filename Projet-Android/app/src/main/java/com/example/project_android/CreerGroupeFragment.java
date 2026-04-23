package com.example.project_android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CreerGroupeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_creer_groupe, container, false);

        EditText etNom         = view.findViewById(R.id.et_groupe_nom);
        EditText etDescription = view.findViewById(R.id.et_groupe_description);
        CheckBox cbPublic      = view.findViewById(R.id.cb_groupe_public);
        Button btnCreer        = view.findViewById(R.id.btn_creer_submit);
        Button btnRetour       = view.findViewById(R.id.btn_creer_retour);

        MainActivity activity = (MainActivity) requireActivity();

        btnCreer.setOnClickListener(v -> {
            String nom  = etNom.getText().toString().trim();
            String desc = etDescription.getText().toString().trim();

            if (nom.isEmpty()) {
                etNom.setError("Nom requis");
                return;
            }
            if (desc.isEmpty()) {
                etDescription.setError("Description requise");
                return;
            }

            String createur = activity.getLoggedUserName();
            boolean estPublic = cbPublic.isChecked();

            GroupeModel nouveauGroupe = GroupeRepository.getInstance()
                    .creerGroupe(nom, desc, createur, estPublic);

            Toast.makeText(getContext(),
                    "Groupe \"" + nom + "\" cree !", Toast.LENGTH_SHORT).show();

            // Aller directement dans le groupe cree
            GroupeDetailFragment detail =
                    GroupeDetailFragment.newInstance(nouveauGroupe.getId());
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, detail)
                    .addToBackStack(null)
                    .commit();
        });

        btnRetour.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );

        return view;
    }
}