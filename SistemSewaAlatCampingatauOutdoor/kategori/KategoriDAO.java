package kategori;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import connection.DBConnection;

public class KategoriDAO {

    public void insertKategori(Kategori kategori) {
      String sql = "INSERT INTO kategori (nama_kategori) VALUES (?)";
      try (Connection conn = DBConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, kategori.getNamaKategori());
        stmt.executeUpdate();
        System.out.println("Kategori berhasil ditambahkan.");
      } catch (SQLException e) {
        System.out.println("Gagal menambahkan kategori.");
        e.printStackTrace();
      }
    }

    public List<Kategori> getAllKategori() {
      List<Kategori> list = new ArrayList<>();
      String sql = "SELECT * FROM kategori";

      try (Connection conn = DBConnection.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {

        while (rs.next()) {
          Kategori kategori = new Kategori(
            rs.getInt("id_kategori"),
            rs.getString("nama_kategori")
          );
          list.add(kategori);
        }
      } catch (SQLException e) {
        System.out.println("Gagal mengambil data kategori.");
        e.printStackTrace();
      }

      return list;
    }

    public List<Kategori> searchKategori(String kataKunci) {
      List<Kategori> list = new ArrayList<>();
      String sql = "SELECT * FROM kategori WHERE id_kategori = ? OR nama_kategori LIKE ?";

      try (Connection conn = DBConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, kataKunci);
        stmt.setString(2, "%" + kataKunci + "%");

        try (ResultSet rs = stmt.executeQuery()) {
          while (rs.next()) {
            Kategori kategori = new Kategori(
              rs.getInt("id_kategori"),
              rs.getString("nama_kategori")
            );
            list.add(kategori);
          }
        }
      } catch (SQLException e) {
          System.out.println("Gagal mencari kategori.");
          e.printStackTrace();
      }

      return list;
    }

    public void updateKategori(Kategori kategori) {
    String sql = "UPDATE kategori SET nama_kategori=? WHERE id_kategori=?";
    try (Connection conn = DBConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, kategori.getNamaKategori());
        stmt.setInt(2, kategori.getIdKategori());
        stmt.executeUpdate();
        System.out.println("Kategori berhasil diperbarui.");
    } catch (SQLException e) {
        e.printStackTrace();
    }
  }


    public void deleteKategori(int idKategori) {
      String sql = "DELETE FROM kategori WHERE id_kategori=?";
      try (Connection conn = DBConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, idKategori);
        stmt.executeUpdate();
        System.out.println("Kategori berhasil dihapus.");
      } catch (SQLException e) {
        System.out.println("Gagal menghapus kategori.");
        e.printStackTrace();
      }
    }

    public Kategori getKategoriById(int id) {
      String sql = "SELECT * FROM kategori WHERE id_kategori = ?";
      try (Connection conn = DBConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, id);
        try (ResultSet rs = pstmt.executeQuery()) {
          if (rs.next()) {
            return new Kategori(
              rs.getInt("id_kategori"),
              rs.getString("nama_kategori")
            );
          }
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
      return null; // jika tidak ditemukan
    }
}
