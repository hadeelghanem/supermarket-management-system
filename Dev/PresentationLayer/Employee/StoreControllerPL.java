package PresentationLayer.Employee;

import DomainLayer.Employee.Conrollers.EmployeeController;
import DomainLayer.Employee.Conrollers.StoresController;
import DomainLayer.Employee.Employee;
import DomainLayer.Employee.Role;
import DomainLayer.Factory;
import DomainLayer.Employee.Shift;
import DomainLayer.Employee.ShiftStatus;
import DomainLayer.Employee.ShiftType;
import DomainLayer.Employee.StoreBranch;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class StoreControllerPL {
    private final StoresController branchController;
    private final EmployeeController employeeController;

    private Employee employeeLoggedIn;



    public StoreControllerPL() {
        // Initialize the StoreControllerPL with the provided StoresController and EmployeeController
        this.branchController = Factory.getInstance().getBranchController();
        this.employeeController = branchController.getEmployeeController();
    }
    public EmployeeController getEmployeeController() {
        return employeeController;
    }
    public Employee getEmployeeLoggedIn() {
        return employeeLoggedIn;
    }
    public boolean login(int id, String password) {
        // Implement the login logic here
        if (employeeController.login(id, password)) {
            employeeLoggedIn = employeeController.getEmployee(id);
            System.out.println("Login successful!");
            return true;
        } else {
            System.out.println("Login failed.back to start manu");
            return false;
        }
    }

    public boolean register(int id, String password) {
        // Implement the registration logic here
        if (employeeController.getEmployee(id) == null) {
          // ///// ///////////////////////////////////////////////////////////////////////////
            System.out.println("User not found.");
            return false;
        }
        if(employeeController.getEmployee(id).getPassword()!= null) {
            System.out.println("User already registered.");
            return false;
        }
        if (employeeController.register(id, password)) {
            System.out.println("Registration successful! you are now logged in.");
            employeeLoggedIn = employeeController.getEmployee(id);
//            System.out.println("test register in controller pl " );
            //System.out.println("test register entered id " + id);
//            System.out.println("test register id " + employeeLoggedIn.getId());
            return true;
        } else {
            System.out.println("back to start menu");
            return false;
        }
    }

    public boolean addEmployee(int id, String name, int storeBranch, String bankName, int accountNumber, Double salary, List<Role> qualification, boolean hr, String employeeTerms, int license) {
        // Implement the logic to add an employee
        if(employeeController.getEmployee(id) != null) {
            System.out.println("Employee ID already exists.");
            return false;
        }
        if(branchController.getStoreBranch(storeBranch) == null) {
            //System.out.println("Branch not found.");
            System.out.println("Branch not found.");
            return false;
        }
        employeeController.addEmployee(id, name, storeBranch, bankName, accountNumber, salary, qualification, hr,employeeTerms, license);
        return true;
    }

    public boolean addAvailability(int dayOfWeek , boolean morningShift, boolean eveningShift)
    {
        return employeeController.enterAvailability(employeeLoggedIn.getId(),dayOfWeek, morningShift, eveningShift);
    }

    public boolean logout(int id) {
        // Implement the logout logic here
        if (employeeController.logout(id)) {
            employeeLoggedIn = null;
            //System.out.println("Logout successful!");
            return true;
        } else {
            //System.out.println("Logout failed.");
            return false;
        }
    }

    public void viewScheduledShifts() {
//        System.out.println("test viewScheduledShifts in storeControllerPL");
//        System.out.println("test : the id logged in + " + employeeLoggedIn.getId());
        List<Integer> shifts=employeeController.viewScheduledShifts(employeeLoggedIn.getId());
        if (shifts.isEmpty()) {
            System.out.println("No scheduled shifts found.");
            return;
        }
        branchController.viewScheduledShiftsById(shifts,employeeLoggedIn.getStoreBranchId());

    }
    public void viewScheduledShifts(String branchName) {
        branchController.viewScheduledShifts(branchName);
    }

    public boolean editBankAccount(String bankName, int accountNumber) {

        employeeController.editBankAccount(employeeLoggedIn.getId(), bankName, accountNumber);
        return true;

    }

//    public boolean removeAvailability(int day, boolean availableMorning, boolean availableEvening) {
//
//        return employeeController.removeAvailability(employeeLoggedIn.getId(), day, availableMorning, availableEvening);
//    }

    public List<String> getAllBranchesNames() {
        return branchController.getAllBranchesNames();
    }

    public void viewOldShifts(String branchName) {

        branchController.viewOldShifts(branchName);

    }

    public String getBranchStatus(String name) {

        return branchController.getStatus(name);
    }

    public boolean editBranchStatus(String name, String status) {
        if (branchController.editBranchStatus(name, status)) {
            //System.out.println("Branch status updated successfully.");
            return true;
        } else {
            //System.out.println("Failed to update branch status.");
            return false;
        }
    }

    public List<Shift> getShiftScheduled(String branchName) {
        return branchController.getShiftScheduled(branchName);
    }

    public boolean addRoleToShift(String branchName, int selectedShift, Role selectedRole) {
       return branchController.addRoleToShift(branchName, selectedShift, selectedRole);
    }

    public boolean removeRoleFromShift(String branchName, int selectedShift, Role selectedRole) {
        return branchController.removeRoleFromShift(branchName, selectedShift, selectedRole);
    }

    public Shift getShiftById(String branchName, int selectedShift) {
        List<Shift> shifts = branchController.getShiftScheduled(branchName);
        for (Shift shift : shifts) {
            if (shift.getId() == selectedShift) {
                return shift;
            }
        }
        return null;
    }

    public List<Integer> getAvailableEmployees(String branchName, Role selectedRole, int selectedShift) {
        List<Integer> availableEmployees = branchController.getAvailableEmployees(branchName, selectedRole, selectedShift);
        return branchController.notInShift(availableEmployees, branchName, selectedShift);
        //return availableEmployees;
    }


    public boolean addEmployeeToShift(int selectedEmployeeId, String branchName, int selectedShift, Role selectedRole)
    {
        int branchID = branchController.getBranchByName(branchName).getId();
        if (branchController.addEmployeeToShift(selectedEmployeeId, branchID, selectedShift, selectedRole)) {
            //System.out.println("✅ Employee ID " + selectedEmployeeId + " assigned to shift with role: " + selectedRole.name());
            return true;
        } else {
            //System.out.println("❌ Failed to assign employee to shift.");
            return false;
        }
    }

    public boolean removeEmployeeFromShift(int selectedEmployeeId, String branchName, int selectedShift, Role selectedRole) {
        int branchID = branchController.getBranchByName(branchName).getId();
        if (branchController.removeEmployeeFromShift(selectedEmployeeId, branchID, selectedShift,selectedRole)) {
            //System.out.println("✅ Employee ID " + selectedEmployeeId + " removed from shift.");

            return true;
        } else {
            //System.out.println("❌ Failed to remove employee from shift.");
            return false;
        }
    }

    public boolean cancelShift(String branchName, int selectedShift) {
        if (branchController.cancelShift(branchName, selectedShift)) {
            //System.out.println("✅ Shift ID " + selectedShift + " canceled successfully.");
            return true;
        } else {
            System.out.println("❌ Failed to cancel shift.");
            return false;
        }
    }

    public int addNewShift(String branchName, LocalDate date, ShiftType type, ShiftStatus status) {
        int branchID = branchController.getBranchByName(branchName).getId();
        int shift = branchController.addNewShift(branchID, date, type, status);
        return  shift;
    }




    public void newWeek() {
        employeeController.newWeek();
        branchController.newWeek();

    }



    public boolean check(String branchName) {
        StoreBranch storeBranch = branchController.getBranchByName(branchName);
        if (storeBranch == null) {
            //System.out.println("❌ Branch not found.");
            return false;
        }
        if (employeeController.check( storeBranch)) {
            //System.out.println("✅ Availability updated successfully.");
            return true;
        } else {
            //System.out.println("❌ Failed to update availability.");
            return false;
        }
    }




    public List<Integer> notAvailabelEmployeesByTimeAndRole(String branchName, Role selectedRole, int selectedShift) {
        List<Integer> notAvailableEmployees = branchController.notAvailabelEmployeesByTimeAndRole(branchName, selectedRole, selectedShift);
       return branchController.notInShift(notAvailableEmployees, branchName, selectedShift);
        // return notAvailableEmployees;
    }
}
