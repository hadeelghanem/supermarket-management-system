package DataAccessLayer;

import DTO.BankAccountDTO;
import DTO.DriverDTO;
import DataAccessLayer.util.Database;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcBankAccountDAO implements BankAccountDAO{
    @Override
    public BankAccountDTO insert(BankAccountDTO bank) throws SQLException {
        String sql = """
                INSERT INTO BankAccount
                (bankName, branchNumber, accountNumber, accountOwnerName, employeeId)
                VALUES (?, ?, ?, ?, ?)
                 """;

        try (PreparedStatement ps = Database.getConnection()
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, bank.bankName());
            ps.setInt(2, bank.branchNumber());
            ps.setInt(3, bank.accountNumber());
            ps.setString(4, bank.accountOwnerName());
            ps.setInt(5, bank.employeeId());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int generatedId = keys.getInt(5);

                    // if you want to return the newly-created DTO with its generated ID
                    return new BankAccountDTO(
                            bank.bankName(),
                            bank.branchNumber(),
                            bank.accountNumber(),
                            bank.accountOwnerName(),
                            generatedId
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
        String sql = "DELETE FROM BankAccount WHERE id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error deleting bank account: " + e.getMessage());
        }

    }

    @Override
    public BankAccountDTO findById(int id) throws SQLException {
        String sql = "SELECT * FROM BankAccount WHERE id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new BankAccountDTO(
                        rs.getString("bankName"),
                        rs.getInt("branchNumber"),
                        rs.getInt("accountNumber"),
                        rs.getString("accountOwnerName"),
                        rs.getInt("employeeId")
                );
            } else {
                return null; // or throw an exception if you prefer
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error finding bank account by ID: " + e.getMessage());
        }
    }
}
