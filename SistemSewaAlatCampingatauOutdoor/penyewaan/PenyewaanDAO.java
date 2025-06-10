package penyewaan;

import java.sql.*;
import java.util.ArrayList;
import connection.DBConnection;
import java.util.List;
import user.User;

public class PenyewaanDAO {

  public List<Penyewaan> getAllPenyewaan() {
    List<Penyewaan> list = new ArrayList<>();
    String sql = "SELECT p.id_penyewaan, p.tgl_sewa, p.tgl_rencana_menyewa, p.tgl_kembali, p.barang_jaminan, " +
              "u.id_user, u.nama, u.password, u.role, u.no_hp, u.alamat, p.status, " +
              "CASE WHEN pg.id_pengembalian IS NOT NULL THEN 'Selesai' ELSE 'Belum_Selesai' END AS statussewa " +
              "FROM penyewaan p " +
              "JOIN user u ON p.id_user = u.id_user " +
              "LEFT JOIN pengembalian pg ON p.id_penyewaan = pg.id_penyewaan";

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

        Penyewaan p = new Penyewaan(
          rs.getInt("id_penyewaan"),
          user,
          rs.getTimestamp("tgl_sewa"),
          rs.getTimestamp("tgl_rencana_menyewa"),
          rs.getTimestamp("tgl_kembali"),
          rs.getString("barang_jaminan"),
          rs.getString("status")
        );
        p.setStatusSewa(rs.getString("statussewa"));
        list.add(p);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return list;
  }

  public List<Penyewaan> getAllPenyewaanBelumSelesai() {
    List<Penyewaan> list = new ArrayList<>();
    String sql = "SELECT * FROM ( " +
                  "SELECT p.id_penyewaan, p.tgl_sewa, p.tgl_rencana_menyewa, p.tgl_kembali, p.barang_jaminan, " +
                  "u.id_user, u.nama, u.password, u.role, u.no_hp, u.alamat, p.status AS status_penyewaan, " +
                  "CASE WHEN pg.id_pengembalian IS NOT NULL THEN 'Selesai' ELSE 'Belum_Selesai' END AS statussewa " +
                  "FROM penyewaan p " +
                  "JOIN user u ON p.id_user = u.id_user " +
                  "LEFT JOIN pengembalian pg ON p.id_penyewaan = pg.id_penyewaan " +
              ") AS subquery " +
              "WHERE statussewa = 'Belum_Selesai' AND status_penyewaan = 'Disetujui'";

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

        Penyewaan p = new Penyewaan(
          rs.getInt("id_penyewaan"),
          user,
          rs.getTimestamp("tgl_sewa"),
          rs.getTimestamp("tgl_rencana_menyewa"),
          rs.getTimestamp("tgl_kembali"),
          rs.getString("barang_jaminan"),
          rs.getString("status_penyewaan")
        );
        p.setStatusSewa(rs.getString("statussewa"));
        list.add(p);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return list;
  }

  public List<Penyewaan> getAllPenyewaansSetujui() {
    List<Penyewaan> list = new ArrayList<>();
    String sql = "SELECT * FROM ( " +
                  "SELECT p.id_penyewaan, p.tgl_sewa, p.tgl_rencana_menyewa, p.tgl_kembali, p.barang_jaminan, " +
                  "u.id_user, u.nama, u.password, u.role, u.no_hp, u.alamat, p.status AS status_penyewaan, " +
                  "CASE WHEN pg.id_pengembalian IS NOT NULL THEN 'Selesai' ELSE 'Belum_Selesai' END AS statussewa " +
                  "FROM penyewaan p " +
                  "JOIN user u ON p.id_user = u.id_user " +
                  "LEFT JOIN pengembalian pg ON p.id_penyewaan = pg.id_penyewaan " +
              ") AS subquery " +
              "WHERE statussewa = 'Belum_Selesai' AND status_penyewaan = 'Belum_Disetujui'";

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

        Penyewaan p = new Penyewaan(
          rs.getInt("id_penyewaan"),
          user,
          rs.getTimestamp("tgl_sewa"),
          rs.getTimestamp("tgl_rencana_menyewa"),
          rs.getTimestamp("tgl_kembali"),
          rs.getString("barang_jaminan"),
          rs.getString("status_penyewaan")
        );
        p.setStatusSewa(rs.getString("statussewa"));
        list.add(p);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return list;
  }

  public Penyewaan getPenyewaanById(int id) {
    Penyewaan penyewaan = null;
    String sql = "SELECT p.*, u.* FROM penyewaan p JOIN user u ON p.id_user = u.id_user WHERE p.id_penyewaan = ?";

    try (Connection conn = DBConnection.getConnection();
          PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            User user = new User(
                rs.getInt("id_user"),
                rs.getString("nama"),
                rs.getString("password"),
                rs.getString("role"),
                rs.getString("no_hp"),
                rs.getString("alamat")
            );

            // SET OBJEKNYA DI SINI
            penyewaan = new Penyewaan(
                rs.getInt("id_penyewaan"),
                user,
                rs.getTimestamp("tgl_sewa"),
                rs.getTimestamp("tgl_rencana_menyewa"),
                rs.getTimestamp("tgl_kembali"),
                rs.getString("barang_jaminan"),
                rs.getString("status")
            );
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return penyewaan;
  }

  public List<Penyewaan> getPenyewaanByUserId(int userId) {
    List<Penyewaan> list = new ArrayList<>();
    String sql = "SELECT p.id_penyewaan, p.tgl_sewa, p.tgl_rencana_menyewa, p.tgl_kembali, p.barang_jaminan, " +
                  "u.id_user, u.nama, u.password, u.role, u.no_hp, u.alamat, p.status, " +
                  "CASE WHEN pg.id_pengembalian IS NOT NULL THEN 'Selesai' ELSE 'Belum_Selesai' END AS statussewa " +
                  "FROM penyewaan p " +
                  "JOIN user u ON p.id_user = u.id_user " +
                  "LEFT JOIN pengembalian pg ON p.id_penyewaan = pg.id_penyewaan " +
                  "WHERE p.id_user = ?";

    try (Connection conn = DBConnection.getConnection();
          PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, userId);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            User user = new User(
                rs.getInt("id_user"),
                rs.getString("nama"),
                rs.getString("password"),
                rs.getString("role"),
                rs.getString("no_hp"),
                rs.getString("alamat")
            );

            Penyewaan p = new Penyewaan(
                rs.getInt("id_penyewaan"),
                user,
                rs.getTimestamp("tgl_sewa"),
                rs.getTimestamp("tgl_rencana_menyewa"),
                rs.getTimestamp("tgl_kembali"),
                rs.getString("barang_jaminan"),
                rs.getString("status")
            );
            p.setStatusSewa(rs.getString("statussewa"));
            list.add(p);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return list;
}

  public int insertPenyewaan(Penyewaan penyewaan) {
    String sql = "INSERT INTO penyewaan (id_user, tgl_sewa, tgl_rencana_menyewa, tgl_kembali, barang_jaminan, status) " +
                  "VALUES (?, ?, ?, ?, ?, ?)";

    try (Connection conn = DBConnection.getConnection();
          PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

        stmt.setInt(1, penyewaan.getUser().getIdUser());
        stmt.setTimestamp(2, new java.sql.Timestamp(penyewaan.getTglSewa().getTime()));
        stmt.setTimestamp(3, new java.sql.Timestamp(penyewaan.getTglRencanaMenyewa().getTime()));
        stmt.setTimestamp(4, penyewaan.getTglKembali() != null ? new java.sql.Timestamp(penyewaan.getTglKembali().getTime()) : null);
        stmt.setString(5, penyewaan.getBarangJaminan());
        stmt.setString(6, "Belum_Disetujui"); // Nilai default untuk status

        stmt.executeUpdate();

        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                int generatedId = generatedKeys.getInt(1);
                penyewaan.setIdPenyewaan(generatedId);
                return generatedId;
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return -1; // Indikasi gagal
  }

  public boolean setujuiPenyewaan(int idPenyewaan) {
      String sql = "UPDATE penyewaan SET status = ? WHERE id_penyewaan = ?";

      try (Connection conn = DBConnection.getConnection();
          PreparedStatement stmt = conn.prepareStatement(sql)) {

          stmt.setString(1, "Disetujui");
          stmt.setInt(2, idPenyewaan);

          int rowsUpdated = stmt.executeUpdate();
          return rowsUpdated > 0;

      } catch (SQLException e) {
          System.out.println("Gagal menyetujui penyewaan: " + e.getMessage());
          return false;
      }
  }

}
