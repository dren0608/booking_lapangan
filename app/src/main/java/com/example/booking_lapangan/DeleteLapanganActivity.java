package com.example.booking_lapangan;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DeleteLapanganActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_lapangan);

        String idLapangan = getIntent().getStringExtra("id_lapangan");

        Button btnDelete = findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData(idLapangan);
            }
        });
    }

    private void deleteData(final String idLapangan) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://192.168.1.208/lapangan_futsal/delete_lapangan.php"); // Ganti dengan IP kamu
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);

                    String params = "id_lapangan=" + idLapangan;

                    OutputStream os = conn.getOutputStream();
                    os.write(params.getBytes());
                    os.flush();
                    os.close();

                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        runOnUiThread(() -> {
                            Toast.makeText(DeleteLapanganActivity.this, "Data berhasil dihapus!", Toast.LENGTH_SHORT).show();
                            finish();
                        });
                    } else {
                        runOnUiThread(() -> {
                            Toast.makeText(DeleteLapanganActivity.this, "Gagal menghapus data!", Toast.LENGTH_SHORT).show();
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() ->
                            Toast.makeText(DeleteLapanganActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show()
                    );
                }
            }
        }).start();
    }
}
