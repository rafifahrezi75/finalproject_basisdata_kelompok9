package detail_penyewaan;

import alat.Alat;
import connection.DBConnection;
import penyewaan.Penyewaan;
import penyewaan.PenyewaanDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DetailPenyewaanDAO {

  public List<DetailPenyewaan> getDetailByIdPenyewaan(int idPenyewaan) {
    List<DetailPenyewaan> list = new ArrayList<>();

    String sql = "SELECT dp.id_detail_penyewaan, dp.jumlah_barang, " +
                "a.id_alat, a.nama_alat, a.harga_perhari " +
                "FROM detail_penyewaan dp " +
                "JOIN alat a ON dp.id_alat = a.id_alat " +
                "WHERE dp.id_penyewaan = ?";

    try (Connection conn = DBConnection.getConnection();
          PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setInt(1, idPenyewaan);
      ResultSet rs = stmt.executeQuery();

      // Ambil objek penyewaan hanya sekali (asumsi valid ID)
      PenyewaanDAO penyewaanDAO = new PenyewaanDAO();
      Penyewaan penyewaan = penyewaanDAO.getPenyewaanById(idPenyewaan);

      if (penyewaan == null) {
        System.out.println("ID penyewaan tidak ditemukan.");
        return list;
      }

      while (rs.next()) {
        Alat alat = new Alat(
          rs.getInt("id_alat"),
          rs.getString("nama_alat"),
          rs.getInt("harga_perhari")
        );

        DetailPenyewaan detail = new DetailPenyewaan(
          rs.getInt("id_detail_penyewaan"),
          penyewaan,
          alat,
          rs.getInt("jumlah_barang")
        );

        list.add(detail);
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return list;
  }

  public void insertDetailPenyewaan(DetailPenyewaan detail) {
    String sql = "INSERT INTO detail_penyewaan (id_penyewaan, id_alat, jumlah_barang) VALUES (?, ?, ?)";

    try (Connection conn = DBConnection.getConnection();
          PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setInt(1, detail.getPenyewaan().getIdPenyewaan());
      stmt.setInt(2, detail.getAlat().getIdAlat());
      stmt.setInt(3, detail.getJumlahBarang());

      stmt.executeUpdate();

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

}