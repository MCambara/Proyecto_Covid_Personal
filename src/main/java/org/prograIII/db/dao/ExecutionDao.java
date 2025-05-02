package org.prograIII.db.dao;

import org.prograIII.db.dabaBaseConnection.DatabaseConnection;
import org.prograIII.db.model.ExecutionModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExecutionDao {

    private static final Logger logger = LogManager.getLogger(ExecutionDao.class);

    public boolean save(ExecutionModel execution) {
        String sql = "INSERT INTO executed_reports (execution_date, country_iso) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, execution.getExecutionDate());
            stmt.setString(2, execution.getCountryIso());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("[ERROR] Error inserting execution: {} -> {}", execution, e.getMessage());
            return false;
        }
    }

    public List<ExecutionModel> getAll() {
        List<ExecutionModel> executions = new ArrayList<>();
        String sql = "SELECT * FROM executed_reports";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ExecutionModel execution = new ExecutionModel(
                        rs.getString("execution_date"),
                        rs.getString("country_iso")
                );
                executions.add(execution);
            }
        } catch (SQLException e) {
            logger.error("[ERROR] Error fetching executions: {}", e.getMessage());
        }
        return executions;
    }
}