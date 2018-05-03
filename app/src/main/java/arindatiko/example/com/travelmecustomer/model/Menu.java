package arindatiko.example.com.travelmecustomer.model;

public class Menu {
    private int id_menu;
    private int id_kuliner;
    private String nama;
    private Double harga;
    private String foto;
    private String deskripsi;
    private Kuliner kuliner;

    public Menu() {
    }

    public Menu(int id_menu, int id_kuliner, String nama, Double harga, String foto, String deskripsi) {
        this.id_menu = id_menu;
        this.id_kuliner = id_kuliner;
        this.nama = nama;
        this.harga = harga;
        this.foto = foto;
        this.deskripsi = deskripsi;
    }

    public int getId_menu() {
        return id_menu;
    }

    public int getId_kuliner() {
        return id_kuliner;
    }

    public String getNama() {
        return nama;
    }

    public Double getHarga() {
        return harga;
    }

    public String getFoto() {
        return foto;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public Kuliner getKuliner() {
        return kuliner;
    }
}
