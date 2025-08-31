package DataAccessLayer;

import DTO.PendingReportsDTO;
import DTO.TransportDTO;
import DataAccessLayer.util.Database;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class JdbcTransportDAO implements TransportDAO{

    @Override
    public List<TransportDTO> findAll() throws SQLException {
        String sql = "SELECT Transport_ID, Date, Departure_Time, Source, Destination, weight FROM TransportsCompleted";
        try (Statement st = Database.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            List<TransportDTO> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new TransportDTO(
                        rs.getInt("Transport_ID"),
                        rs.getString("Date"),
                        rs.getString("Departure_Time"),
                        rs.getString("Source"),
                        rs.getString("Destination"),
                        rs.getInt("weight")));
            }
            return list;
        }
    }

    @Override
    public TransportDTO insert(TransportDTO user) throws SQLException {
        String sql = """
                INSERT INTO TransportsCompleted
                (Transport_ID, Date, Departure_Time, Source, Destination, weight)
                VALUES (?, ?, ?, ?, ?, ?)
                 """;

        try (PreparedStatement ps = Database.getConnection()
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, user.TransportId());
            ps.setString(2, user.Date());
            ps.setString(3, user.Departure_Time());
            ps.setString(4, user.Source());
            ps.setString(5, user.Destination());
            ps.setInt(6, user.Weight());


            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int generatedId = keys.getInt(1);
                    // if you want to return the newly-created DTO with its generated ID
                    return new TransportDTO(
                            generatedId,
                            user.Date(),
                            user.Departure_Time(),
                            user.Source(),
                            user.Destination(),
                            user.Weight()
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error inserting Transport: " + e.getMessage());
        }
        return null; // or throw an exception if you prefer
    }
}
