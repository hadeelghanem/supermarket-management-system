package DataAccessLayer;

import DTO.DriversAssignedDTO;
import DTO.EmployeeDTO;
import DTO.PendingReportsDTO;
import DTO.ShiftToRoleDTO;
import DataAccessLayer.util.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class JdbcShiftToRoleDAO implements ShiftToRoleDAO {


    @Override
    public ShiftToRoleDTO insert(ShiftToRoleDTO shift) throws SQLException {
        String sql = """
                INSERT INTO ShiftToRole
                (ShiftId, StoreBranchId, SHIFT_MANAGER, CASHIER, DELIVERY_MAN, CLEANING, WAREHOUSE_MAN, ORDEYLY)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                 """;

        try (PreparedStatement ps = Database.getConnection()
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, shift.ShiftId());
            ps.setInt(2, shift.StoreBranchId());
            ps.setBoolean(3, shift.SHIFT_MANAGER());
            ps.setBoolean(4, shift.CASHIER());
            ps.setBoolean(5, shift.DELIVERY_MAN());
            ps.setBoolean(6, shift.CLEANING());
            ps.setBoolean(7, shift.WAREHOUSE_MAN());
            ps.setBoolean(8, shift.ORDEYLY());

            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return new ShiftToRoleDTO(shift.ShiftId(),
                            shift.StoreBranchId(),
                            shift.SHIFT_MANAGER(),
                            shift.CASHIER(),
                            shift.DELIVERY_MAN(),
                            shift.CLEANING(),
                            shift.WAREHOUSE_MAN(),
                            shift.ORDEYLY());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error inserting shift: " + e.getMessage());
        }
        return null; // or throw an exception if you prefer


    }

    @Override
    public void delete(int shiftid , int shiftBranch) throws SQLException {
        String sql = "DELETE FROM ShiftToRole WHERE ShiftId = ? AND StoreBranchId = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, shiftid);
            ps.setInt(2, shiftBranch);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error deleting shift: " + e.getMessage());
        }
    }

    @Override
    public ShiftToRoleDTO findById(int shiftId, int shiftBranch) throws SQLException {

        String sql = "SELECT * FROM ShiftToRole WHERE  shiftId = ? AND StoreBranchId = ?";
        List<ShiftToRoleDTO> list = new ArrayList<>();

        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            // bind your parameters!
            ps.setInt(1, shiftId);
            ps.setInt(2, shiftBranch);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return (new ShiftToRoleDTO(
                           rs.getInt("ShiftId"),
                            rs.getInt("StoreBranchId"),
                            rs.getBoolean("SHIFT_MANAGER"),
                            rs.getBoolean("CASHIER"),
                            rs.getBoolean("DELIVERY_MAN"),
                            rs.getBoolean("CLEANING"),
                            rs.getBoolean("WAREHOUSE_MAN")
                            , rs.getBoolean("ORDEYLY")
                    ));
                }
            }
        }

        return null;

    }
    @Override
    public void updateCashier(int shiftid,int storeid, boolean value) throws SQLException {
        String sql = "UPDATE ShiftToRole SET CASHIER = ? WHERE shiftId = ? AND StoreBranchId = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setBoolean(1, value);
            ps.setInt(2, shiftid);
            ps.setInt(3, storeid);
            ps.executeUpdate();
        }
    }
    @Override
    public void updateDeliveryMan(int shiftid,int storeid, boolean value) throws SQLException {
        String sql = "UPDATE ShiftToRole SET DELIVERY_MAN = ? WHERE shiftId = ? AND StoreBranchId = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setBoolean(1, value);
            ps.setInt(2, shiftid);
            ps.setInt(3, storeid);
            ps.executeUpdate();
        }
    }
    @Override
    public void updateCleaning(int shiftid,int storeid, boolean value) throws SQLException {
        String sql = "UPDATE ShiftToRole SET CLEANING = ? WHERE shiftId = ? AND StoreBranchId = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setBoolean(1, value);
            ps.setInt(2, shiftid);
            ps.setInt(3, storeid);
            ps.executeUpdate();
        }
    }
    @Override
    public void updateSHIFT_MANAGER(int shiftid,int storeid, boolean value) throws SQLException {
        String sql = "UPDATE ShiftToRole SET SHIFT_MANAGER = ? WHERE shiftId = ? AND StoreBranchId = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setBoolean(1, value);
            ps.setInt(2, shiftid);
            ps.setInt(3, storeid);
            ps.executeUpdate();
        }
    }
    @Override
    public void updateWAREHOUSE_MAN(int shiftid,int storeid, boolean value) throws SQLException {
        String sql = "UPDATE ShiftToRole SET WAREHOUSE_MAN = ? WHERE shiftId = ? AND StoreBranchId = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setBoolean(1, value);
            ps.setInt(2, shiftid);
            ps.setInt(3, storeid);
            ps.executeUpdate();
        }
    }
    @Override
    public void updateORDEYLY(int shiftid,int storeid, boolean value) throws SQLException {
        String sql = "UPDATE ShiftToRole SET ORDEYLY = ? WHERE shiftId = ? AND StoreBranchId = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setBoolean(1, value);
            ps.setInt(2, shiftid);
            ps.setInt(3, storeid);
            ps.executeUpdate();
        }
    }
}
