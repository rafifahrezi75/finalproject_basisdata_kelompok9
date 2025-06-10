package report;

public class PelangganSetia {
    private int idUser;
    private String nama;
    private int totalPenyewaan;

    // Constructor
    public PelangganSetia(int idUser, String nama, int totalPenyewaan) {
        this.idUser = idUser;
        this.nama = nama;
        this.totalPenyewaan = totalPenyewaan;
    }

    // Getter dan Setter
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

    public int getTotalPenyewaan() {
        return totalPenyewaan;
    }

    public void setTotalPenyewaan(int totalPenyewaan) {
        this.totalPenyewaan = totalPenyewaan;
    }
}

