package com.example.uas_10120090_akb;
//NIM   : 10120090
//NAMA  : Muhammad Rizky Muhyi
//Kelas : IF-3


public class Note {
    String key, Judul, Kategori, Tanggal, isiCatatan;

    public Note() {

    }

    public String getJudul() {
        return Judul;
    }

    public String getKategori() {
        return Kategori;
    }

    public String getTanggal() {
        return Tanggal;
    }

    public String getIsiCatatan() {
        return isiCatatan;
    }

    public String getKey() {
        return key;
    }

    public void setJudul(String Judul) {
        this.Judul = Judul;
    }

    public void setKategori(String Kategori) {
        this.Kategori = Kategori;
    }

    public void setTanggal(String Tanggal) {
        this.Tanggal = Tanggal;
    }

    public void setIsiCatatan(String isiCatatan) {
        this.isiCatatan = isiCatatan;
    }

    public void setKey(String key) {
        this.key = key;
    }
}