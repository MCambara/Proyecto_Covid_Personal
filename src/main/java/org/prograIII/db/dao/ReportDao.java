package org.prograIII.db.dao;

import org.prograIII.db.dabaBaseConnection.DatabaseConnection;
import org.prograIII.db.model.ReportModel;
import org.springframework.stereotype.Repository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Repository
public class ReportDao {

    private static final Logger logger = LogManager.getLogger(ReportDao.class);

    public boolean save(ReportModel report) {
        String sql = "INSERT INTO covid_reports (date, confirmed, deaths, recovered, iso, region_name, province) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, report.getDate());
            stmt.setInt(2, report.getConfirmed());
            stmt.setInt(3, report.getDeaths());
            stmt.setInt(4, report.getRecovered());
            stmt.setString(5, report.getIso());
            stmt.setString(6, report.getRegionName());
            stmt.setString(7, report.getProvince());

            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            logger.error("[ERROR] Error inserting report: {} -> {}", report, e.getMessage());
            return false;
        }
    }

    public TreeMap<String, ReportModel> findByDateAndIso(String date, String iso) {
        TreeMap<String, ReportModel> reportsMap = new TreeMap<>();
        String sql = "SELECT * FROM covid_reports WHERE date = ? AND iso = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, date);
            stmt.setString(2, iso);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ReportModel report = new ReportModel(
                        rs.getInt("id"),
                        rs.getString("date"),
                        rs.getInt("confirmed"),
                        rs.getInt("deaths"),
                        rs.getInt("recovered"),
                        rs.getString("iso"),
                        rs.getString("region_name"),
                        rs.getString("province")
                );

                reportsMap.put(report.getProvince(), report);
            }
        } catch (Exception e) {
            logger.error("[ERROR] Error fetching reports for date={} and iso={}: {}", date, iso, e.getMessage());
        }

        return reportsMap;
    }

    public boolean exists(ReportModel report) {
        String sql = "SELECT COUNT(*) FROM covid_reports WHERE date = ? AND confirmed = ? AND deaths = ? AND recovered = ? AND iso = ? AND region_name = ? AND province = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, report.getDate());
            stmt.setInt(2, report.getConfirmed());
            stmt.setInt(3, report.getDeaths());
            stmt.setInt(4, report.getRecovered());
            stmt.setString(5, report.getIso());
            stmt.setString(6, report.getRegionName());
            stmt.setString(7, report.getProvince());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            logger.error("[ERROR] Error checking report existence: {}", e.getMessage());
        }
        return false;
    }

}
