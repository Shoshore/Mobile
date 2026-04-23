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

public class RegisterFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        EditText etName      = view.findViewById(R.id.et_register_name);
        EditText etEmail     = view.findViewById(R.id.et_register_email);
        EditText etPassword  = view.findViewById(R.id.et_register_password);
        EditText etConfirm   = view.findViewById(R.id.et_register_confirm);
        Button   btnRegister = view.findViewById(R.id.btn_register_submit);
        TextView tvGoLogin   = view.findViewById(R.id.tv_go_login);
        Button   btnRetour   = view.findViewById(R.id.btn_register_retour);

        btnRegister.setOnClickListener(v -> {
            String name    = etName.getText().toString().trim();
            String email   = etEmail.getText().toString().trim();
            String pass    = etPassword.getText().toString().trim();
            String confirm = etConfirm.getText().toString().trim();

            if (name.isEmpty())    { etName.setError("Nom requis"); return; }
            if (email.isEmpty())   { etEmail.setError("Email requis"); return; }
            if (pass.isEmpty())    { etPassword.setError("Mot de passe requis"); return; }
            if (!pass.equals(confirm)) {
                etConfirm.setError("Les mots de passe ne correspondent pas");
                return;
            }
            if (pass.length() < 4) {
                etPassword.setError("Minimum 4 caracteres");
                return;
            }

            boolean ok = UserRepository.getInstance().register(email, pass, name);
            if (ok) {
                Toast.makeText(getContext(), "Compte cree ! Connectez-vous.", Toast.LENGTH_SHORT).show();
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new LoginFragment())
                        .commit();
            } else {
                etEmail.setError("Cet email est deja utilise");
            }
        });

        tvGoLogin.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new LoginFragment())
                        .addToBackStack(null)
                        .commit()
        );

        btnRetour.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );

        return view;
    }
}