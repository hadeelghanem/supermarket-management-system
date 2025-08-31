package DomainLayer.Employee;

import DTO.DriversAssignedDTO;
import DTO.EmployeeToShiftScheduledDTO;
import DTO.ShiftToRoleDTO;
import DataAccessLayer.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class Shift {
    private int id;
    private int storeID;
    private LocalDate date;

    private ShiftType type;
    private ShiftStatus status;

    private HashMap< Integer, Role> employeesRequired;
    private List<Role> neededRoles;
    private List<Integer> driversAssigned;

    private List<Integer> WharehouseAssigned;
    private JdbcDriversAssignedDAO jdbcDriversAssignedDAO;
    private JdbcWharehouseAssignedDAO jdbcWharehouseAssignedDAO;

    private JdbcEmployeeToShiftScheduledDAO jdbcEmployeeToShiftScheduledDAO;
    private JdbcShiftDAO jdbcShiftDAO;
    private JdbcShiftToRoleDAO jdbcShiftToRoleDAO;


    public Shift(int id, int storeID, LocalDate date, ShiftType type, ShiftStatus status) {
        this.id = id;
        this.storeID = storeID;
        this.date = date;
        this.type = type;
        this.status = status;
        this.employeesRequired = new HashMap<>();
        this.driversAssigned = new ArrayList<>();
        this.WharehouseAssigned = new ArrayList<>();
        this.neededRoles = new ArrayList<>();
        this.jdbcDriversAssignedDAO = new JdbcDriversAssignedDAO();
        this.jdbcWharehouseAssignedDAO = new JdbcWharehouseAssignedDAO();
        this.jdbcShiftDAO = new JdbcShiftDAO();
        this.jdbcShiftToRoleDAO = new JdbcShiftToRoleDAO();
        this.jdbcEmployeeToShiftScheduledDAO = new JdbcEmployeeToShiftScheduledDAO();
    }
    public int getId() {
        return id;
    }
    public int getStoreID() {
        return storeID;
    }
    public LocalDate getDate() {
        return date;
    }
    public ShiftType getType() {
        return type;
    }
    public ShiftStatus getStatus() {
        return status;
    }
    public HashMap< Integer,Role> getEmployeesRequired() {
        return employeesRequired;
    }
    public void setEmployeesRequired(HashMap< Integer,Role> employeesRequired) {
        this.employeesRequired = employeesRequired;
    }
    public List<Role> getNeededRoles() {
        return neededRoles;
    }
    public void setNeededRoles(List<Role> neededRoles) {
        this.neededRoles = neededRoles;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setStoreID(int storeID) {
        this.storeID = storeID;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public void setType(ShiftType type) {
        this.type = type;
    }
    public void setStatus(ShiftStatus status) {
        this.status = status;
    }
    public boolean addEmployeeRequired(Role role, int empId) {
        if (!employeesRequired.containsKey(empId)) {
            employeesRequired.put(empId, role);
            if(role==Role.DELIVERY_MAN)
            {
                this.driversAssigned.add(empId);

            }
            if(role==Role.WAREHOUSE_MAN)
            {
                this.WharehouseAssigned.add(empId);
            }
            return true;
           // neededRoles.remove(role);
        }
        return false;
    }
    public void removeEmployeeRequired(int empId) {
        if (employeesRequired.containsKey(empId)) {
            Role role = employeesRequired.get(empId);
            employeesRequired.remove(empId);

            if (role == Role.DELIVERY_MAN) {
                driversAssigned.remove(Integer.valueOf(empId));
            }

            if (role == Role.WAREHOUSE_MAN) {
                WharehouseAssigned.remove(Integer.valueOf(empId));
            }
        }
    }

    public void clearEmployeesRequired() {
        employeesRequired.clear();
    }
    public void addNeededRole(Role role) {
        neededRoles.add(role);
        if (role == Role.SHIFT_MANAGER){
            try {
                jdbcShiftToRoleDAO.updateSHIFT_MANAGER(this.id, this.storeID, true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if (role == Role.CASHIER) {
            try {
                jdbcShiftToRoleDAO.updateCashier(this.id, this.storeID, true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if (role == Role.DELIVERY_MAN){
            try {
                jdbcShiftToRoleDAO.updateDeliveryMan(this.id, this.storeID, true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if (role == Role.CLEANING) {
            try {
                jdbcShiftToRoleDAO.updateCleaning(this.id, this.storeID, true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if (role == Role.WAREHOUSE_MAN){
            try {
                jdbcShiftToRoleDAO.updateWAREHOUSE_MAN(this.id, this.storeID, true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if (role == Role.ORDEYLY) {
            try {
                jdbcShiftToRoleDAO.updateORDEYLY(this.id, this.storeID, true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }
    public void removeNeededRole(Role role) {
        neededRoles.remove(role);
        if (role == Role.SHIFT_MANAGER){
            try {
                jdbcShiftToRoleDAO.updateSHIFT_MANAGER(this.id, this.storeID, false);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if (role == Role.CASHIER) {
            try {
                jdbcShiftToRoleDAO.updateCashier(this.id, this.storeID, false);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if (role == Role.DELIVERY_MAN){
            try {
                jdbcShiftToRoleDAO.updateDeliveryMan(this.id, this.storeID, false);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if (role == Role.CLEANING) {
            try {
                jdbcShiftToRoleDAO.updateCleaning(this.id, this.storeID, false);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if (role == Role.WAREHOUSE_MAN){
            try {
                jdbcShiftToRoleDAO.updateWAREHOUSE_MAN(this.id, this.storeID, false);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if (role == Role.ORDEYLY) {
            try {
                jdbcShiftToRoleDAO.updateORDEYLY(this.id, this.storeID, false);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void clearNeededRoles() {
        neededRoles.clear();
        try {
            jdbcShiftToRoleDAO.updateWAREHOUSE_MAN(this.id, this.storeID, false);
            jdbcShiftToRoleDAO.updateORDEYLY(this.id, this.storeID, false);
            jdbcShiftToRoleDAO.updateCleaning(this.id, this.storeID, false);
            jdbcShiftToRoleDAO.updateDeliveryMan(this.id, this.storeID, false);
            jdbcShiftToRoleDAO.updateCashier(this.id, this.storeID, false);
            jdbcShiftToRoleDAO.updateSHIFT_MANAGER(this.id, this.storeID, false);
        } catch (SQLException e) {
            throw new RuntimeException(e);}
    }
    //public void exchangeShift

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Shift Details:")
                .append("\n📅 Date: ").append(date)
                .append("\n⏰ Shift Type: ").append(type)
                .append("\n");

        if (employeesRequired.isEmpty()) {
            sb.append("No employees assigned.\n");
        } else {
            sb.append("Employees Required: \n");
            for (Map.Entry<Integer, Role> entry : employeesRequired.entrySet()) {
                sb.append(" Employee ID: ")
                        .append(entry.getKey())
                        .append(" -  Role: ")
                        .append(entry.getValue())
                        .append("\n");
            }
        }

        return sb.toString();
    }

    public List<Integer> getDriversAssigned() {
        return driversAssigned;
    }
    public List<Integer> getWharehouseAssigned() {
        return WharehouseAssigned;
    }



    public List<Integer> getDriversRequiredList() {

        if (employeesRequired == null) return Collections.emptyList();
        List<Integer> employees = new ArrayList<>();
        for (Map.Entry<Integer, Role> entry : employeesRequired.entrySet())
        {
            Integer key = entry.getKey();
            Role role = entry.getValue();
            if (role==Role.DELIVERY_MAN && key>0)
            {
                employees.add(key);
            }
        }
        return employees;
    }


    public boolean isOneRoleAssigned(Role role) {
        int count = 0;
        for (Map.Entry<Integer, Role> entry : employeesRequired.entrySet()) {
            if (entry.getValue() == role) {
                count++;
            }
        }
        return count<2;
    }

    public void LoadDriversAssignedFromDB(int shiftId, int shiftBranch) {
        this.driversAssigned.clear();
        try {
            List<DriversAssignedDTO> f = jdbcDriversAssignedDAO.findById(shiftId, shiftBranch);
            for (DriversAssignedDTO driversAssignedDTO : f) {
                driversAssigned.add(driversAssignedDTO.employeeId());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void LoadWharehouseAssignedFromDB(int shiftId, int shiftBranch) {
        this.WharehouseAssigned.clear();
        try {
            List<DTO.WharehouseAssignedDTO> f = jdbcWharehouseAssignedDAO.findById(shiftId, shiftBranch);
            for (DTO.WharehouseAssignedDTO wharehouseAssignedDTO : f) {
                WharehouseAssigned.add(wharehouseAssignedDTO.employeeId());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void LoadRoles(){
        neededRoles.clear();
        try {
            ShiftToRoleDTO shiftRoles= jdbcShiftToRoleDAO.findById(this.id, this.storeID);
            if (shiftRoles.CASHIER())
                neededRoles.add(Role.CASHIER);
            if (shiftRoles.SHIFT_MANAGER())
                neededRoles.add(Role.SHIFT_MANAGER);
            if (shiftRoles.CLEANING())
                neededRoles.add(Role.CLEANING);
            if (shiftRoles.ORDEYLY())
                neededRoles.add(Role.ORDEYLY);
            if (shiftRoles.DELIVERY_MAN())
                neededRoles.add(Role.DELIVERY_MAN);
            if (shiftRoles.WAREHOUSE_MAN())
                neededRoles.add(Role.WAREHOUSE_MAN);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void LoadEmployees(){
       try {
           List<EmployeeToShiftScheduledDTO> list = jdbcEmployeeToShiftScheduledDAO.findById(this.id);
           for (EmployeeToShiftScheduledDTO employeeToShiftScheduledDTO : list) {
               employeesRequired.put(employeeToShiftScheduledDTO.employeeID(), Role.valueOf(employeeToShiftScheduledDTO.role()));
               //new
                if (employeeToShiftScheduledDTO.role().equals("DELIVERY_MAN"))
                {
                    driversAssigned.add(employeeToShiftScheduledDTO.employeeID());
                }
           }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Integer> getWharehouseManRequiredList() {
        if (employeesRequired == null) return Collections.emptyList();
        List<Integer> employees = new ArrayList<>();
        for (Map.Entry<Integer, Role> entry : employeesRequired.entrySet())
        {
            Integer key = entry.getKey();
            Role role = entry.getValue();
            if (role==Role.WAREHOUSE_MAN && key>0)
            {
                employees.add(key);
            }
        }
        return employees;
    }
    public boolean isEmpInShift(int empId) {
        return employeesRequired.containsKey(empId);

    }

}
