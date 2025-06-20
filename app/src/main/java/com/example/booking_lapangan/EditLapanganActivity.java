package com.example.booking_lapangan;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class EditLapanganActivity extends AppCompatActivity {

    EditText editId, editNama, editDeskripsi, editLokasi, editHarga, editImage;
    Spinner spinnerStatus;
    Button btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_lapangan);

        // Inisialisasi
        editId = findViewById(R.id.editId);
        editNama = findViewById(R.id.editNama);
        editDeskripsi = findViewById(R.id.editDeskripsi);
        editLokasi = findViewById(R.id.editLokasi);
        editHarga = findViewById(R.id.editHarga);
        editImage = findViewById(R.id.editImage);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        btnUpdate = findViewById(R.id.btnUpdate);

        // Atur isi spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.status_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapter);

        // Ambil data dari intent
        String lapanganData = getIntent().getStringExtra("lapanganData");
        // Format: ID:1,Nama:Lap A,Deskripsi:Ukuran full,Lokasi:Jl. Sudirman,Harga:200000,Image:a.jpg,Status:tersedia
        if (lapanganData != null) {
            String[] data = lapanganData.split(",");
            editId.setText(data[0].replace("ID:", "").trim());
            editNama.setText(data[1].replace("Nama:", "").trim());
            editDeskripsi.setText(data[2].replace("Deskripsi:", "").trim());
            editLokasi.setText(data[3].replace("Lokasi:", "").trim());
            editHarga.setText(data[4].replace("Harga:", "").trim());
            editImage.setText(data[5].replace("Image:", "").trim());

            // Set spinner status
            String status = data[6].replace("Status:", "").trim();
            int spinnerIndex = status.equalsIgnoreCase("tersedia") ? 0 : 1;
            spinnerStatus.setSelection(spinnerIndex);
        }

        // Tombol update ditekan
        btnUpdate.setOnClickListener(v -> {
            updateLapangan(
                    editId.getText().toString(),
                    editNama.getText().toString(),
                    editDeskripsi.getText().toString(),
                    editLokasi.getText().toString(),
                    editHarga.getText().toString(),
                    editImage.getText().toString(),
                    spinnerStatus.getSelectedItem().toString()
            );
        });
    }

    private void updateLapangan(String id, String nama, String deskripsi, String lokasi,
                                String harga, String image, String status) {

        new Thread(() -> {
            try {
                URL url = new URL("http://192.168.0.95/lapangan/update_lapangan.php"); // Ganti dengan IP kamu
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                String postData = "id_lapangan=" + id +
                        "&nama=" + nama +
                        "&deskripsi=" + deskripsi +
                        "&lokasi=" + lokasi +
                        "&harga=" + harga +
                        "&image=" + image +
                        "&status=" + status;

                OutputStream os = conn.getOutputStream();
                os.write(postData.getBytes());
                os.flush();
                os.close();

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    runOnUiThread(() -> {
                        Toast.makeText(EditLapanganActivity.this, "Data berhasil diupdate!", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(EditLapanganActivity.this, "Gagal update data!", Toast.LENGTH_SHORT).show();
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() ->
                        Toast.makeText(EditLapanganActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
            }
        }).start();
    }
}
