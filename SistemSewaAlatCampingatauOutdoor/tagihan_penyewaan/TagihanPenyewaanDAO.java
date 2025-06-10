package tagihan_penyewaan;

import java.sql.*;
import connection.DBConnection;

public class TagihanPenyewaanDAO {
    public TagihanPenyewaan getProcedure(int idPenyewaan) {
        TagihanPenyewaan tagihan = null;

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "{CALL hitung_total_sewa_denda(?)}";
            CallableStatement stmt = conn.prepareCall(sql);
            stmt.setInt(1, idPenyewaan);

            boolean hasResult = stmt.execute();

            if (hasResult) {
                try (ResultSet rs = stmt.getResultSet()) {
                    if (rs.next()) {
                        double totalSewa = rs.getDouble("total_harga_sewa");
                        double totalDenda = rs.getDouble("total_denda");
                        double totalTagihan = rs.getDouble("total_tagihan");

                        tagihan = new TagihanPenyewaan(totalSewa, totalDenda, totalTagihan);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Gagal mengambil total tagihan: " + e.getMessage());
            e.printStackTrace();
        }

        return tagihan;
    }

}
