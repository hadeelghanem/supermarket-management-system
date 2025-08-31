package DataAccessLayer;

import DTO.DriversAssignedDTO;

import java.sql.SQLException;
import java.util.List;

public interface DriversAssignedDAO {

    DriversAssignedDTO insert(DriversAssignedDTO driverassigned) throws SQLException;
    void delete(int id) throws SQLException;
    List<DriversAssignedDTO> findById(int shiftId, int shiftBranch) throws SQLException;
}
