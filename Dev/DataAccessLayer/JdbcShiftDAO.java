package DataAccessLayer;

import DTO.ShiftDTO;
import DataAccessLayer.util.Database;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class JdbcShiftDAO implements ShiftDAO{
    @Override
    public ShiftDTO insert(ShiftDTO Shift) throws SQLException {
        String sql = """
                INSERT INTO Shift
                (ShiftId, storeId, date, ShiftType, ShiftStatus)
                VALUES (?, ?, ?, ?, ?)
                 """;

        try (PreparedStatement ps = Database.getConnection()
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, Shift.ShiftId());
            ps.setInt(2, Shift.storeId());
            ps.setString(3, Shift.date());
            ps.setString(4, Shift.ShiftType());
            ps.setString(5, Shift.ShiftStatus());




            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    // if you want to return the newly-created DTO with its generated ID
                    return new ShiftDTO(
                            keys.getInt(1), // Assuming ShiftId is the first column in the table
                            Shift.storeId(),
                            Shift.date(),
                            Shift.ShiftType(),
                            Shift.ShiftStatus()
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error inserting Shift: " + e.getMessage());
        }
        return null; // or throw an exception if you prefer

    }

    @Override
    public void delete(int id) throws SQLException {

        String sql = "DELETE FROM Shift WHERE ShiftId = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error deleting Shift: " + e.getMessage());
        }

    }

    @Override
    public ShiftDTO findById(int id) throws SQLException {

        String sql = "SELECT * FROM Shift WHERE ShiftId = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new ShiftDTO(
                            rs.getInt("ShiftId"),
                            rs.getInt("storeId"),
                            rs.getString("date"),
                            rs.getString("ShiftType"),
                            rs.getString("ShiftStatus")
                    );
                } else {
                    return null; // or throw an exception if you prefer
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error finding Shift by ID: " + e.getMessage());
        }
    }

    @Override
    public List<ShiftDTO> findAll(int storeid) throws SQLException {
        String sql = "SELECT * FROM Shift WHERE storeId = ?";
        List<ShiftDTO> shifts = new ArrayList<>();
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, storeid);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    shifts.add(new ShiftDTO(
                            rs.getInt("ShiftId"),
                            rs.getInt("storeId"),
                            rs.getString("date"),
                            rs.getString("ShiftType"),
                            rs.getString("ShiftStatus")
                    ));
                }
                return shifts; // Return the list of shifts
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error finding Shift by ID: " + e.getMessage());
        }
    }

}
