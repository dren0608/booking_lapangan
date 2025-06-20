package com.example.booking_lapangan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class LoginActivity extends AppCompatActivity {

    private EditText etNama, etPassword;
    private Button btnLogin, btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        etNama = findViewById(R.id.etNama);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(v -> loginUser());

        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void loginUser() {
        String nama = etNama.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (nama.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Nama dan Password harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Encode the values for safety
            String encodedNama = URLEncoder.encode(nama, "UTF-8");
            String encodedPassword = URLEncoder.encode(password, "UTF-8");

            String urlString = "http://192.168.0.95/lapangan/login.php?nama=" + encodedNama + "&password=" + encodedPassword;

            new Thread(() -> {
                try {
                    URL url = new URL(urlString);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");

                    int responseCode = conn.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;

                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();

                        Log.d("RawResponse", response.toString());

                        JSONObject jsonObject = new JSONObject(response.toString());
                        Log.d("LoginResponse", jsonObject.toString());

                        boolean success = jsonObject.getBoolean("success");

                        runOnUiThread(() -> {
                            if (success) {
                                String level = jsonObject.optString("level", "0");
                                String email = jsonObject.optString("email", "not_set");
                                String no_hp = jsonObject.optString("no_hp", "not_set");
                                String namaFromResponse = jsonObject.optString("nama", nama); // fallback to input

                                SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("nama", namaFromResponse);
                                editor.putString("email", email);
                                editor.putString("no_hp", no_hp);
                                editor.putString("level", level);
                                editor.putBoolean("isLoggedIn", true);
                                editor.apply();

                                Toast.makeText(LoginActivity.this,
                                        "Login Berhasil",
                                        Toast.LENGTH_LONG).show();

                                startActivity(new Intent(LoginActivity.this, MenuActivity.class));
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "Login gagal. Coba lagi.", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        runOnUiThread(() ->
                                Toast.makeText(LoginActivity.this, "Server error: " + responseCode, Toast.LENGTH_SHORT).show());
                    }

                    conn.disconnect();

                } catch (Exception e) {
                    runOnUiThread(() ->
                            Toast.makeText(LoginActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                }
            }).start();

        } catch (Exception e) {
            Toast.makeText(this, "Encoding error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            startActivity(new Intent(LoginActivity.this, MenuActivity.class));
            finish();
        }
    }
}
