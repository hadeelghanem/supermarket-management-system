package DomainLayer.Employee.Conrollers;

import DTO.*;
import DataAccessLayer.*;
import DomainLayer.Employee.*;
import DomainLayer.Shipment.Driver;


import java.sql.SQLException;
import java.util.*;

public class EmployeeController {

    private Map<Integer, Employee> employees; // empId -> employee

    private Employee hrManager;
    private JdbcEmployeeToRoleDAO jdbcEmployeeToRoleDAO;
    private JdbcEmployeeDAO jdbcEmployeeDAO ;
    private JdbcDriverDAO jdbcDriverDAO;
    private JdbcEmployeeToAvailabilityDAO jdbcEmployeeToAvailabilityDAO;
    private JdbcHrManagerDAO jdbcHrManagerDAO;
    //private Integer idCounter;
    public EmployeeController() {
        employees = new HashMap<>();
        jdbcEmployeeToRoleDAO = new JdbcEmployeeToRoleDAO();
        jdbcEmployeeDAO = new JdbcEmployeeDAO();
        jdbcDriverDAO = new JdbcDriverDAO();
        jdbcEmployeeToAvailabilityDAO = new JdbcEmployeeToAvailabilityDAO();
        jdbcHrManagerDAO = new JdbcHrManagerDAO();
    }
    public Employee addEmployee(int id, String name, int storeBranch, String bankName, int accountNumber, Double salary, List<Role> qualification, boolean hr, String terms, int license) {
        BankAccount bankDetails = new BankAccount(bankName, storeBranch, accountNumber, name);
        Employee employeex= null;
        if(license ==-1)
        {

            employeex = new Employee(id, name, storeBranch, bankDetails, salary, qualification,hr,terms);
            boolean HR_MANAGER = false;
            boolean SHIFT_MANAGER = false;
            boolean CASHIER = false;
            boolean DELIVERY_MAN = false;
            boolean CLEANING = false;
            boolean WAREHOUSE_MAN = false;
            boolean ORDEYLY = false;
            for (Role currentRole : qualification) {
                if (currentRole.equals(Role.HR_MANAGER)) {
                    HR_MANAGER = true;
                }
                if (currentRole.equals(Role.SHIFT_MANAGER)) {
                    SHIFT_MANAGER = true;
                }
                if (currentRole.equals(Role.CASHIER)) {
                    CASHIER = true;
                }
                if (currentRole.equals(Role.DELIVERY_MAN)) {
                    DELIVERY_MAN = true;
                }
                if (currentRole.equals(Role.CLEANING)) {
                    CLEANING = true;
                }
                if (currentRole.equals(Role.WAREHOUSE_MAN)) {
                    WAREHOUSE_MAN = true;
                }
                if (currentRole.equals(Role.ORDEYLY)) {
                    ORDEYLY = true;
                }


            }

            EmployeeToRoleDTO EmployeeToRoleDtO = new EmployeeToRoleDTO(id, HR_MANAGER, SHIFT_MANAGER, CASHIER, DELIVERY_MAN, CLEANING, WAREHOUSE_MAN, ORDEYLY);
            EmployeeDTO employeeDTO = new EmployeeDTO(id, name, storeBranch, bankName, accountNumber, salary, hr, terms, license);
            try {
                jdbcEmployeeDAO.insert(employeeDTO);
                jdbcEmployeeToRoleDAO.insert(EmployeeToRoleDtO);

            }catch (Exception e) {
                System.out.println("❌ Error adding employee to database: " + e.getMessage());
            }
        }
        else {

            employeex = new Driver(license ,id, name, storeBranch, bankDetails, salary, qualification, hr, terms);

            boolean HR_MANAGER = false;
            boolean SHIFT_MANAGER = false;
            boolean CASHIER = false;
            boolean DELIVERY_MAN = false;
            boolean CLEANING = false;
            boolean WAREHOUSE_MAN = false;
            boolean ORDEYLY = false;
            for (Role currentRole : qualification) {
                if (currentRole.equals(Role.HR_MANAGER)) {
                    HR_MANAGER = true;
                }
                if (currentRole.equals(Role.SHIFT_MANAGER)) {
                    SHIFT_MANAGER = true;
                }
                if (currentRole.equals(Role.CASHIER)) {
                    CASHIER = true;
                }
                if (currentRole.equals(Role.DELIVERY_MAN)) {
                    DELIVERY_MAN = true;
                }
                if (currentRole.equals(Role.CLEANING)) {
                    CLEANING = true;
                }
                if (currentRole.equals(Role.WAREHOUSE_MAN)) {
                    WAREHOUSE_MAN = true;
                }
                if (currentRole.equals(Role.ORDEYLY)) {
                    ORDEYLY = true;
                }


            }

            EmployeeToRoleDTO EmployeeToRoleDtO = new EmployeeToRoleDTO(id, HR_MANAGER, SHIFT_MANAGER, CASHIER, DELIVERY_MAN, CLEANING, WAREHOUSE_MAN, ORDEYLY);
            EmployeeDTO employeeDTO = new EmployeeDTO(id, name, storeBranch, bankName, accountNumber, salary, hr, terms, license);
            DriverDTO driverDTO = new DriverDTO(id, name, license, true);
             try {
                jdbcEmployeeToRoleDAO.insert(EmployeeToRoleDtO);
                jdbcEmployeeDAO.insert(employeeDTO);
                jdbcDriverDAO.insert(driverDTO);

            }catch (Exception e) {
                System.out.println("❌ Error adding employee to database: " + e.getMessage());
            }


        }

        employeex.setId(id);

        employees.put(id, employeex);



        if(hr) {
            hrManager = employeex;

        }
        return employeex;
    }

    public Employee getEmployee(int id) {

        for (Map.Entry<Integer, Employee> entry : employees.entrySet()) {
            if (entry.getKey() == id) {

                return entry.getValue();
            }
        }
        return null;
    }


    public Employee getHrManager() {
        return hrManager;
    }

    public boolean login(int id,String password)  {
        if(password == null || password.isEmpty())
        {  //throw new Exception("Password is empty");
            System.out.println("Password is empty");
            return false;}
        if(employees.get(id) == null){
            //throw new Exception("Employee not found");
            System.out.println("Employee not found");
            return false;
        }

        if(password == null)
        {
            System.out.println("Password is not set- need to register");
            return false;
        }

        employees.get(id).setLoggedIn(true);
        return true;

    }
    public boolean logout(int id)  {
        if(employees.get(id) == null){
            System.out.println("Employee not found");
            return false;
        }
        employees.get(id).setLoggedIn(false);
        return true;
    }
    public boolean setEmployeePassword(int id, String password) {
        if(password == null || password.isEmpty())
        {System.out.println("Password is empty");
            return false;}
        if(employees.get(id) == null){
            System.out.println("Employee not found");
            return false;
        }
        employees.get(id).setPassword(password);
        return true;
    }
    public boolean enterAvailability(int employeeID, int day, boolean morning, boolean evening)  {
        if(employees.get(employeeID) == null){
            //throw new Exception("Employee not found");
            System.out.println("Employee not found");
            return false;
        }
        Availability availability = new Availability(employeeID, day, morning, evening);
        EmployeeToAvailabilityDTO employeeToAvailabilityDTO = new EmployeeToAvailabilityDTO(employeeID, day, morning, evening);

        try {
            jdbcEmployeeToAvailabilityDAO.insert(employeeToAvailabilityDTO);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        employees.get(employeeID).addAvailability(availability);
        return true;
    }


    public boolean register(int id, String password) {
        if(employees.get(id) == null){
            //throw new Exception("Employee already exists");
            System.out.println("Employee does not exists");
            return false;
        }
        employees.get(id).setPassword(password);
        employees.get(id).setLoggedIn(true);
        return true;
    }

    public List<Integer> viewScheduledShifts(int id)
    {
        if(employees.get(id) == null){
            //throw new Exception("Employee not found");
            System.out.println("Employee not found");
            return null;
        }
        return employees.get(id).getShiftIDsFromAssignments();

    }

    public void editBankAccount(int id, String bankName, int accountNumber) {
        Employee employee = employees.get(id);
        employee.setBankAccount(new BankAccount(bankName, employee.getStoreBranchId(), accountNumber, employee.getName()));
    }

    public void newWeek() {
//        for (Employee employee : employees.values()) {
//            employee.newWeek();
//        }
        for (int i = 0; i < employees.size(); i++) {
            employees.get(i).newWeek();
        }
    }
    public List<Integer> getAvailableEmployees(StoreBranch storeBranch, Role selectedRole, int dayOfWeek, boolean isMorning, boolean isEvening)
    {
        List<Integer> availableEmployees = new ArrayList<>();
        for (Map.Entry<Integer, Employee> entry : employees.entrySet()) {
            Integer key = entry.getKey();              // The key from the map
            Employee employee = entry.getValue();      // The Employee object

            if (employee.getStoreBranchId() == storeBranch.getId()) {
                List<Role> qualifications = employee.getQualifications();
                for (Role role : qualifications) {

                    if (role == selectedRole) {
                        if (employee.canWork(dayOfWeek, isMorning, isEvening)) {
                            availableEmployees.add(key);  // Add the key instead of employee.getId()
                        }
                    }
                }
            }
        }
        return availableEmployees;
    }

    public boolean check( StoreBranch branchName) {

        List<Integer> availableEmployees= getgetEmployeesByRole(branchName.getId(),Role.SHIFT_MANAGER);
        if(availableEmployees.size() == 0 || availableEmployees == null) {
            return false;
        }
        else {
            return true;
        }

    }


    public List<Integer> getgetEmployeesByRole(int id, Role role) {
        List<Integer> employeesByRole = new ArrayList<>();
        for (Map.Entry<Integer, Employee> entry : employees.entrySet()) {
            Integer key = entry.getKey();              // The key from the map
            Employee employee = entry.getValue();      // The Employee object

            if (employee.getStoreBranchId() == id) {
                List<Role> qualifications = employee.getQualifications();
                for (Role qualification : qualifications) {
                    if (qualification == role) {
                        employeesByRole.add(key);  // Add the key instead of employee.getId()
                    }
                }
            }
        }
        return employeesByRole;
    }


    public Driver getDriver(Integer driverId) {
        for (Map.Entry<Integer, Employee> entry : employees.entrySet()) {
            int a=entry.getKey();
            int b=driverId;

            if (a==b) {
                return (Driver) entry.getValue();
            }
        }
        return null;
    }
    public List<Driver> getDriversByBranch(int branchID) {
        List<Driver> driverList = new ArrayList<>();
        for (Map.Entry<Integer, Employee> entry : employees.entrySet()) {

        }
        return driverList;
    }

    public void LoadEmployeesFromDB() {

        try {
            List<EmployeeDTO> employeeDTOList = jdbcEmployeeDAO.findAll();
            for (EmployeeDTO employeeDTO : employeeDTOList) {
                if (employeeDTO.isHRManager()){
                    hrManager = (new Employee(employeeDTO.id(), employeeDTO.name(), employeeDTO.storeBranchId(),
                            new BankAccount(employeeDTO.bankAccountName(), employeeDTO.storeBranchId(), employeeDTO.bankAccountNumber(), employeeDTO.name()),
                            employeeDTO.salary(), new ArrayList<>(), true, employeeDTO.employeeTerms()));

                    HrManagerDTO Hr =jdbcHrManagerDAO.findById(employeeDTO.id());

                    hrManager.setPassword1(Hr.pass());
                }
                EmployeeToRoleDTO EmpRole = jdbcEmployeeToRoleDAO.findById(employeeDTO.id());
                List<Role> qualifications = new ArrayList<>();
                if (EmpRole.SHIFT_MANAGER()) {
                    qualifications.add(Role.SHIFT_MANAGER);
                }
                if (EmpRole.CASHIER()) {
                    qualifications.add(Role.CASHIER);
                }
                if (EmpRole.CLEANING()){
                    qualifications.add(Role.CLEANING);
                }
                if (EmpRole.ORDEYLY()){
                    qualifications.add(Role.ORDEYLY);
                }
                if (EmpRole.HR_MANAGER())
                {
                    qualifications.add(Role.HR_MANAGER);
                }
                if (EmpRole.DELIVERY_MAN()){
                    qualifications.add(Role.DELIVERY_MAN);
                }
                if (EmpRole.WAREHOUSE_MAN()){
                    qualifications.add(Role.WAREHOUSE_MAN);
                }


               if (! qualifications.contains(Role.DELIVERY_MAN)) {
                   Employee Emp = (new Employee(employeeDTO.id(), employeeDTO.name(), employeeDTO.storeBranchId(),
                           new BankAccount(employeeDTO.bankAccountName(), employeeDTO.storeBranchId(),
                                   employeeDTO.bankAccountNumber(), employeeDTO.name()),
                           employeeDTO.salary(), qualifications, employeeDTO.isHRManager(), employeeDTO.employeeTerms()));
                   employees.put(employeeDTO.id(),Emp);
               }else {
                   Driver emp1 = new Driver(employeeDTO.licenseNumber(), employeeDTO.id(), employeeDTO.name(), employeeDTO.storeBranchId(),
                           new BankAccount(employeeDTO.bankAccountName(), employeeDTO.storeBranchId(),
                                   employeeDTO.bankAccountNumber(), employeeDTO.name()),
                           employeeDTO.salary(), qualifications, employeeDTO.isHRManager(), employeeDTO.employeeTerms());
                   employees.put(employeeDTO.id(), emp1);



               }

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

