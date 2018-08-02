package arindatiko.example.com.travelmecustomer.model;

import java.util.ArrayList;

public class Rekomendasi {

    private int  id_tujuan, id_user, flag, id_pesanan;
    private String jenis_layanan,id_rekomendasi, waktu_pesan;

    /* private Wisata wisata;
     private Kuliner kuliner;
     private Kamar kamar;*/
    private ArrayList<Kamar> kamar;
    private ArrayList<Kuliner> kuliner;
    private ArrayList<Wisata> wisata;
    private User user;

    public Rekomendasi() {
    }

    public int getId_pesanan() {
        return id_pesanan;
    }

    public void setId_pesanan(int id_pesanan) {
        this.id_pesanan = id_pesanan;
    }

    public String getId_rekomendasi() {
        return id_rekomendasi;
    }

    public void setId_rekomendasi(String id_rekomendasi) {
        this.id_rekomendasi = id_rekomendasi;
    }

    public int getId_tujuan() {
        return id_tujuan;
    }

    public void setId_tujuan(int id_tujuan) {
        this.id_tujuan = id_tujuan;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getJenis_layanan() {
        return jenis_layanan;
    }

    public void setJenis_layanan(String jenis_layanan) {
        this.jenis_layanan = jenis_layanan;
    }

    public String getWaktu_pesan() {
        return waktu_pesan;
    }

    public void setWaktu_pesan(String waktu_pesan) {
        this.waktu_pesan = waktu_pesan;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /*public Wisata getWisata() {
        return wisata;
    }

    public void setWisata(Wisata wisata) {
        this.wisata = wisata;
    }

    public Kuliner getKuliner() {
        return kuliner;
    }

    public void setKuliner(Kuliner kuliner) {
        this.kuliner = kuliner;
    }

    public Kamar getKamar() {
        return kamar;
    }

    public void setKamar(Kamar kamar) {
        this.kamar = kamar;
    }*/

    public ArrayList<Kamar> getKamar() {
        return kamar;
    }

    public void setKamar(ArrayList<Kamar> kamar) {
        this.kamar = kamar;
    }

    public ArrayList<Kuliner> getKuliner() {
        return kuliner;
    }

    public void setKuliner(ArrayList<Kuliner> kuliner) {
        this.kuliner = kuliner;
    }

    public ArrayList<Wisata> getWisata() {
        return wisata;
    }

    public void setWisata(ArrayList<Wisata> wisata) {
        this.wisata = wisata;
    }
}
