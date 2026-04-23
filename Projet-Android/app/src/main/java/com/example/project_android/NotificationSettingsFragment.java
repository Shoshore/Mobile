package com.example.project_android;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class NotificationSettingsFragment extends Fragment {

    private NotificationPrefsModel prefs = new NotificationPrefsModel();

    private final ActivityResultLauncher<String> notifPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
                if (granted) {
                    Toast.makeText(getContext(),
                            "Notifications activees !", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(),
                            "Permission refusee", Toast.LENGTH_SHORT).show();
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification_settings, container, false);

        Switch switchPhotos  = view.findViewById(R.id.switch_notif_photos);
        Switch switchGroupes = view.findViewById(R.id.switch_notif_groupes);
        Switch switchLieux   = view.findViewById(R.id.switch_notif_lieux);
        EditText etAuteur    = view.findViewById(R.id.et_notif_auteur);
        EditText etLieu      = view.findViewById(R.id.et_notif_lieu);
        EditText etGroupe    = view.findViewById(R.id.et_notif_groupe);
        Button btnSave       = view.findViewById(R.id.btn_save_notifs);
        Button btnTest       = view.findViewById(R.id.btn_test_notif);
        Button btnRetour     = view.findViewById(R.id.btn_notif_retour);

        // Init avec valeurs actuelles
        switchPhotos.setChecked(prefs.isNotifNouvellesPhotos());
        switchGroupes.setChecked(prefs.isNotifGroupes());
        switchLieux.setChecked(prefs.isNotifLieux());
        etAuteur.setText(prefs.getAuteurSuivi());
        etLieu.setText(prefs.getLieuSuivi());
        etGroupe.setText(prefs.getGroupeSuivi());

        switchPhotos.setOnCheckedChangeListener((btn, checked) ->
                prefs.setNotifNouvellesPhotos(checked));
        switchGroupes.setOnCheckedChangeListener((btn, checked) ->
                prefs.setNotifGroupes(checked));
        switchLieux.setOnCheckedChangeListener((btn, checked) ->
                prefs.setNotifLieux(checked));

        btnSave.setOnClickListener(v -> {
            prefs.setAuteurSuivi(etAuteur.getText().toString().trim());
            prefs.setLieuSuivi(etLieu.getText().toString().trim());
            prefs.setGroupeSuivi(etGroupe.getText().toString().trim());

            // Demander permission si Android 13+
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.POST_NOTIFICATIONS)
                        != PackageManager.PERMISSION_GRANTED) {
                    notifPermissionLauncher.launch(
                            Manifest.permission.POST_NOTIFICATIONS);
                    return;
                }
            }

            Toast.makeText(getContext(),
                    "Preferences sauvegardees !", Toast.LENGTH_SHORT).show();
        });

        // Bouton test : envoie une vraie notification
        btnTest.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.POST_NOTIFICATIONS)
                        != PackageManager.PERMISSION_GRANTED) {
                    notifPermissionLauncher.launch(
                            Manifest.permission.POST_NOTIFICATIONS);
                    return;
                }
            }

            // Envoyer des notifications de test selon les prefs
            if (prefs.isNotifNouvellesPhotos()) {
                String auteur = prefs.getAuteurSuivi().isEmpty()
                        ? "Marie L." : prefs.getAuteurSuivi();
                NotificationHelper.notifyNewPhoto(requireContext(),
                        auteur, "Paris, France");
            }
            if (prefs.isNotifGroupes()) {
                String groupe = prefs.getGroupeSuivi().isEmpty()
                        ? "Voyageurs du monde" : prefs.getGroupeSuivi();
                NotificationHelper.notifyGroupeActivite(requireContext(),
                        groupe, "Tom R.");
            }
            if (prefs.isNotifLieux()) {
                String lieu = prefs.getLieuSuivi().isEmpty()
                        ? "Tokyo" : prefs.getLieuSuivi();
                NotificationHelper.notifyLieuSuivi(requireContext(), lieu);
            }

            Toast.makeText(getContext(),
                    "Notifications de test envoyees !", Toast.LENGTH_SHORT).show();
        });

        btnRetour.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );

        return view;
    }
}