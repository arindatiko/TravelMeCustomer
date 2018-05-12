package arindatiko.example.com.travelmecustomer.model;

import java.util.List;

public class Penginapan {
    private int id_penginapan;
    private int id_admin;
    private String nama;
    private String alamat;
    private String no_telp;
    private Double posisi_lat;
    private Double posisi_lng;
    private String foto;
    private String deskripsi;
    private String fasilitas;
    private String akses;
    private List<Kamar> kamar;

    public Penginapan() {
    }

    public Penginapan(int id_penginapan, int id_admin, String nama, String alamat, String no_telp, Double posisi_lat,
                      Double posisi_lng, String foto, String deskripsi, String fasilitas, String akses) {
        this.id_penginapan = id_penginapan;
        this.id_admin = id_admin;
        this.nama = nama;
        this.alamat = alamat;
        this.no_telp = no_telp;
        this.posisi_lat = posisi_lat;
        this.posisi_lng = posisi_lng;
        this.foto = foto;
        this.deskripsi = deskripsi;
        this.fasilitas = fasilitas;
        this.akses = akses;
    }

    public List<Kamar> getKamar() {
        return kamar;
    }

    public int getId_penginapan() {
        return id_penginapan;
    }

    public void setId_penginapan(int id_penginapan) {
        this.id_penginapan = id_penginapan;
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

    public String getAkses() {
        return akses;
    }

    public void setAkses(String akses) {
        this.akses = akses;
    }
}
