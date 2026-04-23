package com.example.project_android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class LieuxFragment extends Fragment {

    private static final String ARG_VILLE = "ville";

    public static LieuxFragment newInstance(String ville) {
        LieuxFragment f = new LieuxFragment();
        Bundle args = new Bundle();
        args.putString(ARG_VILLE, ville);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lieux, container, false);

        Bundle args = getArguments();
        String ville = args != null ? args.getString(ARG_VILLE, "Paris") : "Paris";

        TextView tvTitre = view.findViewById(R.id.tv_lieux_titre);
        tvTitre.setText("📍 Lieux a " + ville);

        List<LieuModel> lieux = LieuRepository.getInstance().getlieuxForVille(ville);

        RecyclerView recycler = view.findViewById(R.id.recycler_lieux);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setAdapter(new LieuxAdapter(lieux, (lieuIndex) -> {
            LieuDetailFragment detail =
                    LieuDetailFragment.newInstance(ville, lieuIndex);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, detail)
                    .addToBackStack(null)
                    .commit();
        }));

        view.findViewById(R.id.btn_lieux_retour).setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );

        return view;
    }
}