package report;

import java.util.List;
import java.util.Scanner;

public class ReportMethod {
  static Scanner scanner = new Scanner(System.in);

    public static void handleMenuReport(ReportDAO reportDAO) {
        boolean running = true;

        while (running) {
            System.out.println("\n=== KELOLA ALAT ===");
            System.out.println("1. Lihat 3 Pelanggan Teratas");
            System.out.println("2. Laporan Denda");
            System.out.println("3. Laporan Bulanan");
            System.out.println("4. Kembali");
            System.out.print("Pilih: ");
            String pilihan = scanner.nextLine();

            switch (pilihan) {
                case "1" -> tampilkanPelangganSetiaSubQuery(reportDAO);
                case "2" -> tampilkanLaporanSewaAlat(reportDAO);
                case "3" -> tampilkanLaporanPendapatanBulananCTE(reportDAO);
                case "4" -> running = false;
                default -> System.out.println("Pilihan tidak valid.");
            }
        }
    }

    public static void tampilkanPelangganSetiaSubQuery(ReportDAO reportDAO) {
      List<PelangganSetia> list = reportDAO.getPelangganSetiaSubQuery();

      if (list.isEmpty()) {
          System.out.println("Tidak ada data pelanggan setia.");
          return;
      }

      System.out.println("\n=== TOP 3 PELANGGAN SETIA ===");
      System.out.printf("%-5s %-20s %-20s%n", "No", "Nama Pelanggan", "Total Penyewaan");

      int no = 1;
      for (PelangganSetia p : list) {
          System.out.printf("%-5d %-20s %-20d%n", no++, p.getNama(), p.getTotalPenyewaan());
      }
  }

  public static void tampilkanLaporanPendapatanBulananCTE(ReportDAO reportDAO) {
    LaporanPendapatanBulanan laporan = reportDAO.getLaporanPendapatanBulananCTE();

    String[] namaBulan = {
        "Januari", "Februari", "Maret", "April", "Mei", "Juni",
        "Juli", "Agustus", "September", "Oktober", "November", "Desember"
    };

    System.out.println("\n=== LAPORAN PENDAPATAN PENYEWAAN PER BULAN ===");
    for (int i = 0; i < 12; i++) {
        System.out.printf("%-10s : Rp %, .2f%n", namaBulan[i], laporan.getBulan(i));
    }
  }

  public static void tampilkanLaporanSewaAlat(ReportDAO reportDAO) {
    List<LaporanDendaPerbulan> laporan = reportDAO.getLaporanDendaPerBulanCrosstab();

    System.out.printf("%-20s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s\n",
        "Pelanggan", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");

    for (LaporanDendaPerbulan l : laporan) {
        System.out.printf("%-20s ", l.getPelanggan());
        for (int i = 0; i < 12; i++) {
            System.out.printf("%8.2f ", l.getDendaPerBulan(i));
        }
        System.out.println();
    }
  }

}
