package com.example.ex9;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText etEvenement;
    private Button btnAjouter;
    private ListView listEvenements;

    private ArrayList<String> evenementsList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etEvenement = findViewById(R.id.etEvenement);
        btnAjouter = findViewById(R.id.btnAjouter);
        listEvenements = findViewById(R.id.listEvenements);

        evenementsList = new ArrayList<>();
        evenementsList.add("Réunion équipe 09:00");
        evenementsList.add("Déjeuner avec client 12:30");

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                evenementsList);
        listEvenements.setAdapter(adapter);

        btnAjouter.setOnClickListener(v -> {
            String newEvent = etEvenement.getText().toString().trim();
            if (!newEvent.isEmpty()) {
                evenementsList.add(newEvent);
                adapter.notifyDataSetChanged();
                etEvenement.setText("");
            }
        });
    }
}
