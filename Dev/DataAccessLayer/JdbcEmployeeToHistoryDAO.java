package DataAccessLayer;

import DTO.EmployeeToAvailabilityDTO;
import DTO.EmployeeToHistoryDTO;
import DataAccessLayer.util.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcEmployeeToHistoryDAO implements EmployeeToHistoryDAO{
    @Override
    public EmployeeToHistoryDTO insert(EmployeeToHistoryDTO EmployeeToHistory) throws SQLException {
        String sql = """
                INSERT INTO EmployeeToHistory
                (shiftId, employeeID, role)
                VALUES (?, ?, ?)
                 """;

        try (PreparedStatement ps = Database.getConnection()
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, EmployeeToHistory.shiftId());
            ps.setInt(2, EmployeeToHistory.employeeID());
            ps.setString(3, EmployeeToHistory.role());


            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    // if you want to return the newly-created DTO with its generated ID
                    return new EmployeeToHistoryDTO(
                            EmployeeToHistory.shiftId(),
                            EmployeeToHistory.employeeID(),
                            EmployeeToHistory.role()
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error inserting employee: " + e.getMessage());
        }
        return null; // or throw an exception if you prefer

    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM EmployeeToHistory WHERE employeeID = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error deleting employee: " + e.getMessage());
        }

    }

    @Override
    public EmployeeToHistoryDTO findById(int id) throws SQLException {
        String sql = "SELECT * FROM EmployeeToHistory WHERE employeeID = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new EmployeeToHistoryDTO(
                            rs.getInt("shiftId"),
                            rs.getInt("employeeID"),
                            rs.getString("role")
                    );
                } else {
                    return null; // or throw an exception if you prefer
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error finding employee: " + e.getMessage());
        }
    }
}
