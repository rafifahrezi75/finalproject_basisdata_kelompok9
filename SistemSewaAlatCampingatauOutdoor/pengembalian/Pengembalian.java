package pengembalian;

import penyewaan.Penyewaan;
import java.sql.Timestamp;

public class Pengembalian {
    private int idPengembalian;
    private Timestamp tglDikembalikan;
    private String statusBayar;
    private Penyewaan penyewaan;

    public Pengembalian() {}

    public Pengembalian(int idPengembalian, Timestamp tglDikembalikan, String statusBayar) {
        this.idPengembalian = idPengembalian;
        this.tglDikembalikan = tglDikembalikan;
        this.statusBayar = statusBayar;
    }

    public int getIdPengembalian() {
        return idPengembalian;
    }

    public void setIdPengembalian(int idPengembalian) {
        this.idPengembalian = idPengembalian;
    }

    public Timestamp getTglDikembalikan() {
        return tglDikembalikan;
    }

    public void setTglDikembalikan(Timestamp tglDikembalikan) {
        this.tglDikembalikan = tglDikembalikan;
    }

    public String getStatusBayar() {
        return statusBayar;
    }

    public void setStatusBayar(String statusBayar) {
        this.statusBayar = statusBayar;
    }

    public Penyewaan getPenyewaan() {
        return penyewaan;
    }

    public void setPenyewaan(Penyewaan penyewaan) {
        this.penyewaan = penyewaan;
    }
}