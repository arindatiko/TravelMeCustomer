package arindatiko.example.com.travelmecustomer.model;

public class User {
    private String nama_lengkap, alamat, no_telp, username, password, user_type, Token;
    private int id_user, flag;

    public User() {
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
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
