package report;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connection.DBConnection;

public class ReportDAO {
  public List<PelangganSetia> getPelangganSetiaSubQuery() {
      List<PelangganSetia> list = new ArrayList<>();

      String sql = "SELECT u.id_user, u.nama, sewa_summary.total_penyewaan " +
                    "FROM user u " +
                    "JOIN ( " +
                    "    SELECT id_user, COUNT(*) AS total_penyewaan " +
                    "    FROM penyewaan " +
                    "    WHERE status = 'Disetujui' " +
                    "    GROUP BY id_user " +
                    ") AS sewa_summary ON u.id_user = sewa_summary.id_user " +
                    "WHERE u.role = 'pelanggan' " +
                    "ORDER BY sewa_summary.total_penyewaan DESC " +
                    "LIMIT 3";

      try (Connection conn = DBConnection.getConnection();
          PreparedStatement stmt = conn.prepareStatement(sql);
          ResultSet rs = stmt.executeQuery()) {

          while (rs.next()) {
              PelangganSetia pelanggan = new PelangganSetia(
                  rs.getInt("id_user"),
                  rs.getString("nama"),
                  rs.getInt("total_penyewaan")
              );
              list.add(pelanggan);
          }
      } catch (SQLException e) {
          e.printStackTrace();
      }

      return list;
  }

  public LaporanPendapatanBulanan getLaporanPendapatanBulananCTE() {
    String sql =
              "WITH total_sewa_per_penyewaan AS ( " +
              "  SELECT " +
              "    pw.id_penyewaan, " +
              "    MONTH(pw.tgl_sewa) AS bulan, " +
              "    SUM(dp.jumlah_barang * a.harga_perhari * GREATEST(DATEDIFF(pw.tgl_kembali, pw.tgl_rencana_menyewa), 1)) AS total_pendapatan " +
              "  FROM penyewaan pw " +
              "  JOIN detail_penyewaan dp ON pw.id_penyewaan = dp.id_penyewaan " +
              "  JOIN alat a ON dp.id_alat = a.id_alat " +
              "  WHERE pw.status = 'Disetujui' " +
              "  GROUP BY pw.id_penyewaan, MONTH(pw.tgl_sewa) " +
              "), " +
              "total_denda_per_penyewaan AS ( " +
              "  SELECT " +
              "    pw.id_penyewaan, " +
              "    SUM(d.nilai) AS total_denda " +
              "  FROM penyewaan pw " +
              "  LEFT JOIN pengembalian pg ON pw.id_penyewaan = pg.id_penyewaan " +
              "  LEFT JOIN denda d ON pg.id_pengembalian = d.id_pengembalian " +
              "  WHERE pw.status = 'Disetujui' " +
              "  GROUP BY pw.id_penyewaan " +
              "), " +
              "gabungan AS ( " +
              "  SELECT " +
              "    s.bulan, " +
              "    s.total_pendapatan, " +
              "    COALESCE(d.total_denda, 0) AS total_denda " +
              "  FROM total_sewa_per_penyewaan s " +
              "  LEFT JOIN total_denda_per_penyewaan d ON s.id_penyewaan = d.id_penyewaan " +
              ") " +
              "SELECT " +
              "  SUM(CASE WHEN bulan = 1 THEN total_pendapatan + total_denda ELSE 0 END) AS Januari, " +
              "  SUM(CASE WHEN bulan = 2 THEN total_pendapatan + total_denda ELSE 0 END) AS Februari, " +
              "  SUM(CASE WHEN bulan = 3 THEN total_pendapatan + total_denda ELSE 0 END) AS Maret, " +
              "  SUM(CASE WHEN bulan = 4 THEN total_pendapatan + total_denda ELSE 0 END) AS April, " +
              "  SUM(CASE WHEN bulan = 5 THEN total_pendapatan + total_denda ELSE 0 END) AS Mei, " +
              "  SUM(CASE WHEN bulan = 6 THEN total_pendapatan + total_denda ELSE 0 END) AS Juni, " +
              "  SUM(CASE WHEN bulan = 7 THEN total_pendapatan + total_denda ELSE 0 END) AS Juli, " +
              "  SUM(CASE WHEN bulan = 8 THEN total_pendapatan + total_denda ELSE 0 END) AS Agustus, " +
              "  SUM(CASE WHEN bulan = 9 THEN total_pendapatan + total_denda ELSE 0 END) AS September, " +
              "  SUM(CASE WHEN bulan = 10 THEN total_pendapatan + total_denda ELSE 0 END) AS Oktober, " +
              "  SUM(CASE WHEN bulan = 11 THEN total_pendapatan + total_denda ELSE 0 END) AS November, " +
              "  SUM(CASE WHEN bulan = 12 THEN total_pendapatan + total_denda ELSE 0 END) AS Desember " +
              "FROM gabungan";

    LaporanPendapatanBulanan laporanPendapatanBulanan = new LaporanPendapatanBulanan();

    try (Connection conn = DBConnection.getConnection();
          PreparedStatement stmt = conn.prepareStatement(sql);
          ResultSet rs = stmt.executeQuery()) {

        if (rs.next()) {
            for (int i = 0; i < 12; i++) {
                laporanPendapatanBulanan.setBulan(i, rs.getDouble(i + 1));
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return laporanPendapatanBulanan;
  }


  public List<LaporanDendaPerbulan> getLaporanDendaPerBulanCrosstab() {
    List<LaporanDendaPerbulan> laporanList = new ArrayList<>();

    String sql = 
    "SELECT u.nama AS pelanggan, " +
    "  SUM(CASE WHEN MONTH(pg.tgl_dikembalikan) = 1 THEN d.nilai ELSE 0 END) AS Jan, " +
    "  SUM(CASE WHEN MONTH(pg.tgl_dikembalikan) = 2 THEN d.nilai ELSE 0 END) AS Feb, " +
    "  SUM(CASE WHEN MONTH(pg.tgl_dikembalikan) = 3 THEN d.nilai ELSE 0 END) AS Mar, " +
    "  SUM(CASE WHEN MONTH(pg.tgl_dikembalikan) = 4 THEN d.nilai ELSE 0 END) AS Apr, " +
    "  SUM(CASE WHEN MONTH(pg.tgl_dikembalikan) = 5 THEN d.nilai ELSE 0 END) AS May, " +
    "  SUM(CASE WHEN MONTH(pg.tgl_dikembalikan) = 6 THEN d.nilai ELSE 0 END) AS Jun, " +
    "  SUM(CASE WHEN MONTH(pg.tgl_dikembalikan) = 7 THEN d.nilai ELSE 0 END) AS Jul, " +
    "  SUM(CASE WHEN MONTH(pg.tgl_dikembalikan) = 8 THEN d.nilai ELSE 0 END) AS Aug, " +
    "  SUM(CASE WHEN MONTH(pg.tgl_dikembalikan) = 9 THEN d.nilai ELSE 0 END) AS Sep, " +
    "  SUM(CASE WHEN MONTH(pg.tgl_dikembalikan) = 10 THEN d.nilai ELSE 0 END) AS Oct, " +
    "  SUM(CASE WHEN MONTH(pg.tgl_dikembalikan) = 11 THEN d.nilai ELSE 0 END) AS Nov, " +
    "  SUM(CASE WHEN MONTH(pg.tgl_dikembalikan) = 12 THEN d.nilai ELSE 0 END) AS `Dec` " +
    "FROM pengembalian pg " +
    "JOIN denda d ON pg.id_pengembalian = d.id_pengembalian " +
    "JOIN penyewaan p ON pg.id_penyewaan = p.id_penyewaan " +
    "JOIN user u ON p.id_user = u.id_user " +
    "GROUP BY u.nama " +
    "ORDER BY u.nama";


    try (Connection conn = DBConnection.getConnection();
          PreparedStatement stmt = conn.prepareStatement(sql);
          ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            LaporanDendaPerbulan laporan = new LaporanDendaPerbulan();
            laporan.setPelanggan(rs.getString("pelanggan"));

            for (int i = 0; i < 12; i++) {
                laporan.setDendaPerBulan(i, rs.getDouble(i + 2)); // kolom ke-2 adalah Jan, dst
            }

            laporanList.add(laporan);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return laporanList;
  }


}
