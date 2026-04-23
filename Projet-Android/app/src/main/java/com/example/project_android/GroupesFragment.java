package com.example.project_android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class GroupesFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_groupes, container, false);

        MainActivity activity = (MainActivity) requireActivity();
        String currentUser = activity.isLoggedIn()
                ? activity.getLoggedUserName() : "Anonyme";

        List<GroupeModel> groupes = GroupeRepository.getInstance().getAllGroupes();

        RecyclerView recycler = view.findViewById(R.id.recycler_groupes);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        GroupeAdapter adapter = new GroupeAdapter(groupes, currentUser,
                new GroupeAdapter.OnGroupeActionListener() {
                    @Override
                    public void onVoirGroupe(GroupeModel groupe) {
                        GroupeDetailFragment detail =
                                GroupeDetailFragment.newInstance(groupe.getId());
                        requireActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, detail)
                                .addToBackStack(null)
                                .commit();
                    }

                    @Override
                    public void onRejoindreGroupe(GroupeModel groupe) {
                        if (!activity.isLoggedIn()) {
                            Toast.makeText(getContext(),
                                    "Connectez-vous pour rejoindre un groupe",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        boolean ok = GroupeRepository.getInstance()
                                .rejoindreGroupe(groupe.getId(), currentUser);
                        if (ok) {
                            Toast.makeText(getContext(),
                                    "Vous avez rejoint " + groupe.getNom(),
                                    Toast.LENGTH_SHORT).show();
                            recycler.getAdapter().notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(),
                                    "Vous etes deja membre", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        recycler.setAdapter(adapter);

        // Bouton créer un groupe
        Button btnCreer = view.findViewById(R.id.btn_creer_groupe);
        btnCreer.setOnClickListener(v -> {
            if (!activity.isLoggedIn()) {
                Toast.makeText(getContext(),
                        "Connectez-vous pour creer un groupe",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new CreerGroupeFragment())
                    .addToBackStack(null)
                    .commit();
        });

        view.findViewById(R.id.btn_groupes_retour).setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );

        return view;
    }
}