package arindatiko.example.com.travelmecustomer.model;

import java.util.ArrayList;

public class Wisata {
    private int  id_wisata;
    private int id_user;
    private String nama;
    private String desa;
    private String kecamatan;
    private Double tiket_masuk_dewasa;
    private Double tiket_masuk_anak;
    private Double biaya_parkir_motor;
    private Double biaya_parkir_mobil;
    private Double biaya_parkir_bus;
    private String foto;
    private String deskripsi;
    private String fasilitas;
    private Double posisi_lat;
    private Double posisi_lng;
    private String akses;
    private String zona;
    private String jam_buka;
    private String jam_tutup;
    private String jenis;

    public Wisata() {
    }

    public Wisata(int id_wisata, int id_user, String nama, String desa, String kecamatan, Double tiket_masuk_dewasa, Double tiket_masuk_anak, Double biaya_parkir_motor, Double biaya_parkir_mobil, Double biaya_parkir_bus, String foto, String deskripsi, String fasilitas, Double posisi_lat, Double posisi_lng, String akses, String zona, String jam_buka, String jam_tutup, String jenis) {
        this.id_wisata = id_wisata;
        this.id_user = id_user;
        this.nama = nama;
        this.desa = desa;
        this.kecamatan = kecamatan;
        this.tiket_masuk_dewasa = tiket_masuk_dewasa;
        this.tiket_masuk_anak = tiket_masuk_anak;
        this.biaya_parkir_motor = biaya_parkir_motor;
        this.biaya_parkir_mobil = biaya_parkir_mobil;
        this.biaya_parkir_bus = biaya_parkir_bus;
        this.foto = foto;
        this.deskripsi = deskripsi;
        this.fasilitas = fasilitas;
        this.posisi_lat = posisi_lat;
        this.posisi_lng = posisi_lng;
        this.akses = akses;
        this.zona = zona;
        this.jam_buka = jam_buka;
        this.jam_tutup = jam_tutup;
        this.jenis = jenis;
    }

    public int getId_wisata() {
        return id_wisata;
    }

    public void setId_wisata(int id_wisata) {
        this.id_wisata = id_wisata;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getDesa() {
        return desa;
    }

    public void setDesa(String desa) {
        this.desa = desa;
    }

    public String getKecamatan() {
        return kecamatan;
    }

    public void setKecamatan(String kecamatan) {
        this.kecamatan = kecamatan;
    }

    public Double getTiket_masuk_dewasa() {
        return tiket_masuk_dewasa;
    }

    public void setTiket_masuk_dewasa(Double tiket_masuk_dewasa) {
        this.tiket_masuk_dewasa = tiket_masuk_dewasa;
    }

    public Double getTiket_masuk_anak() {
        return tiket_masuk_anak;
    }

    public void setTiket_masuk_anak(Double tiket_masuk_anak) {
        this.tiket_masuk_anak = tiket_masuk_anak;
    }

    public Double getBiaya_parkir_motor() {
        return biaya_parkir_motor;
    }

    public void setBiaya_parkir_motor(Double biaya_parkir_motor) {
        this.biaya_parkir_motor = biaya_parkir_motor;
    }

    public Double getBiaya_parkir_mobil() {
        return biaya_parkir_mobil;
    }

    public void setBiaya_parkir_mobil(Double biaya_parkir_mobil) {
        this.biaya_parkir_mobil = biaya_parkir_mobil;
    }

    public Double getBiaya_parkir_bus() {
        return biaya_parkir_bus;
    }

    public void setBiaya_parkir_bus(Double biaya_parkir_bus) {
        this.biaya_parkir_bus = biaya_parkir_bus;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getFasilitas() {
        return fasilitas;
    }

    public void setFasilitas(String fasilitas) {
        this.fasilitas = fasilitas;
    }

    public Double getPosisi_lat() {
        return posisi_lat;
    }

    public void setPosisi_lat(Double posisi_lat) {
        this.posisi_lat = posisi_lat;
    }

    public Double getPosisi_lng() {
        return posisi_lng;
    }

    public void setPosisi_lng(Double posisi_lng) {
        this.posisi_lng = posisi_lng;
    }

    public String getAkses() {
        return akses;
    }

    public void setAkses(String akses) {
        this.akses = akses;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String getJam_buka() {
        return jam_buka;
    }

    public void setJam_buka(String jam_buka) {
        this.jam_buka = jam_buka;
    }

    public String getJam_tutup() {
        return jam_tutup;
    }

    public void setJam_tutup(String jam_tutup) {
        this.jam_tutup = jam_tutup;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }
}