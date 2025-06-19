package com.example.booking_lapangan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    private Button btnLogout, btnLapangan, btnBooking, btnData, btnUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        btnLapangan = findViewById(R.id.btnLapangan);
        btnBooking = findViewById(R.id.btnBooking);
        btnData = findViewById(R.id.btnData);
        btnUser = findViewById(R.id.btnUser);
        btnLogout = findViewById(R.id.btnLogout);

        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String level = prefs.getString("level", "0");

        if (level.equals("1")) {
            btnData.setVisibility(View.VISIBLE);
            btnBooking.setVisibility(View.VISIBLE);
            btnLapangan.setVisibility(View.VISIBLE);
            btnUser.setVisibility(View.VISIBLE);
        } else {
            btnData.setVisibility(View.GONE);
            btnBooking.setVisibility(View.VISIBLE);
            btnLapangan.setVisibility(View.VISIBLE);
            btnUser.setVisibility(View.VISIBLE);
        }

        btnLogout.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();

            startActivity(new Intent(MenuActivity.this, LoginActivity.class));
            finish();
        });
        btnLapangan = findViewById(R.id.btnLapangan);

        btnLapangan.setOnClickListener(v -> {
            startActivity(new Intent(MenuActivity.this, LapanganActivity.class));
            finish();
        });
        btnBooking = findViewById(R.id.btnBooking);

        btnBooking.setOnClickListener(v -> {
            startActivity(new Intent(MenuActivity.this, BookingActivity.class));
            finish();
        });
        btnData = findViewById(R.id.btnData);

        btnData.setOnClickListener(v -> {
            startActivity(new Intent(MenuActivity.this, DataActivity.class));
            finish();
        });
        btnUser = findViewById(R.id.btnUser);

        btnUser.setOnClickListener(v -> {
            startActivity(new Intent(MenuActivity.this, ProfileActivity.class));
            finish();
        });

    }
}
