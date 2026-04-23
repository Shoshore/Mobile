package com.example.project_android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class LoginFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        EditText etEmail    = view.findViewById(R.id.et_login_email);
        EditText etPassword = view.findViewById(R.id.et_login_password);
        Button   btnLogin   = view.findViewById(R.id.btn_login_submit);
        TextView tvGoRegister = view.findViewById(R.id.tv_go_register);
        Button   btnRetour  = view.findViewById(R.id.btn_login_retour);

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String pass  = etPassword.getText().toString().trim();

            if (email.isEmpty()) { etEmail.setError("Email requis"); return; }
            if (pass.isEmpty())  { etPassword.setError("Mot de passe requis"); return; }

            if (UserRepository.getInstance().login(email, pass)) {
                String name = UserRepository.getInstance().getName(email);
                // Sauvegarder dans MainActivity
                MainActivity activity = (MainActivity) requireActivity();
                activity.setLoggedIn(true);
                activity.setLoggedUserName(name);

                Toast.makeText(getContext(), "Bienvenue " + name + " !", Toast.LENGTH_SHORT).show();

                // Retour au profil
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new ProfileFragment())
                        .commit();
            } else {
                Toast.makeText(getContext(), "Email ou mot de passe incorrect", Toast.LENGTH_SHORT).show();
            }
        });

        tvGoRegister.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new RegisterFragment())
                        .addToBackStack(null)
                        .commit()
        );

        btnRetour.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );

        return view;
    }
}