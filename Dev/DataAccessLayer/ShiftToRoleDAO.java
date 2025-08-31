package DataAccessLayer;

import DTO.DriversAssignedDTO;
import DTO.EmployeeDTO;
import DTO.ShiftToRoleDTO;
import DTO.TransportDTO;

import java.sql.SQLException;
import java.util.List;

public interface ShiftToRoleDAO {
    ShiftToRoleDTO insert(ShiftToRoleDTO shift) throws SQLException;
    void delete(int shiftid , int shiftBranch) throws SQLException;
    ShiftToRoleDTO findById(int shiftId, int shiftBranch) throws SQLException;

    public void updateCashier(int shiftid,int storeid, boolean value) throws SQLException ;
    public void updateDeliveryMan(int shiftid,int storeid, boolean value) throws SQLException ;
    public void updateCleaning(int shiftid,int storeid, boolean value) throws SQLException ;
    public void updateSHIFT_MANAGER(int shiftid,int storeid, boolean value) throws SQLException ;
    public void updateWAREHOUSE_MAN(int shiftid,int storeid, boolean value) throws SQLException ;
    public void updateORDEYLY(int shiftid,int storeid, boolean value) throws SQLException ;



    }
