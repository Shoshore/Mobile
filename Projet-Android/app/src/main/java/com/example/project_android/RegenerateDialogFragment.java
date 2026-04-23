package com.example.project_android;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class RegenerateDialogFragment extends DialogFragment {

    public interface OnRegenerateListener {
        void onRegenerate(AjustementModel ajustements);
    }

    private OnRegenerateListener listener;

    public void setOnRegenerateListener(OnRegenerateListener l) {
        this.listener = l;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.dialog_regenerate, null);

        SeekBar  seekBudget    = view.findViewById(R.id.seek_regen_budget);
        SeekBar  seekDuree     = view.findViewById(R.id.seek_regen_duree);
        TextView tvBudgetVal   = view.findViewById(R.id.tv_regen_budget_val);
        TextView tvDureeVal    = view.findViewById(R.id.tv_regen_duree_val);
        CheckBox cbPlusMusees  = view.findViewById(R.id.cb_plus_musees);
        CheckBox cbPlusResto   = view.findViewById(R.id.cb_plus_restos);
        CheckBox cbMoinsMarche = view.findViewById(R.id.cb_moins_marche);
        CheckBox cbEviterFoule = view.findViewById(R.id.cb_eviter_foule);

        // Budget 0-500€
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

        // Duree 1-12h
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

        return new AlertDialog.Builder(requireContext())
                .setTitle("🔄 Ajuster et regenerer")
                .setView(view)
                .setPositiveButton("Regenerer", (dialog, which) -> {
                    if (listener != null) {
                        AjustementModel aj = new AjustementModel();
                        aj.setBudget(seekBudget.getProgress() * 10);
                        aj.setDuree(seekDuree.getProgress() + 1);
                        aj.setPlusMusees(cbPlusMusees.isChecked());
                        aj.setPlusRestos(cbPlusResto.isChecked());
                        aj.setMoinsMarche(cbMoinsMarche.isChecked());
                        aj.setEviterFoule(cbEviterFoule.isChecked());
                        listener.onRegenerate(aj);
                    }
                })
                .setNegativeButton("Annuler", null)
                .create();
    }
}