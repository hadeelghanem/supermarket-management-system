package DataAccessLayer;

import DTO.DriversAssignedDTO;
import DTO.EmployeeToAvailabilityDTO;

import java.sql.SQLException;

public interface EmployeeToAvailabilityDAO {
    EmployeeToAvailabilityDTO insert(EmployeeToAvailabilityDTO employeeToAvailability) throws SQLException;
    void delete(int id) throws SQLException;
    EmployeeToAvailabilityDTO findById(int id) throws SQLException;
}
