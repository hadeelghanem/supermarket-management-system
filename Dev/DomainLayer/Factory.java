package DomainLayer;


import DTO.EmployeeDTO;
import DTO.EmployeeToRoleDTO;
import DataAccessLayer.JdbcBankAccountDAO;
import DataAccessLayer.JdbcEmployeeDAO;
import DataAccessLayer.JdbcEmployeeToRoleDAO;
import DomainLayer.Employee.*;
import DomainLayer.Employee.Conrollers.EmployeeController;
import DomainLayer.Employee.Conrollers.StoresController;
import DomainLayer.Shipment.Area;
import DomainLayer.Shipment.TransportManager;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Factory {
    private static Factory instance = null;
    private final StoresController branchController;
    private final TransportManager transportManager;
    private final EmployeeController employeeController;

    private final JdbcBankAccountDAO jdbcBankAccountDAO;
    private final JdbcEmployeeDAO jdbcEmployeeDAO;
    private final JdbcEmployeeToRoleDAO jdbcEmployeeToRoleDAO;


    private Factory() {
        this.branchController = new StoresController();
        this.transportManager = new TransportManager();
        this.employeeController = new EmployeeController();
        this.jdbcEmployeeDAO = new JdbcEmployeeDAO();
        this.jdbcBankAccountDAO = new JdbcBankAccountDAO();
        this.jdbcEmployeeToRoleDAO = new JdbcEmployeeToRoleDAO();

        branchController.addStoreBranch(0, "Branch0", "Address 0", StoreStatus.OPEN, "tel1", "contact1", Area.NORTH);
        branchController.addStoreBranch(1, "Branch1", "Address 1", StoreStatus.OPEN, "tel2", "contact2", Area.SOUTH);

        //Initialize();
    }

    public static Factory getInstance() {
        if (instance == null) {
            instance = new Factory();
        }
        return instance;
    }

    public StoresController getBranchController() {
        return branchController;
    }

    public TransportManager getTransportManager() {
        return transportManager;
    }


    public void Initialize(){

        // shiftManagerRole
        List<Role> hrManagerRole = new ArrayList<>();
        hrManagerRole.add(Role.HR_MANAGER);
        Employee hrManager = branchController.getEmployeeController().addEmployee(0, "HRManager", -1, "BankName", 123456789, 10000.0, hrManagerRole, true, "terms", -1);
        hrManager.setPassword("pass1");
        /////////////////////////////////////////////////////////////////////////////////////////
//            EmployeeDTO DTO = new EmployeeDTO(hrManager.getId(), hrManager.getName(), hrManager.getStoreBranchId(),
//                    hrManager.getBankDetails().getBankName(),
//                    hrManager.getBankDetails().getAccountNumber(), hrManager.getSalary(),
//                    hrManager.isHRManager(),hrManager.getEmployeeTerms(), -1);
//
//
//            boolean HR_MANAGER = false;
//            boolean SHIFT_MANAGER = false;
//            boolean CASHIER = false;
//            boolean DELIVERY_MAN = false;
//            boolean CLEANING = false;
//            boolean WAREHOUSE_MAN = false;
//            boolean ORDEYLY = false;
//        for (Role currentRole : hrManagerRole) {
//            if (currentRole.equals(Role.HR_MANAGER)) {
//                HR_MANAGER = true;
//            }
//            if (currentRole.equals(Role.SHIFT_MANAGER)) {
//                SHIFT_MANAGER = true;
//            }
//            if (currentRole.equals(Role.CASHIER)) {
//                CASHIER = true;
//            }
//            if (currentRole.equals(Role.DELIVERY_MAN)) {
//                DELIVERY_MAN = true;
//            }
//            if (currentRole.equals(Role.CLEANING)) {
//                CLEANING = true;
//            }
//            if (currentRole.equals(Role.WAREHOUSE_MAN)) {
//                WAREHOUSE_MAN = true;
//            }
//            if (currentRole.equals(Role.ORDEYLY)) {
//                ORDEYLY = true;
//            }
//
//
//        }
//
//            EmployeeToRoleDTO EmployeeToRoleDtO = new EmployeeToRoleDTO(hrManager.getId(), HR_MANAGER, SHIFT_MANAGER, CASHIER, DELIVERY_MAN, CLEANING, WAREHOUSE_MAN, ORDEYLY);
//            try {
//                jdbcEmployeeDAO.insert(DTO);
//                jdbcEmployeeToRoleDAO.insert(EmployeeToRoleDtO);
//            }catch (SQLException e) {
//                e.printStackTrace();
//            }

        //String tel, String contact_name, Area area
        //Branch0
//        branchController.addStoreBranch(0, "Branch0", "Address 0", StoreStatus.OPEN, "tel1", "contact1", Area.NORTH);


        // driver
        List<Role> cashierRole = new ArrayList<>();
        cashierRole.add(Role.DELIVERY_MAN);
        Employee cashier = branchController.getEmployeeController().addEmployee(2, "Delivery", 0, "BankName", 123456789, 10000.0, cashierRole, false, "terms", 10);
        Availability cashierAvailability = new Availability(cashier.getId(), 1, true, true); // Monday, both shifts
        cashier.addAvailability(cashierAvailability);  // Assuming the addAvailability method exists
        cashier.setPassword("pass3");

        List<Role> shiftMan = new ArrayList<>();
        shiftMan.add(Role.SHIFT_MANAGER);
        Employee shiftMana = branchController.getEmployeeController().addEmployee(10, "manager", 0, "BankName", 123456789, 10000.0, shiftMan, false, "terms", -1);

        List<Role> keep = new ArrayList<>();
        keep.add(Role.WAREHOUSE_MAN);
        Employee keeper = branchController.getEmployeeController().addEmployee(13, "manager", 0, "BankName", 123456789, 10000.0, keep, false, "terms", -1);

        List<Role> dr = new ArrayList<>();
        dr.add(Role.DELIVERY_MAN);
        Employee dri = branchController.getEmployeeController().addEmployee(15, "Delivery", 0, "BankName", 123456789, 10000.0, cashierRole, false, "terms", 10);

        //shift
//        LocalDate now = LocalDate.now();
//        Date date= new Date(now.getYear(), now.getMonthValue(), now.getDayOfMonth());
//
//        int selectedShift=branchController.addNewShift(0,date , ShiftType.NIGHT,ShiftStatus.SCHEDULED);
//        branchController.addRoleToShift("Branch0",selectedShift, Role.DELIVERY_MAN);
//        branchController.addEmployeeToShift(10, 0, selectedShift, Role.DELIVERY_MAN);


        //////////////////////////////////////////////////////////////////////////////

        //Branch1
//        branchController.addStoreBranch(1, "Branch1", "Address 1", StoreStatus.OPEN, "tel2", "contact2", Area.SOUTH);

        //WAREHOUSE_MAN
        List<Role> cleaningRole = new ArrayList<>();
        cleaningRole.add(Role.WAREHOUSE_MAN);
        Employee cleaning = branchController.getEmployeeController().addEmployee(3, "Cleaning", 1, "BankName", 123456789, 10000.0, cleaningRole, false, "terms", -1);
        Availability cleaningAvailability = new Availability(cleaning.getId(), 2, true, true); // Monday, both shifts
        Availability cleaningAvailability2 = new Availability(cleaning.getId(), 5, true, true); // Tuesday, both shifts
        cleaning.setPassword("pass4");
        cleaning.addAvailability(cleaningAvailability);  // Assuming the addAvailability method exists
        cleaning.addAvailability(cleaningAvailability2);  // Assuming the addAvailability method exists


        // SHIFT_MANAGER
        List<Role> shiftManagerRole = new ArrayList<>();
        shiftManagerRole.add(Role.SHIFT_MANAGER);
        Employee shiftManager = branchController.getEmployeeController().addEmployee(4, "Shift Manager", 1, "BankName", 123456789, 10000.0, shiftManagerRole, false, "terms", -1);
        Availability shiftManagerAvailability = new Availability(shiftManager.getId(), 2, true, true); // Monday, both shifts
        Availability shiftManagerAvailability2 = new Availability(shiftManager.getId(), 4, true, true); // Tuesday, both shifts
        shiftManager.addAvailability(shiftManagerAvailability);  // Assuming the addAvailability method exists
        shiftManager.addAvailability(shiftManagerAvailability2);  // Assuming the addAvailability method exists


    }

}
