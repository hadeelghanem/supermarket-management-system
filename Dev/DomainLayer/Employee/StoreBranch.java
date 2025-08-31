package DomainLayer.Employee;

import DTO.ShiftDTO;
import DataAccessLayer.JdbcEmployeeToShiftScheduledDAO;
import DataAccessLayer.JdbcShiftDAO;
import DataAccessLayer.ShiftDAO;
import DomainLayer.Shipment.Area;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StoreBranch {
    private int id;
    private String name;
    private String address;
    private int managerEmployeeID;
    private StoreStatus status;

    private List<Shift> shiftScheduled;
    private List<Shift> shiftHistory;
    private List<Shift> cancelledShifts;

    ////new merged from site
    private String Tel;
    private String Contact_name;
    private Area area;
    private JdbcShiftDAO jdbcShiftDAO;
    private JdbcEmployeeToShiftScheduledDAO jdbcEmployeeToShiftScheduledDAO;

    public StoreBranch(int id, String name, String address, StoreStatus status ,String tel, String contact_name, Area area) {
        this.id = id;
        this.name = name;
        this.address = address;
        //this.managerEmployeeID = managerEmployeeID;
        this.status = status;
        this.shiftScheduled = new ArrayList<>();
        this.shiftHistory = new ArrayList<>();
        this.cancelledShifts = new ArrayList<>();
        Tel = tel;
        Contact_name = contact_name;
        this.area = area;
        this.jdbcShiftDAO = new JdbcShiftDAO();
        this.jdbcEmployeeToShiftScheduledDAO = new JdbcEmployeeToShiftScheduledDAO();
    }
    public String getAddress() {
        return address;
    }

    public Area getArea() {
        return area;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public int getManagerEmployeeID() {
        return managerEmployeeID;
    }
    public StoreStatus getStatus() {
        return status;
    }
    public List<Shift> getShiftScheduled() {
        return shiftScheduled;
    }
    public List<Shift> getShiftHistory() {
        return shiftHistory;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setManagerEmployeeID(int managerEmployeeID) {
        this.managerEmployeeID = managerEmployeeID;
    }
    public void setStatus(StoreStatus status) {
        this.status = status;
    }
    public void addShiftScheduled(Shift shift) {
        this.shiftScheduled.add(shift);
    }
    public void removeShiftScheduled(Shift shift) {
        this.shiftScheduled.remove(shift);
        this.shiftHistory.add(shift);

    }
    public void removeEmployeeFromShift(Shift shift, int employeeID) {

        //removeEmployeeRequired from shift
        shift.removeEmployeeRequired(employeeID);
    }
    public boolean addEmployeeToShift(Shift shift, int employeeID, Role role) {
        //addEmployeeRequired to shift
        if(shift.getEmployeesRequired().containsKey(employeeID)) {
            return false;
        }
        return shift.addEmployeeRequired(role, employeeID);
    }
    public void addRoleToShift(Shift shift, Role role) {
        //addRole to shift
        shift.addNeededRole(role);
    }

    public void removeRoleFromShift(Shift shift, Role role) {
        //removeRole from shift
        shift.removeNeededRole(role);
    }



    public void cancelShiftScheduled(Shift shift) {
        shift.setStatus(ShiftStatus.CANCELLED);
        shiftScheduled.remove(shift);
        cancelledShifts.add(shift);
    }

    public void newWeek() {
        for (Shift shift : shiftScheduled) {
            if (shift.getStatus() == ShiftStatus.SCHEDULED) {
                shift.setStatus(ShiftStatus.DONE);
            }
        }
        // Move all shifts from shiftScheduled to shiftHistory
        shiftHistory.addAll(shiftScheduled);
        shiftScheduled.clear();
    }
    public StoreBranch getbyname(String name) {
        if (this.name.equals(name)) {
            return this;
        }
        return null;
    }
    public void LoadShiftsFromDB() {
        try {
            List<ShiftDTO> shifts = jdbcShiftDAO.findAll(this.id);
            for (ShiftDTO shiftDTO : shifts) {
                Shift shift = new Shift(shiftDTO.ShiftId(),shiftDTO.storeId(),
                        LocalDate.parse(shiftDTO.date()),
                        ShiftType.valueOf(shiftDTO.ShiftType()),
                        ShiftStatus.valueOf(shiftDTO.ShiftStatus()));
                shift.LoadRoles();
                shift.LoadEmployees();
                this.shiftScheduled.add(shift);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }



}
