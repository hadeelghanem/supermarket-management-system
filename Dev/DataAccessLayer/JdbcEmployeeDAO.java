package DataAccessLayer;

import DTO.EmployeeDTO;
import DataAccessLayer.util.Database;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class JdbcEmployeeDAO implements EmployeeDAO{
    @Override
    public List<EmployeeDTO> findAll() throws SQLException {
        String sql = "SELECT id, name, storeBranchId, bankAccountName, bankAccountNumber, salary, isHRManager, employeeTerms, licenseNumber FROM Employee";
        try (Statement st = Database.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            List<EmployeeDTO> list = new ArrayList<>();
            while (rs.next()) {
                list.add(
                        new EmployeeDTO(
                                rs.getInt("id"),
                                rs.getString("name"),
                                rs.getInt("storeBranchId"),
                                rs.getString("bankAccountName"),
                                rs.getInt("bankAccountNumber"),
                                rs.getDouble("salary"),
                                rs.getBoolean("isHRManager"),
                                rs.getString("employeeTerms"),
                                rs.getInt("licenseNumber")
                        ));
            }
            return list;
        }


    }

    @Override
    public EmployeeDTO insert(EmployeeDTO employee) throws SQLException {
        String sql = """
                INSERT INTO Employee
                (id, name, storeBranchId, bankAccountName, bankAccountNumber, salary, isHRManager, employeeTerms, licenseNumber)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                 """;

        try (PreparedStatement ps = Database.getConnection()
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, employee.id());
            ps.setString(2, employee.name());
            ps.setInt(3, employee.storeBranchId());
            ps.setString(4, employee.bankAccountName());
            ps.setInt(5, employee.bankAccountNumber());
            ps.setDouble(6, employee.salary());
            ps.setBoolean(7, employee.isHRManager());
            ps.setString(8, employee.employeeTerms());
            ps.setInt(9, employee.licenseNumber());



            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int generatedId = keys.getInt(1);
                    // if you want to return the newly-created DTO with its generated ID
                    return new EmployeeDTO(
                            generatedId,
                            employee.name(),
                            employee.storeBranchId(),
                            employee.bankAccountName(),
                            employee.bankAccountNumber(),
                            employee.salary(),
                            employee.isHRManager(),
                            employee.employeeTerms(),
                            employee.licenseNumber()
                    );
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
        String sql = "DELETE FROM Employee WHERE id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error deleting employee: " + e.getMessage());
        }

    }

    @Override
    public EmployeeDTO findById(int id) throws SQLException {
        String sql = "SELECT id, name, storeBranchId, bankAccountName, bankAccountNumber, salary, isHRManager, employeeTerms, licenseNumber FROM Employee WHERE id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new EmployeeDTO(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getInt("storeBranchId"),
                            rs.getString("bankAccountName"),
                            rs.getInt("bankAccountNumber"),
                            rs.getDouble("salary"),
                            rs.getBoolean("isHRManager"),
                            rs.getString("employeeTerms"),
                            rs.getInt("licenseNumber")

                    );
                } else {
                    return null; // or throw an exception if you prefer
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error finding employee by ID: " + e.getMessage());
        }
    }

    @Override
    public EmployeeDTO update(EmployeeDTO employee) throws SQLException {

        String sql = """
                UPDATE Employee
                SET  name = ?, storeBranchId = ?, bankAccountName = ?, bankAccountNumber = ?, salary = ?, isHRManager = ?, employeeTerms = ?, licenseNumber = ?
                WHERE id = ?
                """;

        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, employee.name());
            ps.setInt(2, employee.storeBranchId());
            ps.setString(3, employee.bankAccountName());
            ps.setInt(4, employee.bankAccountNumber());
            ps.setDouble(5, employee.salary());
            ps.setBoolean(6, employee.isHRManager());
            ps.setString(7, employee.employeeTerms());
            ps.setInt(8, employee.licenseNumber());
            ps.setInt(9, employee.id());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                return employee; // Return the updated DTO
            } else {
                return null; // or throw an exception if you prefer
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error updating employee: " + e.getMessage());
        }
    }
}
