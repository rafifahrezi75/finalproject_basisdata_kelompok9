package detail_penyewaan;

import java.util.List;
import java.util.Scanner;

import pengembalian.Pengembalian;
import tagihan_penyewaan.TagihanPenyewaan;
import tagihan_penyewaan.TagihanPenyewaanDAO;
import pengembalian.PengembalianDAO;

public class DetailPenyewaanMethod {
    private static final Scanner scanner = new Scanner(System.in);

    public static void lihatDetailPenyewaan() {
        System.out.print("Masukkan ID penyewaan: ");
        int id = Integer.parseInt(scanner.nextLine());

        DetailPenyewaanDAO detailPenyewaanDAO = new DetailPenyewaanDAO();
        List<DetailPenyewaan> detailList = detailPenyewaanDAO.getDetailByIdPenyewaan(id);

        if (detailList.isEmpty()) {
            System.out.println("Tidak ada detail untuk ID tersebut.");
            return;
        }

        System.out.println("\n=== DETAIL PENYEWAAN ID " + id + " ===");
        System.out.printf("%-5s %-20s %-10s %-10s%n", "ID", "Nama Alat", "Jumlah", "Harga");

        for (DetailPenyewaan detail : detailList) {
            System.out.printf("%-5d %-20s %-10d %-10d%n",
                detail.getIdDetailPenyewaan(),
                detail.getAlat().getNamaAlat(),
                detail.getJumlahBarang(),
                detail.getAlat().getHargaPerHari()
            );
        }

        TagihanPenyewaanDAO tagihanPenyewaanDAO = new TagihanPenyewaanDAO();
        TagihanPenyewaan tagihan = tagihanPenyewaanDAO.getProcedure(id);
        if (tagihan != null) {
            System.out.println("\n=== RINCIAN TAGIHAN ===");
            System.out.println("Total Harga Sewa : Rp" + tagihan.getTotalSewa());
            System.out.println("Total Denda      : Rp" + tagihan.getTotalDenda());
            System.out.println("Total Tagihan    : Rp" + tagihan.getTotalTagihan());
        } else {
            System.out.println("Tidak bisa menghitung total tagihan.");
        }

        PengembalianDAO pengembalianDAO = new PengembalianDAO();
        Pengembalian pengembalian = pengembalianDAO.getPengembalianByIdPenyewaan(id);
        if (pengembalian != null) {
            System.out.println("\n=== RINCIAN PENGEMBALIAN ===");
            System.out.println("Tanggal dikembalikan: " + pengembalian.getTglDikembalikan());
            System.out.println("Status Bayar: " + pengembalian.getStatusBayar());
        } else {
            System.out.println("\n=== RINCIAN PENGEMBALIAN ===");
            System.out.println("Tanggal dikembalikan: - ");
            System.out.println("Status Bayar: Belum_Lunas");
        }

    }
}