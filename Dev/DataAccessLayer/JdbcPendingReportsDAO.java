package DataAccessLayer;

import DTO.PendingReportsDTO;
import DTO.TruckDTO;
import DataAccessLayer.util.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class JdbcPendingReportsDAO implements PendingReportsDAO{

    @Override
    public List<PendingReportsDTO> findAll() throws SQLException {
        String sql = "SELECT destination, weight, productsForDes, ReportId FROM PendingReports";
        try (Statement st = Database.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            List<PendingReportsDTO> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new PendingReportsDTO(rs.getString("destination"), rs.getInt("weight"),
                                rs.getString("productsForDes"), rs.getInt("ReportId")));
            }
            return list;
        }
    }

    @Override
    public PendingReportsDTO insert(PendingReportsDTO report) throws SQLException {
        String sql = """
                INSERT INTO PendingReports
                (destination, weight, productsForDes, ReportId)
                VALUES (?, ?, ?, ?)
                 """;

        try (PreparedStatement ps = Database.getConnection()
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, report.destination());
            ps.setInt(2, report.weight());
            ps.setString(3, report.productsForDes());
            ps.setInt(4, report.ReportId());


            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {

                    // if you want to return the newly-created DTO with its generated ID
                    return new PendingReportsDTO(
                            report.destination(),
                            report.weight(),
                            report.productsForDes(),
                            report.ReportId()
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
        String sql = "DELETE FROM PendingReports WHERE ReportId = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error deleting report: " + e.getMessage());
        }

    }
}
