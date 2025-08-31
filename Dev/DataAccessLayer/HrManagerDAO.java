package DataAccessLayer;

import DTO.HrManagerDTO;

import java.sql.SQLException;

public interface HrManagerDAO {
    HrManagerDTO insert(HrManagerDTO hrManager) throws SQLException;
    HrManagerDTO findById(int id) throws SQLException;
}
