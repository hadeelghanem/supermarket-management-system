package DomainLayer.Employee.Conrollers;

import DTO.ShiftDTO;
import DTO.ShiftToRoleDTO;
import DataAccessLayer.JdbcShiftDAO;
import DataAccessLayer.JdbcShiftToRoleDAO;
import DataAccessLayer.ShiftToRoleDAO;
import DomainLayer.API.ShipmentEmployeeAPI;
import DomainLayer.Employee.*;
import DomainLayer.Shipment.Area;
import DomainLayer.Shipment.Driver;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class StoresController {
    HashMap<Integer, StoreBranch> branches;
    private Integer storeCounter;

    EmployeeController employeeController;
    private int shiftID ;
    private static StoresController instance = null;

    private JdbcShiftDAO jdbcShiftDAO;
    private JdbcShiftToRoleDAO jdbcShiftToRoleDAO;

    public StoresController() {
        this.employeeController = new EmployeeController();
        branches = new HashMap<>();
        storeCounter = 0;
        jdbcShiftDAO = new JdbcShiftDAO();
        jdbcShiftToRoleDAO = new JdbcShiftToRoleDAO();
    }

    public static StoresController getInstance() {
        if (instance == null)
            instance = new StoresController();
        return instance;
    }
        public void addStoreBranch(int id, String name, String address, StoreStatus status,String tel, String contact_name, Area area) {
        //String tel, String contact_name, Area area


        StoreBranch storeBranch = new StoreBranch(id, name, address, status, tel, contact_name, area);
        branches.put(storeCounter, storeBranch);
        storeCounter++;
        shiftID = 0;
    }
    public void removeStoreBranch(int id) {
        branches.remove(id);
    }
    public StoreBranch getStoreBranch(int id) {
        return branches.get(id);
    }
    public HashMap<Integer, StoreBranch> getBranches() {
        return branches;
    }


    public boolean addRoleToShift(String branch, int shiftId, Role role)  {
        //StoreBranch storeBranch = branches.get(branchId);
        StoreBranch storeBranch = getBranchByName(branch);
        Shift shift =null;
        for (Shift s : storeBranch.getShiftScheduled()) {
            if (s.getId() == shiftId) {
                shift = s;
                break;
            }
        }
        if (storeBranch != null) {
            for (int i = 0; i < storeBranch.getShiftScheduled().size(); i++) {
                if (storeBranch.getShiftScheduled().get(i).getId() == shiftId) {
                    if (storeBranch.getShiftScheduled().get(i).getNeededRoles().contains(role)) {
                        System.out.println("❌ Role already exists in the shift.");
                        return false; // Role already exists in the shift
                    }

                    List<Integer> availableEmployees = getEmployeesByRole(branch, role);
                    if (availableEmployees==null || availableEmployees.isEmpty()) {
                        System.out.println("❌ No  employees for this role.cant add it.");
                        return false;
                    }

                    storeBranch.getShiftScheduled().get(i).addNeededRole(role);
                    return true; // Role added successfully

                }
            }
        }
        System.out.println("❌ Shift not found.");
        return false; // Shift not found
    }



    public List<String> getAllBranchesNames() {
        return branches.values().stream()
                .map(StoreBranch::getName)
                .toList();
    }

    public void viewOldShifts(String branchName) {
        StoreBranch storeBranch = getBranchByName(branchName);
        if (storeBranch == null) {
            System.out.println("Branch not found");
            return;
        }
        System.out.println("Old shifts for branch " + branchName + ":");
        for (Shift shift : storeBranch.getShiftHistory()) {
            System.out.println(shift.toString());
        }
    }
    public StoreBranch getBranchByName(String branchName) {
        for (StoreBranch storeBranch : branches.values()) {
            if (storeBranch.getName().equals(branchName)) {
                return storeBranch;
            }
        }
        return null;
    }

    public void viewScheduledShifts(String branchName) {
        StoreBranch storeBranch = getBranchByName(branchName);
        if (storeBranch == null) {
            System.out.println("Branch not found");
            return;
        }
        System.out.println("Scheduled shifts for branch " + branchName + ":");
        for (Shift shift : storeBranch.getShiftScheduled()) {
            System.out.println(shift.toString());
        }
    }

    public String getStatus(String name) {
        StoreBranch storeBranch = getBranchByName(name);
        if (storeBranch == null) {
            return null;
        }
        return storeBranch.getStatus().toString();
    }
    public boolean editBranchStatus(String name, String status) {
        StoreBranch storeBranch = getBranchByName(name);
        if (storeBranch == null) {
            System.out.println("Branch not found");
            return false;
        }
        if (status.equals("OPEN")) {
            storeBranch.setStatus(StoreStatus.OPEN);
        } else if (status.equals("CLOSED")) {
            storeBranch.setStatus(StoreStatus.CLOSED);
        } else {
            return false;
        }
        return true;
    }

    public List<Shift> getShiftScheduled(String branchName) {
        StoreBranch storeBranch = getBranchByName(branchName);
        if (storeBranch == null) {
            System.out.println("Branch not found");
            return null;
        }
        return storeBranch.getShiftScheduled();
    }

    public boolean removeRoleFromShift(String branchName, int selectedShift, Role selectedRole) {
        StoreBranch storeBranch = getBranchByName(branchName);
        Shift shift =null;
        for (Shift s : storeBranch.getShiftScheduled()) {
            if (s.getId() == selectedShift) {
                shift = s;
                break;
            }
        }
        if (storeBranch != null) {
            for (int i = 0; i < storeBranch.getShiftScheduled().size(); i++) {
                if (storeBranch.getShiftScheduled().get(i).getId() == selectedShift) {
                    if (selectedRole == Role.SHIFT_MANAGER) {
                        System.out.println("❌ Cannot remove SHIFT_MANAGER role.");
                        return false; // Cannot remove SHIFT_MANAGER role
                    }
                    if (!storeBranch.getShiftScheduled().get(i).getNeededRoles().contains(selectedRole)) {
                        System.out.println("❌ Role does not exist in the shift.");
                        return false; // Role does not exist in the shift
                    }
                    //check if its SHIFT_MANAGER

                    //make sure there is no employee assigned to this role
                    HashMap<Integer, Role> employeesRequired = storeBranch.getShiftScheduled().get(i).getEmployeesRequired();
                    for (int k = 0; k < employeesRequired.size(); k++) {
                        if (employeesRequired.get(k) == selectedRole) {
                            System.out.println("❌ Cannot remove role, employee is assigned to this role in shift " );
                            return false;
                        }
                    }
                    storeBranch.getShiftScheduled().get(i).removeNeededRole(selectedRole);
                    return true; // Role removed successfully

                }
            }
        }
        System.out.println("❌ Shift not found.");
        return false; // Shift not found

    }


    public void viewScheduledShiftsById(List<Integer> shifts, int storeBranchId) {
        StoreBranch storeBranch = branches.get(storeBranchId);
        if (storeBranch == null) {
            System.out.println("Store branch not found");
            return;
        }
        System.out.println("Scheduled shifts for the next week :");
        for (int i = 0; i < shifts.size(); i++) {

            System.out.println(storeBranch.getShiftScheduled().get(shifts.get(i)).toString());
        }
    }

    public List<Integer> getAvailableEmployees(String branchName, Role selectedRole, int selectedShift) {
        StoreBranch storeBranch = getBranchByName(branchName);
        if (storeBranch == null) {
            System.out.println("Branch not found");
            return null;
        }

        List<Integer> availableEmployees = new ArrayList<>();
        //Shift shift = storeBranch.getShiftScheduled().get(selectedShift);
        Shift shift =null;
        for (Shift s : storeBranch.getShiftScheduled()) {
            if (s.getId() == selectedShift) {
                shift = s;
                break;
            }
        }

        int dayOfWeek= shift.getDate().getDayOfWeek().getValue(); // 1 = Monday, 2 = Tuesday, ..., 7 = Sunday
        boolean isMorning = shift.getType() == ShiftType.MORNING;
        availableEmployees=employeeController.getAvailableEmployees(storeBranch, selectedRole,dayOfWeek, isMorning, !isMorning);

        if (availableEmployees==null || availableEmployees.isEmpty()) {
            return null;
        } else {
            return availableEmployees;
        }

    }

    public List<Integer> getEmployeesByRole(String branchName, Role role) {
        StoreBranch storeBranch = getBranchByName(branchName);
        if (storeBranch == null) {
            System.out.println("Branch not found");
            return null;
        }
        List<Integer> employees = employeeController.getgetEmployeesByRole(storeBranch.getId(),role);
        return employees;

    }


    public boolean addEmployeeToShift(int empId, int branchId, int shiftId, Role role)  {
        StoreBranch storeBranch = branches.get(branchId);
        //Shift shift =storeBranch.getShiftScheduled().get(shiftId);
        Shift shift =null;
        for (Shift s : storeBranch.getShiftScheduled()) {
            if (s.getId() == shiftId) {
                shift = s;
                break;
            }
        }
        Employee employee = employeeController.getEmployee(empId);
        if (employee == null) {
            System.out.println("❌ Employee not found.");
            return false; // Employee not found
        }
        if (storeBranch != null) {
            for (int i = 0; i < storeBranch.getShiftScheduled().size(); i++) {
                if (storeBranch.getShiftScheduled().get(i).getId() == shiftId) {
                    boolean bool=storeBranch.getShiftScheduled().get(i).addEmployeeRequired(role, empId);
                    if(!bool) {
                        System.out.println("❌ Employee already exists in the shift.");
                        return false; // Employee already exists in the shift
                    }
                    employeeController.getEmployee(empId).addShiftScheduled(shiftId, role);

                    return true; // Employee added to shift successfully

                }
            }
        }
        return false; // Employee not added to shift
    }
    private boolean check(Role selectedRole ,Role role, StoreBranch storeBranch,Shift shift,String msg)
    {
        boolean isDriver= Role.DELIVERY_MAN==role;
        if(selectedRole==role && shift.isOneRoleAssigned(role)) {
            if (new ShipmentEmployeeAPI().shipmentExists(storeBranch, shift.getDate(), shift.getType(), isDriver)) {
                System.out.println(msg);
                return false;
            } else {
                return true;
            }
        }
        return true;

    }
    public boolean removeEmployeeFromShift(int empId, int branchId, int shiftId,Role selectedRole)
    {

        StoreBranch storeBranch = branches.get(branchId);
        //Shift shift =storeBranch.getShiftScheduled().get(shiftId);
        Shift shift =null;
        for (Shift s : storeBranch.getShiftScheduled()) {
            if (s.getId() == shiftId) {
                shift = s;
                break;
            }
        }
        Employee employee = employeeController.getEmployee(empId);
        if(!check(selectedRole,Role.DELIVERY_MAN,storeBranch,shift,"❌ Cannot remove driver assigned to shipment."))
        {
            return false;
        }
        if(!check(selectedRole,Role.WAREHOUSE_MAN,storeBranch,shift,"❌ Cannot remove WAREHOUSE_MAN - shipment on the way."))
        {
            return false;
        }


        if(selectedRole==Role.SHIFT_MANAGER)
        {
            System.out.println("❌ Cannot remove shift manager from shift.");
            return false;
        }
        if (employee == null) {
            System.out.println("❌ Employee not found.");
            return false; // Employee not found
        }
        if (storeBranch != null)
        {
            for (int i = 0; i < storeBranch.getShiftScheduled().size(); i++)
            {
                if (storeBranch.getShiftScheduled().get(i).getId() == shiftId)
                {
                    storeBranch.getShiftScheduled().get(i).removeEmployeeRequired(empId);
                    employeeController.getEmployee(empId).removeShiftScheduled(shift.getId());

                    //remove employee from shift
                    //new ShipmentEmployeeAPI.cancelShipment
                    return true; // Employee removed from shift successfully
                    //break;
                }
            }
        }
        return false; // Employee not removed from shift
    }


    public boolean cancelShift(String branchName, int selectedShift)
    {
        StoreBranch storeBranch = getBranchByName(branchName);
        if (storeBranch == null) {
            System.out.println("Branch not found");
            return false;
        }
        for (int i = 0; i < storeBranch.getShiftScheduled().size(); i++) {
            if (storeBranch.getShiftScheduled().get(i).getId() == selectedShift) {
                Shift shift = storeBranch.getShiftScheduled().get(i);
                removeShift(storeBranch.getId(), shift);
                //this.shiftID--;
                //storeBranch.addShiftToHistory(shift);

                try {
                    jdbcShiftDAO.delete(selectedShift);
                    jdbcShiftToRoleDAO.delete(selectedShift, storeBranch.getId());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                return true; // Shift canceled successfully
            }
        }
        System.out.println("❌ Shift not found.");
        return false; // Shift not found
    }

    public void removeShift(int branchId, Shift shift)  {

        StoreBranch storeBranch = branches.get(branchId);
        if (storeBranch != null) {
            storeBranch.cancelShiftScheduled(shift);
            HashMap<Integer, Role> employeesRequired = shift.getEmployeesRequired();
            for (Map.Entry<Integer, Role> entry : employeesRequired.entrySet()) {
                int empId = entry.getKey();
                Role role = entry.getValue();
                Employee employee = employeeController.getEmployee(empId);
                if (employee != null) {
                    employee.removeShiftScheduled(shift.getId());
                }
            }

        }
    }

    public int addNewShift(int branchID, LocalDate date, ShiftType type, ShiftStatus status) {
        StoreBranch storeBranch = branches.get(branchID);
        if (storeBranch != null) {
            Shift shift = new Shift(shiftID, branchID, date, type, status);
            ShiftDTO shiftDTO = new ShiftDTO(shiftID, branchID, date.toString(), type.toString(), status.toString());
            ShiftToRoleDTO shiftToRoleDTO = new ShiftToRoleDTO(shiftID, branchID, false, false, false, false, false, false);
            try {
                jdbcShiftDAO.insert(shiftDTO); // Save the shift to the database
                jdbcShiftToRoleDAO.insert(shiftToRoleDTO); // Save the shift roles to the database
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            // Check if the shift already exists
            for (Shift existingShift : storeBranch.getShiftScheduled()) {
                if (existingShift.getDate().equals(date) && existingShift.getType() == type) {
                    System.out.println("❌ Shift already exists.");
                    return -1; // Shift already exists
                }
            }
            storeBranch.addShiftScheduled(shift);
            shiftID++;
            return shiftID-1; // Shift added successfully
        }
        return -1; // Shift not added
    }



    public void newWeek() {
        for (StoreBranch storeBranch : branches.values()) {
            storeBranch.newWeek();
        }

    }

    public List<Integer> notAvailabelEmployeesByTimeAndRole(String branchName, Role selectedRole, int selectedShift) {
        List<Integer> availableEmployeesByTime = getAvailableEmployees(branchName, selectedRole, selectedShift);
        List<Integer> availableEmployees=getEmployeesByRole(branchName, selectedRole);

        if (availableEmployeesByTime==null ||availableEmployeesByTime.isEmpty() )
        {
            return availableEmployees;
        }
        List<Integer> notAvailable = new ArrayList<>();
        for (Integer empId : availableEmployees) {
            if (!availableEmployeesByTime.contains(empId)) {
                notAvailable.add(empId);
            }
        }
        return notAvailable;

    }


    public List<Driver> driversInShift(LocalDate date, LocalTime time, int destination) {
        StoreBranch storeBranch = branches.get(destination);
        if (storeBranch == null) {
            System.out.println("Branch not found");
            return null;
        }
        LocalTime noon = LocalTime.NOON;   // 12:00
        ShiftType shiftType = null;
        if (time.isBefore(noon)) {
             shiftType = ShiftType.MORNING;
        } else {
                shiftType = shiftType.NIGHT;
        }

        List<Driver> drivers = new ArrayList<>();
        for (Shift shift : storeBranch.getShiftScheduled())
        {

            if (shift.getDate().equals(date) && shift.getType() == shiftType) {
                List<Integer> driverIds = shift.getDriversAssigned();


                for (Integer driverId : driverIds) {
                    Driver driver = employeeController.getDriver(driverId);
//                    System.out.println(driverId);
                    if (driver != null) {
                        drivers.add(driver);
                    }
                }
            }
        }
        return drivers;
    }



    public boolean shiftHasWarehouseKeeper(LocalDate date, LocalTime time, int destination) {
        StoreBranch storeBranch = branches.get(destination);
        if (storeBranch == null) {
            System.out.println("Branch not found");
            return false;
        }
        LocalTime noon = LocalTime.NOON;   // 12:00
        ShiftType shiftType = null;
        if (time.isBefore(noon)) {
            shiftType = ShiftType.MORNING;
        } else {
            shiftType = shiftType.NIGHT;
        }
        for (Shift shift : storeBranch.getShiftScheduled())
        {
            if (shift.getDate().equals(date) && shift.getType() == shiftType) {
                //new getWharehouseManRequiredList
                if(shift.getWharehouseManRequiredList().size() > 0) {
                    return true; // Shift has a warehouse keeper
                }
                else {
                    return false; // Shift does not have a warehouse keeper
                }

            }
        }
        return false;
    }

    public EmployeeController getEmployeeController() {
        return employeeController;
    }

    public List<Driver> getDrivers(int source) {
        StoreBranch storeBranch = branches.get(source);
        if (storeBranch == null) {
            System.out.println("Branch not found");
            return null;
        }
        List<Driver> drivers = employeeController.getDriversByBranch(source);
        if (drivers == null || drivers.isEmpty()) {
            System.out.println("❌ No drivers available in this branch WE IN STORE CONTROLLER.");
            return Collections.emptyList(); // Return an empty list if no drivers are found
        }
        return drivers;
    }



    public void loadShiftsIdCounter() {
        for (StoreBranch storeBranch : branches.values())
        {
            if(storeBranch.getShiftScheduled()!=null &&  !storeBranch.getShiftScheduled().isEmpty())
            {
                this.shiftID+=storeBranch.getShiftScheduled().size();
            }
        }
    }


    public List<Integer> notInShift(List<Integer> availableEmployees, String branchName, int selectedShift) {
        StoreBranch storeBranch = getBranchByName(branchName);
        if (storeBranch == null) {
            System.out.println("Branch not found");
            return null;
        }

        List<Integer> empp = new ArrayList<>();
        //Shift shift = storeBranch.getShiftScheduled().get(selectedShift);
        Shift shift =null;
        for (Shift s : storeBranch.getShiftScheduled()) {
            if (s.getId() == selectedShift) {
                shift = s;
                break;
            }
        }
        if (shift == null) {
            System.out.println("❌ Shift not found.");
            return null;
        }
        if (availableEmployees==null || availableEmployees.isEmpty()) {
            return empp; // Return empty list if no available employees
        }
        for( Integer empId : availableEmployees) {
            if (!shift.isEmpInShift(empId)) {
                empp.add(empId);
            }
        }
        return empp;
    }
}
