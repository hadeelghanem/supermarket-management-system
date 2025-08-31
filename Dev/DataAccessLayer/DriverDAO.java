package DataAccessLayer;

import DTO.DriverDTO;
import DTO.PendingReportsDTO;

import java.sql.SQLException;
import java.util.List;

public interface DriverDAO {
    List<DriverDTO> findAll() throws SQLException;
    DriverDTO insert(DriverDTO driver) throws SQLException;
    void delete(int id) throws SQLException;
}
