package DataAccessLayer;

import DTO.HrManagerDTO;
import DTO.TruckDTO;
import DataAccessLayer.util.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcHrManagerDAO implements HrManagerDAO {
    @Override
    public HrManagerDTO insert(HrManagerDTO hrManager) throws SQLException {
        String sql = "INSERT INTO HrManager (id, pass) VALUES (?, ?)";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, hrManager.id());
            ps.setString(2, hrManager.pass());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int generatedId = keys.getInt(1);
                    return new HrManagerDTO(generatedId, hrManager.pass());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error inserting HrManager: " + e.getMessage());
        }
        return null; // or throw an exception if you prefer

    }




    @Override
    public HrManagerDTO findById(int id) throws SQLException {
        String sql = "SELECT id, pass FROM HrManager WHERE id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new HrManagerDTO(rs.getInt("id"), rs.getString("pass"));
                } else {
                    return null; // or throw an exception if you prefer
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error finding HrManager by ID: " + e.getMessage());
        }

    }
}
