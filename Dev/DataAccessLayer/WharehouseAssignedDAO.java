package DataAccessLayer;

import DTO.WharehouseAssignedDTO;

import java.sql.SQLException;
import java.util.List;

public interface WharehouseAssignedDAO {
    WharehouseAssignedDTO insert(WharehouseAssignedDTO WharehouseAssigned) throws SQLException;
    void delete(int id) throws SQLException;
    List<WharehouseAssignedDTO> findById(int shiftId, int shiftBranch) throws SQLException;
}
