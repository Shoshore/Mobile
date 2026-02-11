package com.example.ex5;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText etNom, etPrenom, etAge, etDomaine, etTelephone;
    Button btnValider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // vu
        etNom = findViewById(R.id.etNom);
        etPrenom = findViewById(R.id.etPrenom);
        etAge = findViewById(R.id.etAge);
        etDomaine = findViewById(R.id.etDomaine);
        etTelephone = findViewById(R.id.etTelephone);
        btnValider = findViewById(R.id.btnValider);

        btnValider.setOnClickListener(v -> afficherDialogueConfirmation());
    }

    private void afficherDialogueConfirmation() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_title);
        builder.setMessage(R.string.dialog_message);

        // CONFIRMER
        builder.setPositiveButton(R.string.btn_confirmer, (dialog, which) -> {
            int couleur = Color.parseColor("#C8E6C9");
            etNom.setBackgroundColor(couleur);
            etPrenom.setBackgroundColor(couleur);
            etAge.setBackgroundColor(couleur);
            etDomaine.setBackgroundColor(couleur);
            etTelephone.setBackgroundColor(couleur);
        });


        // ANNULER
        builder.setNegativeButton(R.string.btn_annuler,
                (dialog, which) -> dialog.dismiss());

        builder.show();
    }
}
