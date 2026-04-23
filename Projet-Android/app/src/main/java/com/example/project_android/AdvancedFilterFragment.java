package com.example.project_android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AdvancedFilterFragment extends Fragment {

    // Callback pour renvoyer les filtres au SearchFragment
    public interface OnFiltersAppliedListener {
        void onFiltersApplied(FilterCriteria criteria);
    }

    private OnFiltersAppliedListener listener;

    public void setOnFiltersAppliedListener(OnFiltersAppliedListener l) {
        this.listener = l;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_advanced_filter, container, false);

        EditText etAuteur       = view.findViewById(R.id.et_filter_auteur);
        EditText etPeriodeDebut = view.findViewById(R.id.et_filter_periode_debut);
        EditText etPeriodeFin   = view.findViewById(R.id.et_filter_periode_fin);
        EditText etLieu         = view.findViewById(R.id.et_filter_lieu);
        SeekBar  seekRayon      = view.findViewById(R.id.seek_rayon);
        TextView tvRayonVal     = view.findViewById(R.id.tv_rayon_val);
        RadioGroup rgType       = view.findViewById(R.id.rg_type_lieu);
        Button btnAppliquer     = view.findViewById(R.id.btn_appliquer_filtres);
        Button btnReset         = view.findViewById(R.id.btn_reset_filtres);
        Button btnRetour        = view.findViewById(R.id.btn_filter_retour);

        // Rayon 1–100 km
        seekRayon.setMax(99);
        seekRayon.setProgress(9);
        tvRayonVal.setText("10 km");
        seekRayon.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar s, int p, boolean u) {
                tvRayonVal.setText((p + 1) + " km");
            }
            @Override public void onStartTrackingTouch(SeekBar s) {}
            @Override public void onStopTrackingTouch(SeekBar s) {}
        });

        btnAppliquer.setOnClickListener(v -> {
            FilterCriteria criteria = new FilterCriteria();
            criteria.auteur       = etAuteur.getText().toString().trim();
            criteria.periodeDebut = etPeriodeDebut.getText().toString().trim();
            criteria.periodeFin   = etPeriodeFin.getText().toString().trim();
            criteria.lieu         = etLieu.getText().toString().trim();
            criteria.rayonKm      = seekRayon.getProgress() + 1;

            // Type de lieu selectionne
            int selectedId = rgType.getCheckedRadioButtonId();
            if (selectedId != -1) {
                RadioButton rb = view.findViewById(selectedId);
                criteria.typeLieu = rb.getText().toString();
            }

            if (listener != null) listener.onFiltersApplied(criteria);
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        btnReset.setOnClickListener(v -> {
            etAuteur.setText("");
            etPeriodeDebut.setText("");
            etPeriodeFin.setText("");
            etLieu.setText("");
            seekRayon.setProgress(9);
            rgType.clearCheck();
            if (listener != null) listener.onFiltersApplied(new FilterCriteria());
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        btnRetour.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );

        return view;
    }
}