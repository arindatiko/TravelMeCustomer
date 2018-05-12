package arindatiko.example.com.travelmecustomer.model;

import java.util.List;

public class Kuliner {
    private int id_kuliner;
    private int id_admin;
    private String nama;
    private String alamat;
    private String no_telp;
    private Double posisi_lat;
    private Double posisi_lng;
    private Double harga_tiket_parkir_motor;
    private Double harga_tiket_parkir_mobil;
    private Double harga_tiket_parkir_bus;
    private String foto;
    private String deskripsi;
    private String fasilitas;
    private String jam_buka;
    private String jam_tutup;
    private String akses;
    private List<Menu> menu;

    public Kuliner() {
    }

    public Kuliner(int id_kuliner, int id_admin, String nama, String alamat, String no_telp, Double posisi_lat, Double posisi_lng, Double harga_tiket_parkir_motor, Double harga_tiket_parkir_mobil,
                   Double harga_tiket_parkir_bus, String foto, String deskripsi, String fasilitas, String jam_buka, String jam_tutup, String akses) {
        this.id_kuliner = id_kuliner;
        this.id_admin = id_admin;
        this.nama = nama;
        this.alamat = alamat;
        this.no_telp = no_telp;
        this.posisi_lat = posisi_lat;
        this.posisi_lng = posisi_lng;
        this.harga_tiket_parkir_motor = harga_tiket_parkir_motor;
        this.harga_tiket_parkir_mobil = harga_tiket_parkir_mobil;
        this.harga_tiket_parkir_bus = harga_tiket_parkir_bus;
        this.foto = foto;
        this.deskripsi = deskripsi;
        this.fasilitas = fasilitas;
        this.jam_buka = jam_buka;
        this.jam_tutup = jam_tutup;
        this.akses = akses;
    }

    public List<Menu> getMenu() {
        return menu;
    }

    public int getId_kuliner() {
        return id_kuliner;
    }

    public void setId_kuliner(int id_kuliner) {
        this.id_kuliner = id_kuliner;
    }

    public int getId_admin() {
        return id_admin;
    }

    public void setId_admin(int id_admin) {
        this.id_admin = id_admin;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNo_telp() {
        return no_telp;
    }

    public void setNo_telp(String no_telp) {
        this.no_telp = no_telp;
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

    public Double getHarga_tiket_parkir_motor() {
        return harga_tiket_parkir_motor;
    }

    public void setHarga_tiket_parkir_motor(Double harga_tiket_parkir_motor) {
        this.harga_tiket_parkir_motor = harga_tiket_parkir_motor;
    }

    public Double getHarga_tiket_parkir_mobil() {
        return harga_tiket_parkir_mobil;
    }

    public void setHarga_tiket_parkir_mobil(Double harga_tiket_parkir_mobil) {
        this.harga_tiket_parkir_mobil = harga_tiket_parkir_mobil;
    }

    public Double getHarga_tiket_parkir_bus() {
        return harga_tiket_parkir_bus;
    }

    public void setHarga_tiket_parkir_bus(Double harga_tiket_parkir_bus) {
        this.harga_tiket_parkir_bus = harga_tiket_parkir_bus;
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

    public String getAkses() {
        return akses;
    }

    public void setAkses(String akses) {
        this.akses = akses;
    }
}
