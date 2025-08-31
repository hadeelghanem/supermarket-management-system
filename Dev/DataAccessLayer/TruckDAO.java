package DataAccessLayer;

import DTO.TruckDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface TruckDAO {
    List<TruckDTO> findAll() throws SQLException;
    TruckDTO insert(TruckDTO truck) throws SQLException;
    void delete(int id) throws SQLException;
}
