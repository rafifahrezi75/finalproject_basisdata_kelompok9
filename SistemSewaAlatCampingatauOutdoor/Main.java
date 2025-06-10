import java.util.Scanner;
import user.User;
import user.UserDAO;
import user.UserMethod;
import kategori.KategoriDAO;
import kategori.KategoriMethod;
import alat.AlatDAO;
import alat.AlatMethod;
import penyewaan.PenyewaanDAO;
import penyewaan.PenyewaanMethod;
import detail_penyewaan.DetailPenyewaanDAO;
import detail_penyewaan.DetailPenyewaanMethod;
import pengembalian.PengembalianDAO;
import pengembalian.PengembalianMethod;
import report.ReportDAO;
import report.ReportMethod;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static UserDAO userDAO = new UserDAO();
    static UserMethod userMethod = new UserMethod();

    static KategoriDAO kategoriDAO = new KategoriDAO();
    static KategoriMethod kategoriMethod = new KategoriMethod();

    static AlatDAO alatDAO = new AlatDAO();
    static AlatMethod alatMethod = new AlatMethod();

    static PenyewaanDAO penyewaanDAO = new PenyewaanDAO();
    static PenyewaanMethod penyewaanMethod = new PenyewaanMethod();

    static DetailPenyewaanDAO detailPenyewaanDAO = new DetailPenyewaanDAO();
    static DetailPenyewaanMethod detailPenyewaanMethod = new DetailPenyewaanMethod();

    static PengembalianDAO pengembalianDAO = new PengembalianDAO();
    static PengembalianMethod pengembalianMethod = new PengembalianMethod();

    static ReportDAO reportDAO = new ReportDAO();
    static ReportMethod reportMethod = new ReportMethod();

    public static void main(String[] args) {
        User currentUser = null;
        int attempts = 0;
        final int MAX_ATTEMPTS = 3;

        while (currentUser == null && attempts < MAX_ATTEMPTS) {
            System.out.println("=== SELAMAT DATANG DI SISTEM SEWA ALAT CAMPING DAN OUTDOOR ===");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.print("Pilih menu (1/2): ");
            String pilihan = scanner.nextLine();

            switch (pilihan) {
                case "1" -> {
                    currentUser = userMethod.handleLogin(userDAO);
                    if (currentUser == null) {
                        attempts++;
                        if (attempts >= MAX_ATTEMPTS) {
                            System.out.println("Terlalu banyak percobaan login gagal. Program dihentikan.");
                            System.exit(0);
                        }
                    }
                }
                case "2" -> userMethod.handleRegister(userDAO);
                default -> System.out.println("Pilihan tidak valid.");
            }
        }

        // Masuk ke menu sesuai role
        switch (currentUser.getRole().toLowerCase()) {
            case "admin" -> handleAdminMenu(currentUser);
            case "pelanggan" -> handlePelangganMenu(currentUser);
            default -> System.out.println("Role tidak dikenal.");
        }
    }

    private static void handleAdminMenu(User user) {
        boolean running = true;
        while (running) {
            System.out.println("\n=== MENU ADMIN ===");
            System.out.println("1. User");
            System.out.println("2. Kategori");
            System.out.println("3. Alat");
            System.out.println("4. Penyewaan");
            System.out.println("5. Setujui");
            System.out.println("6. Pengembalian");
            System.out.println("7. Report");
            System.out.println("8. Logout");
            System.out.print("Pilih: ");
            String pilih = scanner.nextLine();

            switch (pilih) {
                case "1" -> userMethod.handleMenuUser(userDAO, user);
                case "2" -> kategoriMethod.handleMenuKategori(kategoriDAO);
                case "3" -> alatMethod.handleMenuAlat(alatDAO, kategoriDAO);
                case "4" -> penyewaanMethod.handleMenuPenyewaan(penyewaanDAO, userDAO);
                case "5" -> penyewaanMethod.setujui(penyewaanDAO);
                case "6" -> pengembalianMethod.handleMenuPengembalian(pengembalianDAO, penyewaanDAO);
                case "7" -> reportMethod.handleMenuReport(reportDAO);
                case "8" -> {
                    System.out.println("Logout berhasil.\n");
                    running = false;
                    main(null); // kembali ke menu login
                }
                default -> System.out.println("Pilihan tidak valid.");
            }
        }
    }

    private static void handlePelangganMenu(User user) {
        boolean running = true;
        while (running) {
            System.out.println("\n=== MENU PELANGGAN ===");
            System.out.println("1. Sewa Alat");
            System.out.println("2. Lihat Histori Sewa");
            System.out.println("3. Logout");
            System.out.print("Pilih: ");
            String pilih = scanner.nextLine();

            switch (pilih) {
                case "1" -> penyewaanMethod.sewaAlat(penyewaanDAO, detailPenyewaanDAO, alatDAO, user);
                case "2" -> penyewaanMethod.handleMenuPenyewaanPelanggan(penyewaanDAO, user);
                case "3" -> {
                    System.out.println("Logout berhasil.\n");
                    running = false;
                    main(null); // kembali ke login
                }
                default -> System.out.println("Pilihan tidak valid.");
            }
        }
    }
}