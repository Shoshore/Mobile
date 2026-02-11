package com.example.ex7;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        TextView tvPhone = findViewById(R.id.tvPhone);
        Button btncall = findViewById(R.id.btnCall);

        String telephone = getIntent().getStringExtra("telephone");
        tvPhone.setText(telephone);

        btncall.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + telephone));
            startActivity(intent);
        });
    }
}
