package pengembalian;

import java.sql.*;
// import java.util.ArrayList;
// import java.util.List;
import connection.DBConnection;
import penyewaan.Penyewaan;

public class PengembalianDAO {

    public Pengembalian getPengembalianByIdPenyewaan(int idPenyewaan) {
        String sql = "SELECT p.id_pengembalian, p.tgl_dikembalikan, p.status_bayar, " +
                    "pen.id_penyewaan, pen.tgl_sewa, pen.tgl_rencana_menyewa, pen.tgl_kembali, " +
                    "pen.barang_jaminan, pen.id_user " +
                    "FROM pengembalian p " +
                    "JOIN penyewaan pen ON p.id_penyewaan = pen.id_penyewaan " +
                    "WHERE p.id_penyewaan = ?";

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPenyewaan);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Buat objek Penyewaan
                Penyewaan penyewaan = new Penyewaan();
                penyewaan.setIdPenyewaan(rs.getInt("id_penyewaan"));
                penyewaan.setTglSewa(rs.getTimestamp("tgl_sewa"));
                penyewaan.setTglRencanaMenyewa(rs.getTimestamp("tgl_rencana_menyewa"));
                penyewaan.setTglKembali(rs.getTimestamp("tgl_kembali"));
                penyewaan.setBarangJaminan(rs.getString("barang_jaminan"));

                // Buat objek Pengembalian
                Pengembalian pengembalian = new Pengembalian();
                pengembalian.setIdPengembalian(rs.getInt("id_pengembalian"));
                pengembalian.setTglDikembalikan(rs.getTimestamp("tgl_dikembalikan"));
                pengembalian.setStatusBayar(rs.getString("status_bayar"));
                pengembalian.setPenyewaan(penyewaan); // set objek penyewaan ke dalam pengembalian

                return pengembalian;
            }

        } catch (SQLException e) {
            System.out.println("Gagal mengambil data pengembalian: " + e.getMessage());
        }

        return null;
    }

    // public List<Pengembalian> getAllPengembalianWithPenyewaan() {
    //     List<Pengembalian> list = new ArrayList<>();

    //     String sql = "SELECT p.id_pengembalian, p.id_penyewaan, p.tgl_dikembalikan, p.status_bayar, " +
    //                     "pen.tgl_sewa, pen.tgl_rencana_menyewa, pen.tgl_kembali, pen.id_user, pen.barang_jaminan " +
    //                     "FROM pengembalian p " +
    //                     "JOIN penyewaan pen ON p.id_penyewaan = pen.id_penyewaan";

    //     try (Connection conn = DBConnection.getConnection();
    //             PreparedStatement stmt = conn.prepareStatement(sql);
    //             ResultSet rs = stmt.executeQuery()) {

    //         while (rs.next()) {

    //             // Buat objek Penyewaan
    //             Penyewaan penyewaan = new Penyewaan();
    //             penyewaan.setIdPenyewaan(rs.getInt("id_penyewaan"));
    //             penyewaan.setTglSewa(rs.getTimestamp("tgl_sewa"));
    //             penyewaan.setTglRencanaMenyewa(rs.getTimestamp("tgl_rencana_menyewa"));
    //             penyewaan.setTglKembali(rs.getTimestamp("tgl_kembali"));
    //             penyewaan.setBarangJaminan(rs.getString("barang_jaminan"));

    //             // Buat objek Pengembalian
    //             Pengembalian pengembalian = new Pengembalian();
    //             pengembalian.setIdPengembalian(rs.getInt("id_pengembalian"));
    //             pengembalian.setTglDikembalikan(rs.getTimestamp("tgl_dikembalikan"));
    //             pengembalian.setStatusBayar(rs.getString("status_bayar"));
    //             pengembalian.setPenyewaan(penyewaan);

    //             list.add(pengembalian);
    //         }

    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }

    //     return list;
    // }

    public boolean insertPengembalian(Pengembalian pengembalian) {
        String sql = "INSERT INTO pengembalian (id_penyewaan, tgl_dikembalikan, status_bayar) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, pengembalian.getPenyewaan().getIdPenyewaan());
            stmt.setTimestamp(2, pengembalian.getTglDikembalikan());
            stmt.setString(3, pengembalian.getStatusBayar());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
