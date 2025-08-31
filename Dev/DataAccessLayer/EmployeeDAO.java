package DataAccessLayer;

import DTO.EmployeeDTO;
import DTO.PendingReportsDTO;

import java.sql.SQLException;
import java.util.List;

public interface EmployeeDAO {
    List<EmployeeDTO> findAll() throws SQLException;
    EmployeeDTO insert(EmployeeDTO employee) throws SQLException;
    void delete(int id) throws SQLException;
    EmployeeDTO findById(int id) throws SQLException;
    EmployeeDTO update(EmployeeDTO employee) throws SQLException;
}
