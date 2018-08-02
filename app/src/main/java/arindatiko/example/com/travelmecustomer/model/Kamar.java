package arindatiko.example.com.travelmecustomer.model;

import java.util.List;

public class Kamar {
    private int id_kamar;
    private int id_penginapan;
    private String nama;
    private int banyak_kamar;
    private int kapasitas;
    private String fasilitas;
    private Double harga;
    private String foto;
    private String last_update;
    private Penginapan penginapan;

    private Double totalHarga;

    public Kamar() {
    }


    public Kamar(int id_kamar, int id_penginapan, String nama, int banyak_kamar, int kapasitas, String fasilitas, Double harga, String foto, String last_update) {
        this.id_kamar = id_kamar;
        this.id_penginapan = id_penginapan;
        this.nama = nama;
        this.banyak_kamar = banyak_kamar;
        this.kapasitas = kapasitas;
        this.fasilitas = fasilitas;
        this.harga = harga;
        this.foto = foto;
        this.last_update = last_update;
    }

    public Kamar(String nama, String fasilitas) {
        this.nama = nama;
        this.fasilitas = fasilitas;
    }

    public String getLast_update() {
        return last_update;
    }

    public void setLast_update(String last_update) {
        this.last_update = last_update;
    }

    public Double getTotalHarga() {
        return totalHarga;
    }

    public void setTotalHarga(Double totalHarga) {
        this.totalHarga = totalHarga;
    }

    public int getId_kamar() {
        return id_kamar;
    }

    public void setId_kamar(int id_kamar) {
        this.id_kamar = id_kamar;
    }

    public int getId_penginapan() {
        return id_penginapan;
    }

    public void setId_penginapan(int id_penginapan) {
        this.id_penginapan = id_penginapan;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getBanyak_kamar() {
        return banyak_kamar;
    }

    public void setBanyak_kamar(int banyak_kamar) {
        this.banyak_kamar = banyak_kamar;
    }

    public int getKapasitas() {
        return kapasitas;
    }

    public void setKapasitas(int kapasitas) {
        this.kapasitas = kapasitas;
    }

    public String getFasilitas() {
        return fasilitas;
    }

    public void setFasilitas(String fasilitas) {
        this.fasilitas = fasilitas;
    }

    public Double getHarga() {
        return harga;
    }

    public void setHarga(Double harga) {
        this.harga = harga;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Penginapan getPenginapan() {
        return penginapan;
    }

    public void setPenginapan(Penginapan penginapan) {
        this.penginapan = penginapan;
    }
}
