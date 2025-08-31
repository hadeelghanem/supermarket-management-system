package DataAccessLayer;

import DTO.EmployeeToRoleDTO;
import DataAccessLayer.util.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcEmployeeToRoleDAO implements EmployeeToRoleDAO{
    @Override
    public EmployeeToRoleDTO insert(EmployeeToRoleDTO EmployeeToRole) throws SQLException {

        String sql = """
                INSERT INTO EmployeeToRole
                (ID, HR_MANAGER, SHIFT_MANAGER, CASHIER, DELIVERY_MAN, CLEANING, WAREHOUSE_MAN, ORDEYLY)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                 """;

        try (PreparedStatement ps = Database.getConnection()
                    .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, EmployeeToRole.ID());
                    ps.setBoolean(2, EmployeeToRole.HR_MANAGER());
                    ps.setBoolean(3, EmployeeToRole.SHIFT_MANAGER());
                    ps.setBoolean(4, EmployeeToRole.CASHIER());
                    ps.setBoolean(5,EmployeeToRole.DELIVERY_MAN());
                    ps.setBoolean(6, EmployeeToRole.CLEANING());
                    ps.setBoolean(7, EmployeeToRole.WAREHOUSE_MAN());
                    ps.setBoolean(8, EmployeeToRole.ORDEYLY());


            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    // if you want to return the newly-created DTO with its generated ID
                    return new EmployeeToRoleDTO(
                            keys.getInt(1), // Assuming ID is auto-generated
                            EmployeeToRole.HR_MANAGER(),
                            EmployeeToRole.SHIFT_MANAGER(),
                            EmployeeToRole.CASHIER(),
                            EmployeeToRole.DELIVERY_MAN(),
                            EmployeeToRole.CLEANING(),
                            EmployeeToRole.WAREHOUSE_MAN(),
                            EmployeeToRole.ORDEYLY());
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
        String sql = "DELETE FROM EmployeeToRole WHERE ID = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error deleting employee: " + e.getMessage());
        }

    }

    @Override
    public EmployeeToRoleDTO findById(int id) throws SQLException {
        String sql = "SELECT * FROM EmployeeToRole WHERE ID = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new EmployeeToRoleDTO(
                            rs.getInt("ID"),
                            rs.getBoolean("HR_MANAGER"),
                            rs.getBoolean("SHIFT_MANAGER"),
                            rs.getBoolean("CASHIER"),
                            rs.getBoolean("DELIVERY_MAN"),
                            rs.getBoolean("CLEANING"),
                            rs.getBoolean("WAREHOUSE_MAN"),
                            rs.getBoolean("ORDEYLY")
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
