package com.example.booking_lapangan;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.view.View;
import android.widget.Button;


public class LapanganActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<Lapangan> lapanganList = new ArrayList<>();
    LapanganAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lapangan);
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LapanganActivity.this, MenuActivity.class);
                startActivity(intent);
                finish(); // optional: close current activity
            }
        });

        recyclerView = findViewById(R.id.recyclerLapangan);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new LapanganAdapter(this, lapanganList);
        recyclerView.setAdapter(adapter);

        loadLapanganData();
    }

    private void loadLapanganData() {
        new Thread(() -> {
            try {
                URL url = new URL("http://192.168.0.95/lapangan/get_lapangan.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                JSONObject json = new JSONObject(result.toString());
                JSONArray lapanganArray = json.getJSONArray("lapangan");

                for (int i = 0; i < lapanganArray.length(); i++) {
                    JSONObject obj = lapanganArray.getJSONObject(i);
                    Log.d("DATA", obj.toString()); // <- Add this line for debugging
                Lapangan l = new Lapangan(
                            obj.getString("nama"),
                            obj.getString("deskripsi"),
                        Integer.parseInt(obj.getString("harga")),
                        obj.getString("image_url"),
                        obj.getString("lokasi")
                    );
                    lapanganList.add(l);
                }

                runOnUiThread(() -> {
                    adapter.notifyDataSetChanged();
                    Toast.makeText(this, "Data Loaded: " + lapanganList.size(), Toast.LENGTH_SHORT).show();
                });


            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
