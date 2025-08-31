package DataAccessLayer;

import DTO.TruckDTO;
import DataAccessLayer.util.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class JdbcTruckDAO implements TruckDAO {
    @Override
    public List<TruckDTO> findAll() throws SQLException {
        String sql = "SELECT Id_number, Model, License_number, Net_Weight, Maximum_Weight FROM Truck";
        try (Statement st = Database.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            List<TruckDTO> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new TruckDTO(rs.getInt("Id_number"), rs.getString("Model"),
                        rs.getInt("License_number"), rs.getInt("Net_Weight"),
                        rs.getInt("Maximum_Weight")));
            }
            return list;
        }
    }


    @Override
    public TruckDTO insert(TruckDTO truck) throws SQLException {
        String sql = """
                INSERT INTO Truck
                (Id_number , Model, License_number, Net_Weight, Maximum_Weight)
                VALUES (?, ?, ?, ?, ?)
                 """;

        try (PreparedStatement ps = Database.getConnection()
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, truck.Id_number());
            ps.setString(2, truck.Model());
            ps.setInt(3, truck.License_number());
            ps.setInt(4, truck.Net_Weight());
            ps.setInt(5, truck.Maximum_Weight());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int generatedId = keys.getInt(1);
                    // if you want to return the newly-created DTO with its generated ID
                    return new TruckDTO(
                            generatedId,
                            truck.Model(),
                            truck.License_number(),
                            truck.Net_Weight(),
                            truck.Maximum_Weight()
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error inserting truck: " + e.getMessage());
        }
        return null; // or throw an exception if you prefer
    }



    @Override
    public void delete(int id) throws SQLException {

        String sql = "DELETE FROM Truck WHERE Id_number = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error deleting truck: " + e.getMessage());
        }

    }

}
