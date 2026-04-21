package com.example.project_android;

import android.content.Intent;
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

    private boolean isLoggedIn = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView tvUsername = view.findViewById(R.id.tv_username);
        Button btnLoginLogout = view.findViewById(R.id.btn_login_logout);
        View sectionConnected = view.findViewById(R.id.section_connected);

        updateUI(tvUsername, btnLoginLogout, sectionConnected);

        btnLoginLogout.setOnClickListener(v -> {
            isLoggedIn = !isLoggedIn;
            updateUI(tvUsername, btnLoginLogout, sectionConnected);
            String msg = isLoggedIn ? "Connecté en tant que Jean Dupont" : "Déconnecté";
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        });

        view.findViewById(R.id.btn_my_photos).setOnClickListener(v ->
                Toast.makeText(getContext(), "Mes photos — à implémenter", Toast.LENGTH_SHORT).show()
        );
        view.findViewById(R.id.btn_publish).setOnClickListener(v ->
                Toast.makeText(getContext(), "Publier une photo — à implémenter", Toast.LENGTH_SHORT).show()
        );

        return view;
    }

    private void updateUI(TextView tvUsername, Button btnLoginLogout, View sectionConnected) {
        if (isLoggedIn) {
            tvUsername.setText("Jean Dupont");
            btnLoginLogout.setText("Se déconnecter");
            sectionConnected.setVisibility(View.VISIBLE);
        } else {
            tvUsername.setText("Mode anonyme");
            btnLoginLogout.setText("Se connecter");
            sectionConnected.setVisibility(View.GONE);
        }
    }
}