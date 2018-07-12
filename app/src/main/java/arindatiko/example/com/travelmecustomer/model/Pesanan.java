package arindatiko.example.com.travelmecustomer.model;

import java.util.List;

public class Pesanan {
    private int id_pesanan;
    private int id_customer;
    private int id_driver;
    private Double posisi_lat;
    private Double posisi_lng;
    private String waktu_pesan;
    private String waktu_acc;
    private Double total_budget;
    private int status;
    private int status_selesai;

    private User user;
    private Drivers drivers;
    private List<Tujuan> tujuan;

    public Pesanan() {
    }

    public int getId_pesanan() {
        return id_pesanan;
    }

    public int getId_customer() {
        return id_customer;
    }

    public int getId_driver() {
        return id_driver;
    }

    public Double getPosisi_lat() {
        return posisi_lat;
    }

    public Double getPosisi_lng() {
        return posisi_lng;
    }

    public String getWaktu_pesan() {
        return waktu_pesan;
    }

    public String getWaktu_acc() {
        return waktu_acc;
    }

    public Double getTotal_budget() {
        return total_budget;
    }

    public int getStatus() {
        return status;
    }

    public void setTujuan(List<Tujuan> tujuan) {
        this.tujuan = tujuan;
    }

    public List<Tujuan> getTujuan() {
        return tujuan;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Drivers getDrivers() {
        return drivers;
    }

    public void setDrivers(Drivers drivers) {
        this.drivers = drivers;
    }

    public int getStatus_selesai() {
        return status_selesai;
    }

    public void setStatus_selesai(int status_selesai) {
        this.status_selesai = status_selesai;
    }
}
