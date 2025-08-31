package DataAccessLayer;

import DTO.DriverDTO;
import DTO.PendingReportsDTO;
import DataAccessLayer.util.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class JdbcDriverDAO implements DriverDAO{

    @Override
    public List<DriverDTO> findAll() throws SQLException {
        String sql = "SELECT id, name, Driving_License, isAvailable FROM Driver";
        try (Statement st = Database.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            List<DriverDTO> list = new ArrayList<>();
            while (rs.next()) {
                list.add(
                        new DriverDTO(
                                rs.getInt("id"),
                                rs.getString("name"),
                                rs.getInt("Driving_License"),
                                rs.getBoolean("isAvailable")
                        ));
            }
            return list;
        }
    }

    @Override
    public DriverDTO insert(DriverDTO driver) throws SQLException {
        String sql = """
                INSERT INTO Driver
                (id, name, Driving_License, isAvailable)
                VALUES (?, ?, ?, ?)
                 """;

        try (PreparedStatement ps = Database.getConnection()
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, driver.id());
            ps.setString(2, driver.name());
            ps.setInt(3, driver.Driving_License());
            ps.setBoolean(4, driver.isAvailable());



            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int generatedId = keys.getInt(1);
                    // if you want to return the newly-created DTO with its generated ID
                    return new DriverDTO(
                            generatedId,
                            driver.name(),
                            driver.Driving_License(),
                            driver.isAvailable()
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
        String sql = "DELETE FROM Driver WHERE id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error deleting driver: " + e.getMessage());
        }

    }
}
