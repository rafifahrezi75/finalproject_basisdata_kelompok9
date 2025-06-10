package detail_penyewaan;

import alat.Alat;
import penyewaan.Penyewaan;

public class DetailPenyewaan {
    private int idDetailPenyewaan;
    private Penyewaan penyewaan; // Komposisi
    private Alat alat;           // Komposisi
    private int jumlahBarang;

    // Constructor lengkap
    public DetailPenyewaan(int idDetailPenyewaan, Penyewaan penyewaan, Alat alat, int jumlahBarang) {
        this.idDetailPenyewaan = idDetailPenyewaan;
        this.penyewaan = penyewaan;
        this.alat = alat;
        this.jumlahBarang = jumlahBarang;
    }

    // Constructor tanpa ID (untuk penyimpanan baru)
    public DetailPenyewaan(Penyewaan penyewaan, Alat alat, int jumlahBarang) {
        this(0, penyewaan, alat, jumlahBarang);
    }

    // Getter & Setter
    public int getIdDetailPenyewaan() {
        return idDetailPenyewaan;
    }

    public void setIdDetailPenyewaan(int idDetailPenyewaan) {
        this.idDetailPenyewaan = idDetailPenyewaan;
    }

    public Penyewaan getPenyewaan() {
        return penyewaan;
    }

    public void setPenyewaan(Penyewaan penyewaan) {
        this.penyewaan = penyewaan;
    }

    public Alat getAlat() {
        return alat;
    }

    public void setAlat(Alat alat) {
        this.alat = alat;
    }

    public int getJumlahBarang() {
        return jumlahBarang;
    }

    public void setJumlahBarang(int jumlahBarang) {
        this.jumlahBarang = jumlahBarang;
    }
}