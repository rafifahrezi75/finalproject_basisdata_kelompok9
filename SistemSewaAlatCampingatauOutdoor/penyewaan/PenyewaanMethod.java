package penyewaan;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import alat.Alat;
import alat.AlatDAO;
import alat.AlatMethod;
import user.User;
import user.UserDAO;
import detail_penyewaan.DetailPenyewaan;
import detail_penyewaan.DetailPenyewaanDAO;
import detail_penyewaan.DetailPenyewaanMethod;

public class PenyewaanMethod {
  private static final Scanner scanner = new Scanner(System.in);

  public static void handleMenuPenyewaan(PenyewaanDAO penyewaanDAO, UserDAO userDAO) {
    boolean exit = false;

    while (!exit) {
      System.out.println("\n====== Menu Penyewaan ======");
      System.out.println("1. Lihat semua penyewaan");
      System.out.println("2. Kembali ke menu utama");
      System.out.print("Pilih menu: ");
      String pilihan = scanner.nextLine();

      switch (pilihan) {
        case "1":
          getAllPenyewaan(penyewaanDAO);
          break;
        case "2":
          exit = true;
          break;
        default:
          System.out.println("Pilihan tidak valid!");
      }
    }
  }

  public static void handleMenuPenyewaanPelanggan(PenyewaanDAO penyewaanDAO, User currentUser) {
    List<Penyewaan> list = penyewaanDAO.getPenyewaanByUserId(currentUser.getIdUser());

    if (list.isEmpty()) {
        System.out.println("Tidak ada data histori penyewaan.");
        return;
    }

    System.out.println("\n=== DAFTAR HISTORI SEWA ===");
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

    System.out.print("\nLihat detail penyewaan? (Y/N): ");
    String jawab = scanner.nextLine();

    if (jawab.equalsIgnoreCase("Y")) {
        DetailPenyewaanMethod.lihatDetailPenyewaan();
    }
  }


  public static void getAllPenyewaan(PenyewaanDAO penyewaanDAO) {
    List<Penyewaan> list = penyewaanDAO.getAllPenyewaan();

    if (list.isEmpty()) {
      System.out.println("Tidak ada data penyewaan.");
      return;
    }

    System.out.println("\n=== DAFTAR SEMUA PENYEWAAN ===");
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

    System.out.print("\nLihat detail penyewaan? (Y/N): ");
    String jawab = scanner.nextLine();

    if (jawab.equalsIgnoreCase("Y")) {
      DetailPenyewaanMethod.lihatDetailPenyewaan();
    }
  }

  public static void sewaAlat(PenyewaanDAO penyewaanDAO, DetailPenyewaanDAO detailDAO, AlatDAO alatDAO, User currentUser) {
    AlatMethod.tampilkanSemuaAlatReady(alatDAO);

    List<Alat> alatDipilih = new ArrayList<>();
    while (true) {
      System.out.print("Masukkan ID Alat yang ingin disewa (0 untuk selesai): ");
      int id = Integer.parseInt(scanner.nextLine());
      if (id == 0) break;

      Alat alat = alatDAO.getAlatById(id);
      if (alat != null) {
        if (alat.getStok() == 0) {
          System.out.println("Stok alat '" + alat.getNamaAlat() + "' habis dan tidak bisa disewa.");
        } else if (alatDipilih.contains(alat)) {
          System.out.println("Alat '" + alat.getNamaAlat() + "' sudah dipilih.");
        } else {
          alatDipilih.add(alat);
        }
      }
    }

    if (alatDipilih.isEmpty()) {
        System.out.println("Tidak ada alat yang dipilih.");
        return;
    }

    Timestamp tglSewa = new Timestamp(System.currentTimeMillis());

    System.out.print("Masukkan tanggal rencana menyewa (format: YYYY-MM-DD HH:MM): ");
    String input = scanner.nextLine();
    input = input + ":00"; // Lengkapi detik agar valid

    System.out.print("Masukkan lama sewa (dalam hari): ");
    int lamaSewaHari = Integer.parseInt(scanner.nextLine());

    Timestamp tglRencanaMenyewa = null;
    try {
        tglRencanaMenyewa = Timestamp.valueOf(input);
    } catch (IllegalArgumentException e) {
        System.out.println("Format tanggal salah! Gunakan format: YYYY-MM-DD HH:MM");
        return;
    }

    // Hitung tanggal kembali
    LocalDateTime kembaliDateTime = tglRencanaMenyewa.toLocalDateTime().plusDays(lamaSewaHari);
    Timestamp tglKembali = Timestamp.valueOf(kembaliDateTime);

    System.out.print("Masukkan barang jaminan: ");
    String jaminan = scanner.nextLine();

    Penyewaan penyewaan = new Penyewaan();
    penyewaan.setUser(currentUser);
    penyewaan.setTglSewa(tglSewa);
    penyewaan.setTglRencanaMenyewa(tglRencanaMenyewa);
    penyewaan.setTglKembali(tglKembali);
    penyewaan.setBarangJaminan(jaminan);

    int idPenyewaan = penyewaanDAO.insertPenyewaan(penyewaan); // asumsi mengembalikan id
    if (idPenyewaan == -1) {
        System.out.println("Gagal menyimpan penyewaan.");
        return;
    }

    // Tetapkan ID ke objek penyewaan baru
    penyewaan.setIdPenyewaan(idPenyewaan);

    for (Alat alat : alatDipilih) {
      int jumlahBarang = 0;
      while (true) {
        System.out.print("Masukkan jumlah barang untuk '" + alat.getNamaAlat() + "' (Stok: " + alat.getStok() + "): ");
      try {
          jumlahBarang = Integer.parseInt(scanner.nextLine());
          if (jumlahBarang <= 0) {
            System.out.println("Jumlah harus lebih dari 0.");
          } else if (jumlahBarang > alat.getStok()) {
            System.out.println("Jumlah melebihi stok tersedia. Maksimum: " + alat.getStok());
          } else {
            break;
          }
        } catch (NumberFormatException e) {
            System.out.println("Input tidak valid. Masukkan angka.");
        }
      }

      DetailPenyewaan dp = new DetailPenyewaan(penyewaan, alat, jumlahBarang);
      detailDAO.insertDetailPenyewaan(dp);
    }

    System.out.println("Penyewaan berhasil disimpan!");
  }

  public static void setujui(PenyewaanDAO penyewaanDAO) {
    List<Penyewaan> list = penyewaanDAO.getAllPenyewaansSetujui();

    if (list.isEmpty()) {
      System.out.println("Tidak ada data penyewaan.");
      return;
    }

    System.out.println("\n=== DAFTAR SEMUA PENYEWAAN ===");
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

    System.out.print("\nMasukkan ID penyewaan yang ingin disetujui (0 untuk batal): ");
    try {
        int id = Integer.parseInt(scanner.nextLine());
        if (id == 0) {
            System.out.println("Pembatalan dipilih.");
            return;
        }

        Penyewaan p = penyewaanDAO.getPenyewaanById(id);
        if (p == null) {
            System.out.println("ID penyewaan tidak ditemukan.");
            return;
        }

        if (!p.getStatus().equalsIgnoreCase("Belum_Disetujui")) {
            System.out.println("Penyewaan ini sudah diproses sebelumnya.");
            return;
        }

        boolean success = penyewaanDAO.setujuiPenyewaan(id);
        if (success) {
            System.out.println("Penyewaan berhasil disetujui!");
        } else {
            System.out.println("Gagal menyetujui penyewaan.");
        }
    } catch (NumberFormatException e) {
        System.out.println("Input tidak valid. Masukkan angka ID.");
    }
  }

}
