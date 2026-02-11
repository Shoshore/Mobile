package com.example.ex6;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        TextView tvRecap = findViewById(R.id.tvRecap);
        Button btnOk = findViewById(R.id.btnOk);
        Button btnRetour = findViewById(R.id.btnRetour);

        Intent intent = getIntent();

        String nom = intent.getStringExtra("nom");
        String prenom = intent.getStringExtra("prenom");
        String age = intent.getStringExtra("age");
        String domaine = intent.getStringExtra("domaine");
        String telephone = intent.getStringExtra("telephone");

        tvRecap.setText(getString(
                R.string.recap_text,
                nom,
                prenom,
                age,
                domaine,
                telephone
        ));


        // OK
        btnOk.setOnClickListener(v ->
                startActivity(new Intent(this, MainActivity3.class)));

        // Retour
        btnRetour.setOnClickListener(v -> finish());
    }
}