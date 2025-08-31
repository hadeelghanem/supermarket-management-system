package DataAccessLayer;

import DTO.EmployeeToShiftScheduledDTO;
import DTO.ShiftDTO;

import java.sql.SQLException;
import java.util.List;

public interface ShiftDAO {
    ShiftDTO insert(ShiftDTO Shift) throws SQLException;
    void delete(int id) throws SQLException;
    ShiftDTO findById(int id) throws SQLException;


    List<ShiftDTO> findAll(int storeid) throws SQLException;
}
