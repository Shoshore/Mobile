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

public class ProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        MainActivity activity   = (MainActivity) requireActivity();
        TextView tvUsername     = view.findViewById(R.id.tv_username);
        Button btnLoginLogout   = view.findViewById(R.id.btn_login_logout);
        View sectionConnected   = view.findViewById(R.id.section_connected);

        updateUI(activity, tvUsername, btnLoginLogout, sectionConnected);

        btnLoginLogout.setOnClickListener(v -> {
            if (activity.isLoggedIn()) {
                activity.setLoggedIn(false);
                activity.setLoggedUserName("");
                updateUI(activity, tvUsername, btnLoginLogout, sectionConnected);
                Toast.makeText(getContext(), "Deconnecte", Toast.LENGTH_SHORT).show();
            } else {
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new LoginFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        view.findViewById(R.id.btn_publish).setOnClickListener(v -> {
            if (activity.isLoggedIn()) {
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new PublishPhotoFragment())
                        .addToBackStack(null)
                        .commit();
            } else {
                Toast.makeText(getContext(),
                        "Connectez-vous pour publier", Toast.LENGTH_SHORT).show();
            }
        });

        view.findViewById(R.id.btn_my_photos).setOnClickListener(v ->
                Toast.makeText(getContext(),
                        "Mes photos - a implementer", Toast.LENGTH_SHORT).show()
        );

        view.findViewById(R.id.btn_notifications).setOnClickListener(v ->
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new NotificationSettingsFragment())
                        .addToBackStack(null)
                        .commit()
        );

        // Bouton groupes → GroupesFragment
        view.findViewById(R.id.btn_groupes).setOnClickListener(v ->
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new GroupesFragment())
                        .addToBackStack(null)
                        .commit()
        );

        return view;
    }

    private void updateUI(MainActivity activity, TextView tvUsername,
                          Button btnLoginLogout, View sectionConnected) {
        if (activity.isLoggedIn()) {
            tvUsername.setText(activity.getLoggedUserName());
            btnLoginLogout.setText("Se deconnecter");
            sectionConnected.setVisibility(View.VISIBLE);
        } else {
            tvUsername.setText("Mode anonyme");
            btnLoginLogout.setText("Se connecter");
            sectionConnected.setVisibility(View.GONE);
        }
    }
}