package user;

public class User {
    private int idUser;
    private String nama;
    private String password;
    private String role;
    private String noHp;
    private String alamat;

    public User(int idUser, String nama, String password, String role, String noHp, String alamat) {
        this.idUser = idUser;
        this.nama = nama;
        this.password = password;
        this.role = role;
        this.noHp = noHp;
        this.alamat = alamat;
    }

    public User(String nama, String password, String role, String noHp, String alamat) {
        this(0, nama, password, role, noHp, alamat);
    }

    // Getter & Setter
    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }
}
