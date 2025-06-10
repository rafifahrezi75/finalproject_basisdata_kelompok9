package pengembalian;

import java.util.Scanner;
import java.sql.Timestamp;
import java.util.List;
import penyewaan.Penyewaan;
import penyewaan.PenyewaanDAO;

public class PengembalianMethod {
    private static final Scanner scanner = new Scanner(System.in);

    public static void handleMenuPengembalian(PengembalianDAO pengembalianDAO, PenyewaanDAO penyewaanDAO) {
        boolean exit = false;

        while (!exit) {
            System.out.println("\n====== Menu Pengembalian ======");
            System.out.println("1. Kembalikan alat sewa");
            System.out.println("2. Kembali ke menu utama");
            System.out.print("Pilih menu: ");
            String pilihan = scanner.nextLine();

            switch (pilihan) {
                case "1":
                    getAllPenyewaan(pengembalianDAO, penyewaanDAO);
                    break;
                case "2":
                    exit = true;
                    break;
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        }
    }

    public static void getAllPenyewaan(PengembalianDAO pengembalianDAO, PenyewaanDAO penyewaanDAO) {
        List<Penyewaan> list = penyewaanDAO.getAllPenyewaanBelumSelesai();

        if (list.isEmpty()) {
            System.out.println("Tidak ada data penyewaan.");
            return;
        }

        System.out.println("\n=== DAFTAR PENYEWAAN ===");
        System.out.printf(
            "%-5s %-15s %-20s %-20s %-20s %-20s %-15s %-15s%n",
            "ID", "Nama User", "Tgl Sewa", "Tgl Rencana", "Tgl Kembali", "Barang Jaminan", "Status Sewa", "Status"
        );

        for (Penyewaan p : list) {
            System.out.printf(
                "%-5d %-15s %-20s %-20s %-20s %-20s %-15s %-15s%n",
                p.getIdPenyewaan(),
                p.getUser().getNama(),
                p.getTglSewa().toString(),
                p.getTglRencanaMenyewa().toString(),
                p.getTglKembali().toString(),
                p.getBarangJaminan(),
                p.getStatusSewa(),
                p.getStatus()
            );
        }
        
        System.out.print("\nPilih (ID) penyewaan yang akan dikembalikan: ");
        int jawab = scanner.nextInt();
        scanner.nextLine(); // clear buffer

        insertPengembalian(pengembalianDAO, penyewaanDAO, jawab);
    }

    public static void insertPengembalian(PengembalianDAO pengembalianDAO, PenyewaanDAO penyewaanDAO, int idPenyewaan) {
        
        Penyewaan penyewaan = penyewaanDAO.getPenyewaanById(idPenyewaan);
        
        if (penyewaan == null) {
            System.out.println("Penyewaan tidak ditemukan.");
            return;
        }

        if (!"Disetujui".equalsIgnoreCase(penyewaan.getStatus())) {
            System.out.println("Penyewaan belum disetujui. Tidak bisa melakukan pengembalian.");
            return;
        }

        Pengembalian pengembalian = new Pengembalian();
        pengembalian.setPenyewaan(penyewaan);

        System.out.print("Masukkan tanggal dikembalikan (format: YYYY-MM-DD HH:MM): ");
        String input = scanner.nextLine();
        input = input + ":00"; // Lengkapi detik agar valid

        Timestamp tglDiKembalikan = null;
        try {
            tglDiKembalikan = Timestamp.valueOf(input);
        } catch (IllegalArgumentException e) {
            System.out.println("Format tanggal salah! Gunakan format: YYYY-MM-DD HH:MM");
            return;
        }

        pengembalian.setTglDikembalikan(tglDiKembalikan);
        pengembalian.setStatusBayar("Lunas");

        boolean success = pengembalianDAO.insertPengembalian(pengembalian);

        if (success) {
            System.out.println("Pengembalian berhasil dicatat.");
        } else {
            System.out.println("Gagal mencatat pengembalian.");
        }
    }


  // public static void getAllPengembalian(PengembalianDAO pengembalianDAO) {
  //   List<Pengembalian> list = pengembalianDAO.getAllPengembalianWithPenyewaan();

  //   if (list.isEmpty()) {
  //       System.out.println("Tidak ada data pengembalian.");
  //       return;
  //   }

  //   System.out.println("\n=== DAFTAR SEMUA PENGEMBALIAN ===");
  //   System.out.printf(
  //       "%-5s %-15s %-15s %-15s %-15s %-15s%n",
  //       "ID", "Tgl Kembali", "Tgl Sewa", "Rencana Kembali", "Jatuh Tempo", "Status Bayar"
  //   );

  //   for (Pengembalian p : list) {
  //       Penyewaan penyewaan = p.getPenyewaan();

  //       System.out.printf(
  //           "%-5d %-15s %-15s %-15s %-15s %-15s%n",
  //           p.getIdPengembalian(),
  //           p.getTglDikembalikan(),
  //           penyewaan.getTglSewa(),
  //           penyewaan.getTglRencanaMenyewa(),
  //           penyewaan.getTglKembali(),
  //           p.getStatusBayar()
  //       );
  //   }
  // }

}