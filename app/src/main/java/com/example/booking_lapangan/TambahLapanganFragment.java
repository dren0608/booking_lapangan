package com.example.booking_lapangan;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class TambahLapanganFragment extends Fragment {

    EditText etNama, etDeskripsi, etLokasi, etHarga, etImage;
    Spinner spinnerStatus;
    Button btnSimpan;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tambah_lapangan, container, false);

        // Inisialisasi
        etNama = view.findViewById(R.id.etNama);
        etDeskripsi = view.findViewById(R.id.etDeskripsi);
        etLokasi = view.findViewById(R.id.etLokasi);
        etHarga = view.findViewById(R.id.etHarga);
        etImage = view.findViewById(R.id.etImage);
        spinnerStatus = view.findViewById(R.id.spinnerStatus);
        btnSimpan = view.findViewById(R.id.btnSimpan);

        // Setup Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.status_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapter);

        // Aksi tombol simpan
        btnSimpan.setOnClickListener(v -> {
            String nama = etNama.getText().toString();
            String deskripsi = etDeskripsi.getText().toString();
            String lokasi = etLokasi.getText().toString();
            String harga = etHarga.getText().toString();
            String image = etImage.getText().toString();
            String status = spinnerStatus.getSelectedItem().toString();

            if (nama.isEmpty() || deskripsi.isEmpty() || lokasi.isEmpty() || harga.isEmpty()) {
                Toast.makeText(getContext(), "Semua field kecuali gambar harus diisi", Toast.LENGTH_SHORT).show();
            } else {
                new InsertLapanganTask().execute(nama, deskripsi, lokasi, harga, image, status);
            }
        });

        return view;
    }

    // AsyncTask untuk insert data ke server
    private class InsertLapanganTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String response = "";
            try {
                URL url = new URL("http://192.168.0.95/lapangan/insert_lapangan.php"); // Ubah sesuai IP kamu
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                String data = "nama=" + URLEncoder.encode(params[0], "UTF-8") +
                        "&deskripsi=" + URLEncoder.encode(params[1], "UTF-8") +
                        "&lokasi=" + URLEncoder.encode(params[2], "UTF-8") +
                        "&harga=" + URLEncoder.encode(params[3], "UTF-8") +
                        "&image=" + URLEncoder.encode(params[4], "UTF-8") +
                        "&status=" + URLEncoder.encode(params[5], "UTF-8");

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(data);
                writer.flush();
                writer.close();
                os.close();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                reader.close();
                response = sb.toString();

            } catch (Exception e) {
                e.printStackTrace();
                response = "Error: " + e.getMessage();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getContext(), "Respon: " + result, Toast.LENGTH_LONG).show();
            // Tambahkan navigasi jika ingin pindah fragment
        }
    }
}
