package DataAccessLayer;

import DTO.EmployeeToHistoryDTO;
import DTO.EmployeeToShiftScheduledDTO;
import DataAccessLayer.util.Database;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class JdbcEmployeeToShiftScheduledDAO implements EmployeeToShiftScheduledDAO {
    public EmployeeToShiftScheduledDTO insert(EmployeeToShiftScheduledDTO EmployeeToShiftScheduled) throws SQLException {
        String sql = """
                INSERT INTO EmployeeToShiftScheduled
                (shiftId, employeeID, role)
                VALUES (?, ?, ?)
                 """;

        try (PreparedStatement ps = Database.getConnection()
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, EmployeeToShiftScheduled.shiftId());
            ps.setInt(2, EmployeeToShiftScheduled.employeeID());
            ps.setString(3, EmployeeToShiftScheduled.role());


            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    // if you want to return the newly-created DTO with its generated ID
                    return new EmployeeToShiftScheduledDTO(
                            EmployeeToShiftScheduled.shiftId(),
                            EmployeeToShiftScheduled.employeeID(),
                            EmployeeToShiftScheduled.role()
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
        String sql = "DELETE FROM EmployeeToShiftScheduled WHERE shiftId = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error deleting employee: " + e.getMessage());
        }

    }

    @Override
    public List<EmployeeToShiftScheduledDTO> findById(int id) throws SQLException {
        String sql = "SELECT * FROM EmployeeToShiftScheduled WHERE shiftId = ?";
        List<EmployeeToShiftScheduledDTO> list = new ArrayList<>();
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()){
                  list.add( new EmployeeToShiftScheduledDTO(
                            rs.getInt("shiftId"),
                            rs.getInt("employeeID"),
                            rs.getString("role")
                  ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error finding employee: " + e.getMessage());
        }
    return list;
    }


}
