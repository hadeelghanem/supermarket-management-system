package DataAccessLayer;

import DTO.PendingReportsDTO;
import DTO.TransportDTO;

import java.sql.SQLException;
import java.util.List;

public interface TransportDAO {
    List<TransportDTO> findAll() throws SQLException;
    TransportDTO insert(TransportDTO user) throws SQLException;

}
