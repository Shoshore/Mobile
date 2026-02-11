package com.example.ex8;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText etDepart, etArrivee;
    Button btnRechercher;
    ListView listHoraires;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etDepart = findViewById(R.id.etDepart);
        etArrivee = findViewById(R.id.etArrivee);
        btnRechercher = findViewById(R.id.btnRechercher);
        listHoraires = findViewById(R.id.listHoraires);

        btnRechercher.setOnClickListener(v -> afficherHoraires());
    }

    private void afficherHoraires() {

        ArrayList<String> horaires = new ArrayList<>();

        horaires.add("🚆 Train Ouigo 8123 — 08:30 → 10:45");
        horaires.add("🚆 Train Ouigo 4567 — 14:15 → 16:30");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                horaires
        );

        listHoraires.setAdapter(adapter);
    }
}
