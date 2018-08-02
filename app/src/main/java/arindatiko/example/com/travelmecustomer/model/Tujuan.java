package arindatiko.example.com.travelmecustomer.model;

import java.util.ArrayList;
import java.util.List;

public class Tujuan {

    private String id_user;
    private String id_tujuan;
    private String jenis_layanan;
    private String id_rekomendasi;
    private int flag;

    private ArrayList<Wisata> wisata;
    private ArrayList<Kuliner> kuliner;
    private ArrayList<Penginapan> penginapan;
    private ArrayList<Kamar> kamar;
    private ArrayList<Menu> menu;

    public String getId_rekomendasi() {
        return id_rekomendasi;
    }

    public void setId_rekomendasi(String id_rekomendasi) {
        this.id_rekomendasi = id_rekomendasi;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getId_tujuan() {
        return id_tujuan;
    }

    public void setId_tujuan(String id_tujuan) {
        this.id_tujuan = id_tujuan;
    }

    public String getJenis_layanan() {
        return jenis_layanan;
    }

    public void setJenis_layanan(String jenis_layanan) {
        this.jenis_layanan = jenis_layanan;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public void setWisata(ArrayList<Wisata> wisata) {
        this.wisata = wisata;
    }

    public void setKuliner(ArrayList<Kuliner> kuliner) {
        this.kuliner = kuliner;
    }

    public void setPenginapan(ArrayList<Penginapan> penginapan) {
        this.penginapan = penginapan;
    }

    public void setKamar(ArrayList<Kamar> kamar) {
        this.kamar = kamar;
    }

    public void setMenu(ArrayList<Menu> menu) {
        this.menu = menu;
    }

    public ArrayList<Wisata> getWisata() {
        return wisata;
    }

    public ArrayList<Kuliner> getKuliner() {
        return kuliner;
    }

    public ArrayList<Penginapan> getPenginapan() {
        return penginapan;
    }

    public ArrayList<Kamar> getKamar() {
        return kamar;
    }

    public ArrayList<Menu> getMenu() {
        return menu;
    }
}
