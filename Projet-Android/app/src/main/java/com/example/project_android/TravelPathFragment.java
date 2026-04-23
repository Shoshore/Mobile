package com.example.project_android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TravelPathFragment extends Fragment {

    private EditText etVille;
    private SeekBar seekBudget, seekDuree;
    private TextView tvBudgetVal, tvDureeVal;
    private CheckBox cbRestaurant, cbLoisirs, cbCulture, cbDecouverte;
    private CheckBox cbEffortFaible, cbSensibleFroid, cbSensibleChaleur;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_travelpath, container, false);

        etVille           = view.findViewById(R.id.et_ville);
        seekBudget        = view.findViewById(R.id.seek_budget);
        seekDuree         = view.findViewById(R.id.seek_duree);
        tvBudgetVal       = view.findViewById(R.id.tv_budget_val);
        tvDureeVal        = view.findViewById(R.id.tv_duree_val);
        cbRestaurant      = view.findViewById(R.id.cb_restaurant);
        cbLoisirs         = view.findViewById(R.id.cb_loisirs);
        cbCulture         = view.findViewById(R.id.cb_culture);
        cbDecouverte      = view.findViewById(R.id.cb_decouverte);
        cbEffortFaible    = view.findViewById(R.id.cb_effort_faible);
        cbSensibleFroid   = view.findViewById(R.id.cb_froid);
        cbSensibleChaleur = view.findViewById(R.id.cb_chaleur);
        Button btnGenerer = view.findViewById(R.id.btn_generer);
        Button btnMeteo = view.findViewById(R.id.btn_voir_meteo);
        btnMeteo.setOnClickListener(v -> {
            String ville = etVille.getText().toString().trim();
            WeatherFragment weatherFrag = new WeatherFragment();
            if (!ville.isEmpty()) {
                Bundle bundle = new Bundle();
                bundle.putString("ville", ville);
                weatherFrag.setArguments(bundle);
            }
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, weatherFrag)
                    .addToBackStack(null)
                    .commit();
        });

        // --- PASSERELLE : ville pré-remplie depuis TravelShare ---
        Bundle args = getArguments();
        if (args != null && args.containsKey("ville_prefillee")) {
            String villePre = args.getString("ville_prefillee");
            etVille.setText(villePre);
        }

        // Budget : 0–500€ par pas de 10
        seekBudget.setMax(50);
        seekBudget.setProgress(10);
        tvBudgetVal.setText("100 €");
        seekBudget.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar s, int p, boolean u) {
                tvBudgetVal.setText((p * 10) + " €");
            }
            @Override public void onStartTrackingTouch(SeekBar s) {}
            @Override public void onStopTrackingTouch(SeekBar s) {}
        });

        // Durée : 1–12h
        seekDuree.setMax(11);
        seekDuree.setProgress(4);
        tvDureeVal.setText("5 h");
        seekDuree.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar s, int p, boolean u) {
                tvDureeVal.setText((p + 1) + " h");
            }
            @Override public void onStartTrackingTouch(SeekBar s) {}
            @Override public void onStopTrackingTouch(SeekBar s) {}
        });

        btnGenerer.setOnClickListener(v -> {
            String ville = etVille.getText().toString().trim();
            if (ville.isEmpty()) {
                etVille.setError("Veuillez entrer une ville");
                return;
            }
            int budget = (seekBudget.getProgress() * 10);
            int duree  = seekDuree.getProgress() + 1;

            TravelPathResultFragment resultFragment = TravelPathResultFragment.newInstance(
                    ville, budget, duree,
                    cbRestaurant.isChecked(), cbCulture.isChecked(),
                    cbLoisirs.isChecked(), cbDecouverte.isChecked(),
                    cbEffortFaible.isChecked()
            );
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, resultFragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }
}