package arindatiko.example.com.travelmecustomer.model;

public class Drivers {
    private String nama_lengkap, alamat, no_telp, username, password, user_type, Token;
    private int id_user;
    private Double posisi_lat, posisi_lng;

    public Drivers() {

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

    public String getNama_lengkap() {
        return nama_lengkap;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getNo_telp() {
        return no_telp;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getUser_type() {
        return user_type;
    }

    public String getToken() {
        return Token;
    }

    public int getId_user() {
        return id_user;
    }

    public void setNama_lengkap(String nama_lengkap) {
        this.nama_lengkap = nama_lengkap;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public void setNo_telp(String no_telp) {
        this.no_telp = no_telp;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public void setToken(String token) {
        this.Token = token;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }
}
