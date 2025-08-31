package DataAccessLayer;

import DTO.EmployeeDTO;
import DTO.EmployeeToAvailabilityDTO;
import DataAccessLayer.util.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcEmployeeToAvailabilityDAO implements EmployeeToAvailabilityDAO{
    @Override
    public EmployeeToAvailabilityDTO insert(EmployeeToAvailabilityDTO employeeToAvailability) throws SQLException {
        String sql = """
                INSERT INTO EmployeeToAvailability
                (id, dayOfWeek, morningShift, eveningShift)
                VALUES (?, ?, ?, ?)
                 """;

        try (PreparedStatement ps = Database.getConnection()
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, employeeToAvailability.id());
            ps.setInt(2, employeeToAvailability.dayOfWeek());
            ps.setBoolean(3, employeeToAvailability.morningShift());
            ps.setBoolean(4, employeeToAvailability.eveningShift());




            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int generatedId = keys.getInt(1);
                    // if you want to return the newly-created DTO with its generated ID
                    return new EmployeeToAvailabilityDTO(
                            generatedId,
                            employeeToAvailability.dayOfWeek(),
                            employeeToAvailability.morningShift(),
                            employeeToAvailability.eveningShift()
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

        String sql = "DELETE FROM EmployeeToAvailability WHERE id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error deleting employee availability: " + e.getMessage());
        }
    }

    @Override
    public EmployeeToAvailabilityDTO findById(int id) throws SQLException {
        String sql = "SELECT * FROM EmployeeToAvailability WHERE id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new EmployeeToAvailabilityDTO(
                            rs.getInt("id"),
                            rs.getInt("dayOfWeek"),
                            rs.getBoolean("morningShift"),
                            rs.getBoolean("eveningShift")
                    );
                } else {
                    return null; // or throw an exception if you prefer
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error finding employee availability: " + e.getMessage());
        }
    }
}
