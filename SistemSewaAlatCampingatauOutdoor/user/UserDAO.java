package user;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import connection.DBConnection;

public class UserDAO {

  // Register User
  public void registerUser(User user) {
    String sql = "INSERT INTO user (nama, password, role, no_hp, alamat) VALUES (?, ?, ?, ?, ?)";
    try (Connection conn = DBConnection.getConnection();
      PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, user.getNama());
      stmt.setString(2, user.getPassword());
      stmt.setString(3, user.getRole());
      stmt.setString(4, user.getNoHp());
      stmt.setString(5, user.getAlamat());
      stmt.executeUpdate();
      System.out.println("Registrasi berhasil.\n");
    } catch (SQLException e) {
      System.out.println("Gagal registrasi.\n");
      e.printStackTrace();
    }
  }

  // Login User
  public User loginUser(String nama, String password) {
    String sql = "SELECT * FROM user WHERE nama = ? AND password = ?";
    try (Connection conn = DBConnection.getConnection();
      PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, nama);
      stmt.setString(2, password);

      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return new User(
            rs.getInt("id_user"),
            rs.getString("nama"),
            rs.getString("password"),
            rs.getString("role"),
            rs.getString("no_hp"),
            rs.getString("alamat")
          );
        }
      }
    } catch (SQLException e) {
      System.out.println("Login gagal.\n");
      e.printStackTrace();
    }
    return null;
  }

  // Update User
  public void updateUser(User user) {
    String sql = "UPDATE user SET nama = ?, password = ?, role = ?, no_hp = ?, alamat = ? WHERE id_user = ?";
    try (Connection conn = DBConnection.getConnection();
      PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, user.getNama());
      stmt.setString(2, user.getPassword());
      stmt.setString(3, user.getRole());
      stmt.setString(4, user.getNoHp());
      stmt.setString(5, user.getAlamat());
      stmt.setInt(6, user.getIdUser());
      stmt.executeUpdate();
      System.out.println("User berhasil diperbarui.\n");
    } catch (SQLException e) {
      System.out.println("Gagal memperbarui user.\n");
      e.printStackTrace();
    }
  }

  // Delete User
  public void deleteUser(int idUser) {
    String sql = "DELETE FROM user WHERE id_user = ?";
    try (Connection conn = DBConnection.getConnection();
      PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, idUser);
      stmt.executeUpdate();
      System.out.println("User berhasil dihapus.\n");
    } catch (SQLException e) {
      System.out.println("Gagal menghapus user.\n");
      e.printStackTrace();
    }
  }

  // Optional: Tampilkan semua user (untuk admin atau debugging)
  public List<User> getAllUsers() {
    List<User> users = new ArrayList<>();
    String sql = "SELECT * FROM user";
    try (Connection conn = DBConnection.getConnection();
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery(sql)) {
      while (rs.next()) {
        User user = new User(
          rs.getInt("id_user"),
          rs.getString("nama"),
          rs.getString("password"),
          rs.getString("role"),
          rs.getString("no_hp"),
          rs.getString("alamat")
        );
        users.add(user);
      }
    } catch (SQLException e) {
      System.out.println("Gagal mengambil data user.\n");
      e.printStackTrace();
    }
    return users;
  }
}
