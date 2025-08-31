package DataAccessLayer;

import DTO.DriversAssignedDTO;
import DTO.EmployeeToHistoryDTO;

import java.sql.SQLException;

public interface EmployeeToHistoryDAO {
    EmployeeToHistoryDTO insert(EmployeeToHistoryDTO EmployeeToHistory) throws SQLException;
    void delete(int id) throws SQLException;
    EmployeeToHistoryDTO findById(int id) throws SQLException;
}
