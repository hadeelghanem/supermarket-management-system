package DomainLayer.Employee;
//import WorkersMoudle.src.DomainLayer.ShiftAssignment;

import DTO.*;
import DataAccessLayer.*;

import java.sql.SQLException;
import java.util.*;

public class Employee {

    private int id;
    private String name;
    private int storeBranchId;
    private BankAccount bankDetails;
    private Double salary;
    private String employeeTerms;
    private Date startDate;
    //private String qualifications;
    private List<Role> qualifications;

    private List<ShiftAssignment> shiftScheduled;
    private List<ShiftAssignment> shiftHistory;

    private List<Availability> availability;
    private JdbcEmployeeToHistoryDAO jdbcEmployeeToHistoryDAO;
    private JdbcEmployeeToShiftScheduledDAO jdbcEmployeeToShiftScheduledDAO;
    private String password;
    private boolean loggedIn;

    private boolean isHRManager;
    JdbcWharehouseAssignedDAO jdbcWharehouseAssignedDAO;
    JdbcDriversAssignedDAO jdbcDriversAssignedDAO;

    private JdbcHrManagerDAO jdbcHrManagerDAO;

    public Employee(int id, String name, int storeBranch, BankAccount bankDetails, Double salary, List<Role> qualification, boolean isHRManager, String employeeTerms) {
     //  if (salary < 0)
            // throw new Exception("Salary must be positive");
            this.id = id;
        this.name = name;
        this.storeBranchId = storeBranch;
        this.bankDetails = bankDetails;
        this.salary = salary;
        this.startDate = new Date(System.currentTimeMillis());
        this.qualifications = qualification;
        this.shiftScheduled = new ArrayList<>();
        this.availability = new ArrayList<>();
        this.shiftHistory = new ArrayList<>();
        this.loggedIn = false;
        this.isHRManager = isHRManager;
        this.employeeTerms = employeeTerms;
        this.jdbcEmployeeToHistoryDAO = new JdbcEmployeeToHistoryDAO();
        this.jdbcEmployeeToShiftScheduledDAO = new JdbcEmployeeToShiftScheduledDAO();
        this.jdbcWharehouseAssignedDAO = new JdbcWharehouseAssignedDAO();
        this.jdbcDriversAssignedDAO = new JdbcDriversAssignedDAO();
        this.jdbcHrManagerDAO = new JdbcHrManagerDAO();
    }

    public int getId() {
        return this.id;
    }

//        public void setId(int id) {
//        this.id = id;
//    }
    public String getName() {
        return name;
    }

    public int getStoreBranchId() {
        return storeBranchId;
    }

    public BankAccount getBankDetails() {
        return bankDetails;
    }

    public Double getSalary() {
        return salary;
    }

    public Date getStartDate() {
        return startDate;
    }

    public List<Role> getQualifications() {
        return qualifications;
    }

    public List<ShiftAssignment> getShiftScheduled() {
        return shiftScheduled;
    }

    public List<Availability> getAvailability() {
        return availability;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }



    public boolean setSalary(Double salary) {
        if (salary < 0) {
            //throw new Exception("Salary must be positive");
            System.out.println("Salary must be positive");
            return false;
        }
        this.salary = salary;
        return true;
    }

    public boolean addQualification(Role qualification) {
        if (qualification == null)
        //throw new Exception("Qualification cannot be null");
        {
            System.out.println("Qualification cannot be null");
            return false;
        }
        this.qualifications.add(qualification);
        return true;
    }

    public boolean removeQualification(Role qualification) {
        if (qualification == null)
        //throw new Exception("Qualification cannot be null");
        {
            System.out.println("Qualification cannot be null");
            return false;
        }
        this.qualifications.remove(qualification);
        return true;
    }

    public boolean addShiftScheduled(int shift, Role role) {
        ShiftAssignment shiftAssignment = new ShiftAssignment(shift, this.id, role);
        if (shift == -1) {
            System.out.println("Shift cannot be null");
            return false;
        }
        this.shiftScheduled.add(shiftAssignment);
        EmployeeToShiftScheduledDTO employeeToShiftScheduledDTO = new EmployeeToShiftScheduledDTO(shift, this.id, role.toString());
        EmployeeToHistoryDTO employeeToHistoryDTO = new EmployeeToHistoryDTO(shift, this.id, role.toString());
        try {

            jdbcEmployeeToShiftScheduledDAO.insert(employeeToShiftScheduledDTO);
            jdbcEmployeeToHistoryDAO.insert(employeeToHistoryDTO);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (role == Role.WAREHOUSE_MAN) {
            WharehouseAssignedDTO wharehouseAssignedDTO = new WharehouseAssignedDTO(this.id, shift, storeBranchId);
            try {
                jdbcWharehouseAssignedDAO.insert(wharehouseAssignedDTO);
            } catch (SQLException e) {
                System.out.println("❌ Failed to insert WharehouseAssignedDTO: " + e.getMessage());
            }
        }
        if (role == Role.DELIVERY_MAN) {

            DriversAssignedDTO driversAssignedDTO = new DriversAssignedDTO(this.id, shift, storeBranchId);
            try {

                jdbcDriversAssignedDAO.insert(driversAssignedDTO);
            } catch (SQLException e) {
                System.out.println("❌ Failed to insert DriversAssignedDTO: " + e.getMessage());
            }
        }

        return true;
    }
    public boolean removeShiftScheduled(int shiftId)  {
        if(shiftId == -1)
            //throw new Exception("Shift cannot be null");
        {
            System.out.println("Shift cannot be null");
            return false;
        }
        //this.shiftScheduled.remove(shift);
        ShiftAssignment target = null;
        for (ShiftAssignment sa : shiftScheduled) {
            if (sa.getShiftID() == shiftId) {
                target = sa;
                break;
            }
        }
        if (target != null) {
            // Remove from scheduled and add to history
            shiftScheduled.remove(target);
            shiftHistory.add(target);
            System.out.println("✅ ShiftAssignment with ID " + shiftId + " moved to history.");
        } else {
            System.out.println("❌ No ShiftAssignment found with ID " + shiftId + ".");
        }
        return true;
    }
    public boolean addAvailability(Availability availability)  {
        if(availability == null)
        {
            System.out.println("Availability cannot be null");
            return false;
        }
        this.availability.add(availability);
        return true;
    }

    public boolean isShiftAvailableForEmployee( Availability availabilityA) {
        if (this.availability.contains(availabilityA)) {
            System.out.println("✅ Availability found.");
            return true;
        } else {
            System.out.println("❌ Availability not found.");
            return false;
        }

    }


    public void setPassword(String _password) {
        if(_password == null || _password.isEmpty())
            throw new IllegalArgumentException("Password cannot be null or empty");
        this.password = _password;
        try {
            jdbcHrManagerDAO.insert(new HrManagerDTO(this.id,_password));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public String getPassword() {
        return password;
    }
    public boolean getP(String password) {
        return this.password.equals(password);
    }
    public void setBankAccount(BankAccount bankAccount) {
        this.bankDetails = bankAccount;
    }


    public void setLoggedIn(boolean b) {
        this.loggedIn = b;
    }


    public boolean isHRManager() {
        return isHRManager;
    }
    public List<Integer> getShiftIDsFromAssignments() {
        List<Integer> shiftIDs = new ArrayList<>();

        // Iterate over each ShiftAssignment in the list
        for (ShiftAssignment assignment : shiftScheduled) {

            shiftIDs.add(assignment.getShiftID());
        }

        return shiftIDs;
    }

    public void newWeek() {
        shiftHistory.addAll(shiftScheduled);
        shiftScheduled.clear();
        availability.clear();
    }

    public List<ShiftAssignment> getShiftHistory() {
        return shiftHistory;
    }

    public boolean canWork(int dayOfWeek, boolean isMorning, boolean isEvening) {

        if(this.availability == null || this.availability.isEmpty())
        {
            return false;
        }
        for(Availability availability : this.availability)
        {
            if(availability.getDayOfWeek() == dayOfWeek )
            {
                if( (availability.isMorningShift() ==true && isMorning) || (availability.isEveningShift()==true && isEvening))
                {
                    return true;
                }

            }
        }
        return false;

    }



    public void setPassword1(String pass) {
        this.password = pass;
    }
}

