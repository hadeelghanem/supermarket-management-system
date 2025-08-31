package DataAccessLayer;

import DTO.DriversAssignedDTO;

import DTO.WharehouseAssignedDTO;
import DataAccessLayer.util.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class JdbcDriversAssignedDAO implements DriversAssignedDAO{
    @Override
    public DriversAssignedDTO insert(DriversAssignedDTO driverassigned) throws SQLException {

        String sql = """
                INSERT INTO driverAssigned
                (employeeId, shiftId, shiftBranch)
                VALUES (?, ?, ?)
                 """;

        try (PreparedStatement ps = Database.getConnection()
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, driverassigned.employeeId());
            ps.setInt(2, driverassigned.shiftId());
            ps.setInt(3, driverassigned.shiftBranch());

            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                 // if you want to return the newly-created DTO with its generated ID
                    return new DriversAssignedDTO(
                            driverassigned.employeeId(),
                            driverassigned.shiftId(),
                            driverassigned.shiftBranch()
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error inserting report: " + e.getMessage());
        }
        return null; // or throw an exception if you prefer
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM driverAssigned WHERE employeeId = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error deleting driver assigned: " + e.getMessage());
        }

    }

    @Override
    public List<DriversAssignedDTO> findById(int shiftId, int shiftBranch) throws SQLException {

        String sql = "SELECT * FROM driverAssigned WHERE  shiftId = ? AND shiftBranch = ?";
        List<DriversAssignedDTO> list = new ArrayList<>();

        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            // bind your parameters!
            ps.setInt(1, shiftId);
            ps.setInt(2, shiftBranch);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new DriversAssignedDTO(
                            rs.getInt("employeeId"),
                            rs.getInt("shiftId"),
                            rs.getInt("shiftBranch")
                    ));
                }
            }
        }

        return list;

    }

}
