package com.example.ex3_java;

import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // layout principal
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(32, 32, 32, 32);

        // nom
        TextView tvNom = new TextView(this);
        tvNom.setText(R.string.label_nom);

        EditText etNom = new EditText(this);
        etNom.setHint(R.string.hint_nom);

        // prénom
        TextView tvPrenom = new TextView(this);
        tvPrenom.setText(R.string.label_prenom);

        EditText etPrenom = new EditText(this);
        etPrenom.setHint(R.string.hint_prenom);

        // age
        TextView tvAge = new TextView(this);
        tvAge.setText(R.string.label_age);

        EditText etAge = new EditText(this);
        etAge.setHint(R.string.hint_age);
        etAge.setInputType(InputType.TYPE_CLASS_NUMBER);

        // domaine
        TextView tvDomaine = new TextView(this);
        tvDomaine.setText(R.string.label_domainecomp);

        EditText etDomaine = new EditText(this);
        etDomaine.setHint(R.string.hint_domainecomp);

        // tel
        TextView tvTelephone = new TextView(this);
        tvTelephone.setText(R.string.label_num);

        EditText etTelephone = new EditText(this);
        etTelephone.setHint(R.string.hint_num);
        etTelephone.setInputType(InputType.TYPE_CLASS_PHONE);

        // button
        Button btnValider = new Button(this);
        btnValider.setText(R.string.label_button);

        // layout
        layout.addView(tvNom);
        layout.addView(etNom);

        layout.addView(tvPrenom);
        layout.addView(etPrenom);

        layout.addView(tvAge);
        layout.addView(etAge);

        layout.addView(tvDomaine);
        layout.addView(etDomaine);

        layout.addView(tvTelephone);
        layout.addView(etTelephone);

        layout.addView(btnValider);

        // affichage
        setContentView(layout);
    }
}