package alat;

import kategori.Kategori;
import kategori.KategoriDAO;
import java.util.List;
import java.util.Scanner;

public class AlatMethod {
    static Scanner scanner = new Scanner(System.in);

    public static void handleMenuAlat(AlatDAO alatDAO, KategoriDAO kategoriDAO) {
        boolean running = true;

        while (running) {
            System.out.println("\n=== KELOLA ALAT ===");
            System.out.println("1. Lihat Semua Alat");
            System.out.println("2. Tambah Alat");
            System.out.println("3. Edit Alat");
            System.out.println("4. Hapus Alat");
            System.out.println("5. Kembali");
            System.out.print("Pilih: ");
            String pilihan = scanner.nextLine();

            switch (pilihan) {
                case "1" -> tampilkanSemuaAlat(alatDAO);
                case "2" -> tambahAlat(alatDAO, kategoriDAO);
                case "3" -> editAlat(alatDAO, kategoriDAO);
                case "4" -> hapusAlat(alatDAO);
                case "5" -> running = false;
                default -> System.out.println("Pilihan tidak valid.");
            }
        }
    }

    public static void tampilkanSemuaAlat(AlatDAO alatDAO) {
        List<Alat> list = alatDAO.getAllAlat();
        if (list.isEmpty()) {
            System.out.println("Belum ada data alat.");
            return;
        }

        System.out.println("\n=== DAFTAR ALAT ===");
        System.out.printf("%-5s %-20s %-15s %-5s %-10s%n", "ID", "Nama", "Kategori", "Stok", "Harga/Hari");
        for (Alat alat : list) {
            System.out.printf("%-5d %-20s %-15s %-5d %-10d%n",
                    alat.getIdAlat(),
                    alat.getNamaAlat(),
                    alat.getKategori().getNamaKategori(),
                    alat.getStok(),
                    alat.getHargaPerHari());
        }
    }

    public static void tampilkanSemuaAlatReady(AlatDAO alatDAO) {
        List<AlatTersediaView> list = alatDAO.getAllAlatReady();
        if (list.isEmpty()) {
            System.out.println("Belum ada data alat.");
            return;
        }

        System.out.println("\n=== DAFTAR ALAT ===");
        System.out.printf("%-5s %-20s %-15s %-5s %-10s%n", "ID", "Nama", "Kategori", "Stok", "Harga/Hari");
        for (AlatTersediaView alat : list) {
            System.out.printf("%-5d %-20s %-15s %-5d %-10d%n",
                    alat.getIdAlat(),
                    alat.getNamaAlat(),
                    alat.getKategori().getNamaKategori(),
                    alat.getStok(),
                    alat.getHargaPerHari());
        }
    }

    public static void tambahAlat(AlatDAO alatDAO, KategoriDAO kategoriDAO) {
        System.out.print("Nama alat: ");
        String nama = scanner.nextLine();

        Kategori kategori = pilihKategori(kategoriDAO);
        if (kategori == null) {
            System.out.println("Proses tambah alat dibatalkan.");
            return;
        }

        System.out.print("Stok: ");
        int stok = Integer.parseInt(scanner.nextLine());

        System.out.print("Harga per hari: ");
        int harga = Integer.parseInt(scanner.nextLine());

        Alat alat = new Alat(nama, kategori, stok, harga);
        alatDAO.insertAlat(alat);
    }

    public static void editAlat(AlatDAO alatDAO, KategoriDAO kategoriDAO) {
        boolean repeat = true;

        while (repeat) {
            System.out.print("Cari alat yang ingin diubah (ID atau Nama): ");
            String kataKunci = scanner.nextLine();
            List<Alat> hasilPencarian = alatDAO.searchAlat(kataKunci);

            if (hasilPencarian == null || hasilPencarian.isEmpty()) {
                System.out.println("Tidak ada alat yang ditemukan. Kembali ke menu.");
                return;
            }

            // Tampilkan hasil pencarian
            System.out.println("\nHasil pencarian:");
            for (Alat a : hasilPencarian) {
                System.out.println(a.getIdAlat() + " - " + a.getNamaAlat());
            }

            System.out.print("(C)ari Lagi ATAU (U)bah ID Alat dari data di atas: ");
            String pilihAksi = scanner.nextLine();

            if (pilihAksi.equalsIgnoreCase("C")) {
                continue;
            } else if (pilihAksi.equalsIgnoreCase("U")) {
                int idAlat;
                while (true) {
                    System.out.print("Masukkan ID Alat yang ingin diubah: ");
                    try {
                        idAlat = Integer.parseInt(scanner.nextLine());
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Input tidak valid. Masukkan angka.");
                    }
                }

                Alat alatLama = alatDAO.getAlatById(idAlat);
                if (alatLama == null) {
                    System.out.println("Alat tidak ditemukan.");
                    return;
                }

                System.out.print("Nama Baru (" + alatLama.getNamaAlat() + "): ");
                String nama = scanner.nextLine();
                if (!nama.isEmpty()) {
                    alatLama.setNamaAlat(nama);
                }

                System.out.print("Ingin ubah kategori? (Y/N): ");
                String ubahKategori = scanner.nextLine();
                if (ubahKategori.equalsIgnoreCase("Y")) {
                    Kategori kategoriBaru = pilihKategori(kategoriDAO);
                    if (kategoriBaru != null) {
                        alatLama.setKategori(kategoriBaru);
                    }
                }

                System.out.print("Stok Baru (" + alatLama.getStok() + "): ");
                String stokInput = scanner.nextLine();
                if (!stokInput.isEmpty()) {
                    try {
                        alatLama.setStok(Integer.parseInt(stokInput));
                    } catch (NumberFormatException e) {
                        System.out.println("Input stok tidak valid, tidak diubah.");
                    }
                }

                System.out.print("Harga per Hari Baru (" + alatLama.getHargaPerHari() + "): ");
                String hargaInput = scanner.nextLine();
                if (!hargaInput.isEmpty()) {
                    try {
                        alatLama.setHargaPerHari(Integer.parseInt(hargaInput));
                    } catch (NumberFormatException e) {
                        System.out.println("Input harga tidak valid, tidak diubah.");
                    }
                }

                alatDAO.updateAlat(alatLama);
                return;

            } else if (pilihAksi.isEmpty()) {
                System.out.println("Kembali ke menu utama.");
                return;
            } else {
                System.out.println("Pilihan tidak dikenali. Kembali ke menu utama.");
                return;
            }
        }
    }

    public static void hapusAlat(AlatDAO alatDAO) {
        System.out.print("Masukkan ID alat yang ingin dihapus: ");
        int id = Integer.parseInt(scanner.nextLine());
        alatDAO.deleteAlat(id);
    }

    public static Kategori pilihKategori(KategoriDAO kategoriDAO) {
        List<Kategori> daftarKategori = kategoriDAO.getAllKategori();

        if (daftarKategori.isEmpty()) {
            System.out.println("Belum ada kategori tersedia. Silakan tambah kategori terlebih dahulu.");
            return null;
        }

        System.out.println("Daftar Kategori:");
        for (Kategori k : daftarKategori) {
            System.out.println(k.getIdKategori() + ". " + k.getNamaKategori());
        }

        while (true) {
            System.out.print("Pilih ID kategori: ");
            String input = scanner.nextLine();

            try {
                int idKategori = Integer.parseInt(input);

                for (Kategori k : daftarKategori) {
                    if (k.getIdKategori() == idKategori) {
                        return k; // Ketemu kategori sesuai input
                    }
                }

                System.out.println("Kategori dengan ID tersebut tidak ditemukan. Silakan coba lagi.");

            } catch (NumberFormatException e) {
                System.out.println("Input tidak valid, masukkan angka.");
            }
        }
    }
}