package arindatiko.example.com.travelmecustomer.model;

public class User {
    private String id_user, nama_lengkap, alamat, email, no_telp, username, user_type;
    private Double posisi_lat, posisi_lng;

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getNama_lengkap() {
        return nama_lengkap;
    }

    public void setNama_lengkap(String nama_lengkap) {
        this.nama_lengkap = nama_lengkap;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNo_telp() {
        return no_telp;
    }

    public void setNo_telp(String no_telp) {
        this.no_telp = no_telp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
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
}
