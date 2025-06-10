package alat;

import java.sql.*;
import java.util.*;
import connection.DBConnection;
import kategori.Kategori;
import kategori.KategoriDAO;

public class AlatDAO {
  private final KategoriDAO kategoriDAO = new KategoriDAO();

  public List<Alat> getAllAlat() {
    List<Alat> list = new ArrayList<>();
    String sql = "SELECT * FROM alat";

    try (Connection conn = DBConnection.getConnection();
          Statement stmt = conn.createStatement();
          ResultSet rs = stmt.executeQuery(sql)) {

      while (rs.next()) {
        Kategori kategori = kategoriDAO.getKategoriById(rs.getInt("id_kategori"));
        Alat alat = new Alat(
          rs.getInt("id_alat"),
          rs.getString("nama_alat"),
          kategori,
          rs.getInt("stok"),
          rs.getInt("harga_perhari")
        );
        list.add(alat);
      }

    } catch (SQLException e) {
      System.out.println("Gagal mengambil data alat: " + e.getMessage());
    }

    return list;
  }

  public List<AlatTersediaView> getAllAlatReady() {
    List<AlatTersediaView> list = new ArrayList<>();
    String sql = "SELECT * FROM alat WHERE stok > 0";

    try (Connection conn = DBConnection.getConnection();
          Statement stmt = conn.createStatement();
          ResultSet rs = stmt.executeQuery(sql)) {

        while (rs.next()) {
            Kategori kategori = kategoriDAO.getKategoriById(rs.getInt("id_kategori"));
            AlatTersediaView alat = new AlatTersediaView(
                rs.getInt("id_alat"),
                rs.getString("nama_alat"),
                kategori,
                rs.getInt("stok"),
                rs.getInt("harga_perhari")
            );
            list.add(alat);
        }

    } catch (SQLException e) {
        System.out.println("Gagal mengambil data alat: " + e.getMessage());
    }

    return list;
  }

  public Alat getAlatById(int idAlat) {
    String sql = """
        SELECT a.*, k.nama_kategori FROM alat a 
        JOIN kategori k ON a.id_kategori = k.id_kategori 
        WHERE a.id_alat = ?
    """;

    try (Connection conn = DBConnection.getConnection();
          PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setInt(1, idAlat);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        Kategori kategori = new Kategori(
          rs.getInt("id_kategori"),
          rs.getString("nama_kategori")
        );

        return new Alat(
          rs.getInt("id_alat"),
          rs.getString("nama_alat"),
          kategori,
          rs.getInt("stok"),
          rs.getInt("harga_perhari")
        );
      }

    } catch (SQLException e) {
      System.out.println("Gagal mengambil data alat berdasarkan ID: " + e.getMessage());
    }

    return null;
  }

  public void insertAlat(Alat alat) {
    String sql = "INSERT INTO alat (nama_alat, id_kategori, stok, harga_perhari) VALUES (?, ?, ?, ?)";

    try (Connection conn = DBConnection.getConnection();
          PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setString(1, alat.getNamaAlat());
      stmt.setInt(2, alat.getKategori().getIdKategori());
      stmt.setInt(3, alat.getStok());
      stmt.setInt(4, alat.getHargaPerHari());

      stmt.executeUpdate();
      System.out.println("Alat berhasil ditambahkan.");

    } catch (SQLException e) {
      System.out.println("Gagal menambahkan alat: " + e.getMessage());
    }
  }

  public List<Alat> searchAlat(String kataKunci) {
    List<Alat> list = new ArrayList<>();
    String sql = """
        SELECT a.*, k.nama_kategori FROM alat a 
        JOIN kategori k ON a.id_kategori = k.id_kategori 
        WHERE a.id_alat LIKE ? OR a.nama_alat LIKE ?
    """;

    try (Connection conn = DBConnection.getConnection();
          PreparedStatement stmt = conn.prepareStatement(sql)) {

      String like = "%" + kataKunci + "%";
      stmt.setString(1, like);
      stmt.setString(2, like);

      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        Kategori kategori = new Kategori(
          rs.getInt("id_kategori"),
          rs.getString("nama_kategori")
        );

        Alat alat = new Alat(
          rs.getInt("id_alat"),
          rs.getString("nama_alat"),
          kategori,
          rs.getInt("stok"),
          rs.getInt("harga_perhari")
        );
        list.add(alat);
      }

    } catch (SQLException e) {
      System.out.println("Gagal mencari alat: " + e.getMessage());
    }

    return list;
  }

  public void updateAlat(Alat alat) {
    String sql = """
        UPDATE alat SET nama_alat = ?, id_kategori = ?, stok = ?, harga_perhari = ?
        WHERE id_alat = ?
    """;

    try (Connection conn = DBConnection.getConnection();
          PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setString(1, alat.getNamaAlat());
      stmt.setInt(2, alat.getKategori().getIdKategori());
      stmt.setInt(3, alat.getStok());
      stmt.setInt(4, alat.getHargaPerHari());
      stmt.setInt(5, alat.getIdAlat());

      int rowsAffected = stmt.executeUpdate();
      if (rowsAffected > 0) {
        System.out.println("Alat berhasil diperbarui.");
      } else {
        System.out.println("Alat tidak ditemukan.");
      }

    } catch (SQLException e) {
      System.out.println("Gagal memperbarui alat: " + e.getMessage());
    }
  }

  public void updateStok(Alat alat) {
    String sql = "UPDATE alat SET stok = ? WHERE id_alat = ?";

    try (Connection conn = DBConnection.getConnection();
          PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setInt(1, alat.getStok());
      stmt.setInt(2, alat.getIdAlat());

      stmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Gagal memperbarui stok alat: " + e.getMessage());
    }
  }

  public void deleteAlat(int idAlat) {
    String sql = "DELETE FROM alat WHERE id_alat = ?";

    try (Connection conn = DBConnection.getConnection();
          PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setInt(1, idAlat);
      stmt.executeUpdate();

      System.out.println("Alat berhasil dihapus.");
    } catch (SQLException e) {
      System.out.println("Gagal menghapus alat: " + e.getMessage());
    }
  }
}