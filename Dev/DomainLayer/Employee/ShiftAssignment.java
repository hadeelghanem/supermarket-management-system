package DomainLayer.Employee;

import DomainLayer.Employee.Role;

public class ShiftAssignment {
    private int id;
    private int shiftID;
    private int employeeID;
    private Role role;

    public ShiftAssignment( int shiftID, int employeeID, Role role) {
        //this.id = id;
        this.shiftID = shiftID;
        this.employeeID = employeeID;
        this.role = role;
    }
    public int getId() {
        return id;
    }
    public int getShiftID() {
        return shiftID;
    }
    public int getEmployeeID() {
        return employeeID;
    }
    public Role getRole() {
        return role;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setShiftID(int shiftID) {
        this.shiftID = shiftID;
    }
    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }
    public void setRole(Role role) {
        this.role = role;
    }



}
