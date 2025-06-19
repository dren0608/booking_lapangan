package com.example.booking_lapangan;

public class Lapangan {
    private String nama, deskripsi, imageUrl, lokasi;
    private int harga;

    public Lapangan(String nama, String deskripsi, int harga, String imageUrl, String lokasi) {
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.harga = harga;
        this.imageUrl = imageUrl;
        this.lokasi = lokasi;
    }

    public String getNama() { return nama; }
    public String getDeskripsi() { return deskripsi; }
    public int getHarga() { return harga; }
    public String getImageUrl() { return imageUrl; }
    public String getLokasi() { return lokasi; }
}
