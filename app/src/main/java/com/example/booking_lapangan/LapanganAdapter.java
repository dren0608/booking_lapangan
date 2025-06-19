package com.example.booking_lapangan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LapanganAdapter extends RecyclerView.Adapter<LapanganAdapter.ViewHolder> {
    private Context context;
    private List<Lapangan> lapanganList;

    public LapanganAdapter(Context context, List<Lapangan> lapanganList) {
        this.context = context;
        this.lapanganList = lapanganList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lapangan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Lapangan l = lapanganList.get(position);
        holder.tvNama.setText(l.getNama());
        holder.tvDeskripsi.setText(l.getDeskripsi());
        holder.tvLokasi.setText(l.getLokasi());
        holder.tvHarga.setText("Rp. " + l.getHarga() + "/jam");

        Glide.with(context)
                .load(l.getImageUrl()) // example: http://192.168.1.12/lapangan/images/lapanganA.jpg
                .placeholder(R.drawable.placeholder)
                .into(holder.imgLapangan);

        holder.btnBooking.setOnClickListener(v -> {
            // handle booking here
            Toast.makeText(context, "Booking " + l.getNama(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return lapanganList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama, tvDeskripsi, tvHarga, tvLokasi;
        ImageView imgLapangan;
        Button btnBooking;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvDeskripsi = itemView.findViewById(R.id.tvDeskripsi);
            tvHarga = itemView.findViewById(R.id.tvHarga);
            tvLokasi = itemView.findViewById(R.id.tvLokasi);
            imgLapangan = itemView.findViewById(R.id.imgLapangan);
            btnBooking = itemView.findViewById(R.id.btnBooking);
        }
    }
}
