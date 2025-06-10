package tagihan_penyewaan;

public class TagihanPenyewaan {
    private double totalSewa;
    private double totalDenda;
    private double totalTagihan;

    public TagihanPenyewaan(double totalSewa, double totalDenda, double totalTagihan) {
        this.totalSewa = totalSewa;
        this.totalDenda = totalDenda;
        this.totalTagihan = totalTagihan;
    }

    public double getTotalSewa() {
        return totalSewa;
    }

    public double getTotalDenda() {
        return totalDenda;
    }

    public double getTotalTagihan() {
        return totalTagihan;
    }
}
