package kategori;

import java.util.List;
import java.util.Scanner;

public class KategoriMethod {
  static Scanner scanner = new Scanner(System.in);

  public static void handleMenuKategori(KategoriDAO kategoriDAO) {
    boolean menu = true;
    while (menu) {
      System.out.println("\n=== KELOLA KATEGORI ===");
      System.out.println("1. Lihat Semua Kategori");
      System.out.println("2. Tambah Kategori");
      System.out.println("3. Edit Kategori");
      System.out.println("4. Hapus Kategori");
      System.out.println("5. Kembali");
      System.out.print("Pilih: ");
      String pilihan = scanner.nextLine();

      switch (pilihan) {
        case "1" -> tampilkanSemuaKategori(kategoriDAO);
        case "2" -> tambahKategori(kategoriDAO);
        case "3" -> editKategori(kategoriDAO);
        case "4" -> hapusKategori(kategoriDAO);
        case "5" -> menu = false;
        default -> System.out.println("Pilihan tidak valid.");
      }
    }
  }

  public static void tampilkanSemuaKategori(KategoriDAO kategoriDAO) {
    List<Kategori> list = kategoriDAO.getAllKategori();
    if (list.isEmpty()) {
      System.out.println("Belum ada kategori.");
      return;
    }

    System.out.println("\n=== DAFTAR KATEGORI ===");
    System.out.printf("%-5s %-20s%n", "ID", "Nama Kategori");
    System.out.println("---------------------------");
    for (Kategori k : list) {
      System.out.printf("%-5d %-20s%n", k.getIdKategori(), k.getNamaKategori());
    }
  }

  public static void tambahKategori(KategoriDAO kategoriDAO) {
    System.out.print("Nama kategori baru: ");
    String nama = scanner.nextLine();
    Kategori kategori = new Kategori(nama);
    kategoriDAO.insertKategori(kategori);
  }

  public static void editKategori(KategoriDAO kategoriDAO) {
    boolean repeat = true;

    while (repeat) {
        System.out.print("Cari kategori yang ingin diubah (ID atau Nama): ");
        String kataKunci = scanner.nextLine();
        List<Kategori> hasilPencarian = kategoriDAO.searchKategori(kataKunci);

        if (hasilPencarian == null || hasilPencarian.isEmpty()) {
            System.out.println("Tidak ada kategori yang ditemukan. Kembali ke menu.\n");
            return; // kembali ke menu utama kategori
        }

        // Tampilkan semua hasil pencarian
        System.out.println("\nHasil pencarian:");
        for (Kategori k : hasilPencarian) {
            System.out.println(k.getIdKategori() + " - " + k.getNamaKategori());
        }

        System.out.print("(C)ari Lagi ATAU (U)bah ID Kategori dari data di atas: ");
        String pilihAksi = scanner.nextLine();

        if (pilihAksi.equalsIgnoreCase("C")) {
            continue; // ulangi pencarian
        } else if (pilihAksi.equalsIgnoreCase("U")) {
            int id_kategori2;

            while (true) {
                System.out.print("Masukkan ID Kategori yang ingin diubah: ");
                String input = scanner.nextLine();
                try {
                    id_kategori2 = Integer.parseInt(input);
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Input tidak valid. Masukkan angka.");
                }
            }

            System.out.print("Nama Baru: ");
            String nama = scanner.nextLine();

            kategoriDAO.updateKategori(new Kategori(id_kategori2, nama));
            return; // selesai dan kembali ke menu utama
        } else if (pilihAksi.equalsIgnoreCase("")){
            System.out.println("Kembali ke menu utama.");
            return;
        } else {
            System.out.println("Pilihan tidak dikenali. Kembali ke menu utama.");
            return;
        }
    }
  }

  public static void hapusKategori(KategoriDAO kategoriDAO) {
    System.out.print("Masukkan ID kategori yang akan dihapus: ");
    try {
      int id = Integer.parseInt(scanner.nextLine());
      kategoriDAO.deleteKategori(id);
    } catch (NumberFormatException e) {
      System.out.println("Input ID tidak valid.\n");
    }
  }
}
