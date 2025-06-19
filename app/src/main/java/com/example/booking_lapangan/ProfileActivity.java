package com.example.booking_lapangan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    TextView tvNama, tvEmail, tvNoHp;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        tvNama = findViewById(R.id.tvNama);
        tvEmail = findViewById(R.id.tvEmail);
        tvNoHp = findViewById(R.id.tvNoHp);
        btnBack = findViewById(R.id.btnBack);

        // Example: Get user data from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String nama = sharedPreferences.getString("nama", "N/A");
        String email = sharedPreferences.getString("email", "N/A");
        String no_hp = sharedPreferences.getString("no_hp", "N/A");

        // Set to TextViews
        tvNama.setText("Nama: " + nama);
        tvEmail.setText("Email: " + email);
        tvNoHp.setText("No HP: " + no_hp);

        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, MenuActivity.class)); // Or MainActivity
            finish();
        });
    }
}
