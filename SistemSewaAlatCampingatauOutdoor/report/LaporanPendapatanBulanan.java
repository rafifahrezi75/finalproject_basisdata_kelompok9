package report;

public class LaporanPendapatanBulanan {
    private double[] pendapatanPerBulan = new double[12];

    public double getBulan(int index) {
        if (index < 0 || index >= 12) {
            throw new IndexOutOfBoundsException("Index bulan harus antara 0 sampai 11");
        }
        return pendapatanPerBulan[index];
    }

    public void setBulan(int index, double nilai) {
        if (index < 0 || index >= 12) {
            throw new IndexOutOfBoundsException("Index bulan harus antara 0 sampai 11");
        }
        pendapatanPerBulan[index] = nilai;
    }
}

