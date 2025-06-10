package penyewaan;

import user.User;
import java.sql.Timestamp;

public class Penyewaan {
    private int idPenyewaan;
    private User user; // Komposisi
    private Timestamp tglSewa;
    private Timestamp tglRencanaMenyewa;
    private Timestamp tglKembali;
    private String barangJaminan;
    private String statusSewa;
    private String status;

    // Constructor lengkap
    public Penyewaan(int idPenyewaan, User user, Timestamp tglSewa, Timestamp tglRencanaMenyewa, Timestamp tglKembali, String barangJaminan, String status) {
        this.idPenyewaan = idPenyewaan;
        this.user = user;
        this.tglSewa = tglSewa;
        this.tglRencanaMenyewa = tglRencanaMenyewa;
        this.tglKembali = tglKembali;
        this.barangJaminan = barangJaminan;
        this.statusSewa = "Belum Selesai"; // default
        this.status = status;
    }

    public Penyewaan(int idPenyewaan, Timestamp tglSewa, Timestamp tglRencanaMenyewa, Timestamp tglKembali, String barangJaminan, String status) {
        this.idPenyewaan = idPenyewaan;
        this.tglSewa = tglSewa;
        this.tglRencanaMenyewa = tglRencanaMenyewa;
        this.tglKembali = tglKembali;
        this.barangJaminan = barangJaminan;
        this.statusSewa = "Belum Selesai"; // default
        this.status = status;
    }

    // Constructor tanpa id (misal untuk insert baru)
    public Penyewaan(User user, Timestamp tglSewa, Timestamp tglRencanaMenyewa, Timestamp tglKembali, String barangJaminan, String status) {
        this(0, user, tglSewa, tglRencanaMenyewa, tglKembali, barangJaminan, status);
    }

    // Getter dan Setter

    public Penyewaan() {
    }

    public int getIdPenyewaan() {
        return idPenyewaan;
    }

    public void setIdPenyewaan(int idPenyewaan) {
        this.idPenyewaan = idPenyewaan;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Timestamp getTglSewa() {
        return tglSewa;
    }

    public void setTglSewa(Timestamp tglSewa) {
        this.tglSewa = tglSewa;
    }

    public Timestamp getTglRencanaMenyewa() {
        return tglRencanaMenyewa;
    }

    public void setTglRencanaMenyewa(Timestamp tglRencanaMenyewa) {
        this.tglRencanaMenyewa = tglRencanaMenyewa;
    }

    public Timestamp getTglKembali() {
        return tglKembali;
    }

    public void setTglKembali(Timestamp tglKembali) {
        this.tglKembali = tglKembali;
    }

    public String getBarangJaminan() {
        return barangJaminan;
    }

    public void setBarangJaminan(String barangJaminan) {
        this.barangJaminan = barangJaminan;
    }

    public String getStatusSewa() {
        return statusSewa;
    }

    public void setStatusSewa(String statusSewa) {
        this.statusSewa = statusSewa;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}