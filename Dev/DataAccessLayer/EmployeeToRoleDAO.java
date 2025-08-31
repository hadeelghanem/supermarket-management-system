package DataAccessLayer;

import DTO.EmployeeToHistoryDTO;
import DTO.EmployeeToRoleDTO;

import java.sql.SQLException;

public interface EmployeeToRoleDAO {
    EmployeeToRoleDTO insert(EmployeeToRoleDTO EmployeeToRole) throws SQLException;
    void delete(int id) throws SQLException;
    EmployeeToRoleDTO findById(int id) throws SQLException;
}
