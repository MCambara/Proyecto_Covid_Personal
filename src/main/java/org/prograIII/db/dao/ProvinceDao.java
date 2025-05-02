package org.prograIII.db.dao;

import org.prograIII.db.dabaBaseConnection.DatabaseConnection;
import org.prograIII.db.model.ProvinceModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProvinceDao {

    private static final Logger logger = LogManager.getLogger(ProvinceDao.class);

    public boolean save(ProvinceModel province) {
        String sql = "INSERT INTO provinces (iso, province, name, lat, lng) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, province.getIso());
            stmt.setString(2, province.getProvince());
            stmt.setString(3, province.getName());
            stmt.setDouble(4, province.getLat());
            stmt.setDouble(5, province.getLng());

            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            logger.error("[ERROR] Error inserting province: {} -> {}", province, e.getMessage());
            return false;
        }
    }

    public boolean exists(ProvinceModel province) {
        String sql = "SELECT COUNT(*) FROM provinces WHERE iso = ? AND province = ? AND name = ? AND lat = ? AND lng = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, province.getIso());
            stmt.setString(2, province.getProvince());
            stmt.setString(3, province.getName());
            stmt.setDouble(4, province.getLat());
            stmt.setDouble(5, province.getLng());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            logger.error("[ERROR] Error checking province existence: {}", e.getMessage());
        }
        return false;
    }


}
