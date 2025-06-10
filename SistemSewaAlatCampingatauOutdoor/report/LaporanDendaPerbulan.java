package report;

public class LaporanDendaPerbulan {
    private String pelanggan;
    private double[] dendaPerBulan = new double[12];

    public LaporanDendaPerbulan() {}

    public LaporanDendaPerbulan(String pelanggan, double[] dendaPerBulan) {
        this.pelanggan = pelanggan;
        this.dendaPerBulan = dendaPerBulan;
    }

    public String getPelanggan() {
        return pelanggan;
    }

    public void setPelanggan(String pelanggan) {
        this.pelanggan = pelanggan;
    }

    public double getDendaPerBulan(int bulanIndex) {
        if (bulanIndex < 0 || bulanIndex >= 12) {
            throw new IndexOutOfBoundsException("Indeks bulan harus 0-11");
        }
        return dendaPerBulan[bulanIndex];
    }

    public void setDendaPerBulan(int bulanIndex, double nilai) {
        if (bulanIndex < 0 || bulanIndex >= 12) {
            throw new IndexOutOfBoundsException("Indeks bulan harus 0-11");
        }
        dendaPerBulan[bulanIndex] = nilai;
    }

    public double[] getDendaPerBulan() {
        return dendaPerBulan;
    }

    public void setDendaPerBulan(double[] dendaPerBulan) {
        if (dendaPerBulan.length != 12) {
            throw new IllegalArgumentException("Array dendaPerBulan harus panjang 12");
        }
        this.dendaPerBulan = dendaPerBulan;
    }
}