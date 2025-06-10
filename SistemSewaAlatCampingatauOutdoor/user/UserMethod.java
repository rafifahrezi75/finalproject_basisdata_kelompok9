package user;

import java.util.List;
import java.util.Scanner;

public class UserMethod {
  static Scanner scanner = new Scanner(System.in);

  public static void handleRegister(UserDAO userDAO) {
    System.out.print("Nama: ");
    String nama = scanner.nextLine();
    System.out.print("Password: ");
    String password = scanner.nextLine();
    String role = "pelanggan";
    System.out.print("No HP: ");
    String noHp = scanner.nextLine();
    System.out.print("Alamat: ");
    String alamat = scanner.nextLine();

    User newUser = new User(nama, password, role, noHp, alamat);
    userDAO.registerUser(newUser);
    System.out.println("Registrasi berhasil. Silakan login.\n");
  }

  public static User handleLogin(UserDAO userDAO) {
    System.out.print("Nama: ");
    String nama = scanner.nextLine();
    System.out.print("Password: ");
    String password = scanner.nextLine();

    User user = userDAO.loginUser(nama, password);
    if (user != null) {
      System.out.println("Login berhasil. Selamat datang, " + user.getNama() + " (" + user.getRole() + ")");
      return user;
    } else {
      System.out.println("Login gagal. Nama atau password salah.\n");
      return null;
    }
  }

  public static void handleMenuUser(UserDAO userDAO, User currentUser) {
    boolean userMenu = true;
    while (userMenu) {
      System.out.println("\n=== KELOLA USER ===");
      System.out.println("1. Lihat Semua User");
      System.out.println("2. Tambah User");
      System.out.println("3. Hapus User");
      System.out.println("4. Kembali");
      System.out.print("Pilih: ");
      String pilihan = scanner.nextLine();

      switch (pilihan) {
        case "1" -> tampilkanSemuaUser(userDAO);
        case "2" -> tambahUser(userDAO);
        case "3" -> hapusUser(userDAO, currentUser);
        case "4" -> userMenu = false;
        default -> System.out.println("Pilihan tidak valid.");
      }
    }
  }

  public static void tampilkanSemuaUser(UserDAO userDAO) {
    List<User> users = userDAO.getAllUsers();

    if (users.isEmpty()) {
      System.out.println("Tidak ada user yang terdaftar.");
      return;
    }

    System.out.println("\n=== DAFTAR SEMUA USER ===");
    System.out.printf("%-5s %-15s %-10s %-12s %-20s%n", "ID", "Nama", "Role", "No HP", "Alamat");
    System.out.println("---------------------------------------------------------------------");

    for (User user : users) {
      System.out.printf(
        "%-5d %-15s %-10s %-12s %-20s%n",
        user.getIdUser(),
        user.getNama(),
        user.getRole(),
        user.getNoHp(),
        user.getAlamat()
      );
    }
  }

  public static void tambahUser(UserDAO userDAO) {
    System.out.println("\n=== Tambah User Baru ===");
    System.out.print("Nama: ");
    String nama = scanner.nextLine();
    System.out.print("Password: ");
    String password = scanner.nextLine();
    System.out.print("Role (admin/pelanggan): ");
    String role = scanner.nextLine();
    System.out.print("No HP: ");
    String noHp = scanner.nextLine();
    System.out.print("Alamat: ");
    String alamat = scanner.nextLine();

    User newUser = new User(nama, password, role, noHp, alamat);
    userDAO.registerUser(newUser);
    System.out.println("User baru berhasil ditambahkan.");
  }

  public static void hapusUser(UserDAO userDAO, User currentUser) {
    System.out.print("Masukkan ID user yang akan dihapus: ");
    try {
      int id = Integer.parseInt(scanner.nextLine());

      if (id == currentUser.getIdUser()) {
        System.out.println("Anda tidak dapat menghapus akun Anda sendiri.");
        return;
      }

      userDAO.deleteUser(id);
    } catch (NumberFormatException e) {
      System.out.println("Input tidak valid. Harus berupa angka.");
    }
  }
}