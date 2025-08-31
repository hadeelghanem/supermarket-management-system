package DataAccessLayer;

import DTO.EmployeeToRoleDTO;
import DTO.EmployeeToShiftScheduledDTO;

import java.sql.SQLException;
import java.util.List;

public interface EmployeeToShiftScheduledDAO {
    EmployeeToShiftScheduledDTO insert(EmployeeToShiftScheduledDTO EmployeeToRole) throws SQLException;
    void delete(int id) throws SQLException;
    List<EmployeeToShiftScheduledDTO> findById(int id) throws SQLException;
}
