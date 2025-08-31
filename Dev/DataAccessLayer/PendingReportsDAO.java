package DataAccessLayer;

import DTO.PendingReportsDTO;
import DTO.TruckDTO;

import java.sql.SQLException;
import java.util.List;

public interface PendingReportsDAO {
    List<PendingReportsDTO> findAll() throws SQLException;
    PendingReportsDTO insert(PendingReportsDTO report) throws SQLException;
    void delete(int id) throws SQLException;
}
