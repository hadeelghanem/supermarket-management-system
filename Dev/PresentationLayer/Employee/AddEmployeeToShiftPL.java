package PresentationLayer.Employee;

import DomainLayer.Employee.Role;
import DomainLayer.Employee.Shift;

import java.util.List;
import java.util.Scanner;

public class AddEmployeeToShiftPL {
    private final StoreControllerPL storesControllerPL;
    private final String branchName;
    private final int selectedShift;
    private final Scanner scanner;

    private boolean forShiftManager;
    private boolean managerFlag;

    public AddEmployeeToShiftPL(StoreControllerPL storeControllerPL, String branchName, int selectedShift) {
        this.storesControllerPL = storeControllerPL;
        this.branchName = branchName;
        this.selectedShift = selectedShift;
        this.scanner = new Scanner(System.in);
        this.forShiftManager=false;

    }

    public void setForShiftManager(boolean flag)
    {
        this.forShiftManager=flag;
    }
    public void addEmployeeToShift() {
        Shift shift = storesControllerPL.getShiftById(branchName, selectedShift);
        if (shift == null) {
            System.out.println("❌ Shift not found.");
            return;
        }
        List<Role> neededRoles = shift.getNeededRoles();
        if (neededRoles.isEmpty()) {
            System.out.println("⚠️ This shift has no roles to assign.");
            return;
        }
        System.out.println("\n🎯 Roles needed for this shift:");
        for (int i = 0; i < neededRoles.size(); i++) {
            System.out.println((i + 1) + ". " + neededRoles.get(i).name());
        }
        System.out.print("Select the number of the role to assign: ");
        String roleInput = scanner.nextLine().trim();
        int roleIndex;
        try {
            roleIndex = Integer.parseInt(roleInput) - 1;
            if (roleIndex < 0 || roleIndex >= neededRoles.size()) {
                System.out.println("❌ Invalid selection.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Please enter a valid number.");
            return;
        }
        //System.out.println("start of tests in addEmployeeToShift in SHiftsPL ");
        Role selectedRole = neededRoles.get(roleIndex);
        //System.out.println("test Selected role: " + selectedRole.name());
        findAndAssignEmployeeToShift(selectedRole);


    }
    private void findAndAssignEmployeeToShift(Role selectedRole) {
        List<Integer> available = storesControllerPL.getAvailableEmployees(branchName, selectedRole, selectedShift);
        List<Integer> notAvailable = storesControllerPL.notAvailabelEmployeesByTimeAndRole(branchName, selectedRole, selectedShift);
        if (available==null || available.isEmpty()) {
            System.out.println("❌ No available employees for :" + selectedRole);

            promoteEmloyeeNotAvailable(notAvailable, selectedRole);
        }
        else
        {
            // 1. Print available employees
            printtList(available,"\n👥 Available employees:");
            // 2. Offer choice: select or show not-available
            System.out.println("\nWhat would you like to do? Choose an option (1-2)");
            System.out.println("1. Select an employee from the above");
            System.out.println("2. Show not-available employees");
            int menuChoice;
            try {
                menuChoice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Returning to previous menu.");
                return;
            }
            int selectedEmployeeId = -1;
            switch (menuChoice) {
                case 1 -> {

                    promoteEmloyee(available, selectedRole);
                }
                case 2 -> {
                    //promoteEmloyee(notAvailable, selectedRole);

                    promoteEmloyeeNotAvailable(notAvailable, selectedRole);

                }
                default -> System.out.println("Unknown option—please enter 1 or 2.");

            }
        }
    }

    private boolean promoteManager(List<Integer> lst, Role selectedRole)
    {
        System.out.print("Enter the ID of the employee you want to assign as manager: ");
        int selectedEmployeeId = -1;
        boolean flag = true;
        while (flag) {
            flag = false;
            //selectedEmployeeId = scanner.nextInt();
            String idString = scanner.next();
            try {
                selectedEmployeeId = Integer.parseInt(idString);
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid ID. Please enter a valid number.");
                flag = true;
                continue;
            }
            if (!flag) {
                if (!lst.contains(selectedEmployeeId)) {
                    System.out.println("❌ Invalid employee ID. Please try again.");
                    flag = true;
                }
            }
        }
        return assign(selectedEmployeeId, branchName, selectedShift, selectedRole);
    }

    private void printtList(List<Integer> lst, String msg)
    {
        if(forShiftManager)
        {
            System.out.println(msg);
            for (int empId : lst) {
                System.out.println("• Employee ID: " + empId);
            }
            return;
        }
        System.out.println(msg);
        for (int i = 0; i < lst.size(); i++) {
            System.out.println((i + 1) + ". Employee ID: " + lst.get(i));
        }
    }

    private boolean assign(int selectedEmployeeId, String branchName, int selectedShift, Role selectedRole) {
        if (storesControllerPL.addEmployeeToShift(selectedEmployeeId, branchName, selectedShift, selectedRole)) {
            System.out.println("✅ Employee ID " + selectedEmployeeId + " assigned to shift with role: " + selectedRole.name());
            this.forShiftManager=true;
            return true;
        } else {
            System.out.println("❌ Failed to assign employee to shift.");
            this.forShiftManager=false;
            return false;
        }
    }

    private boolean promoteEmloyee(List<Integer> lst ,Role selectedRole )
    {
        if(forShiftManager)
        {
            return promoteManager(lst, selectedRole);
        }

        System.out.print("Select the number of the employee to assign: ");
        String employeeInput = scanner.nextLine().trim();
        int employeeIndex;
        try {
            employeeIndex = Integer.parseInt(employeeInput) - 1;
            if (employeeIndex < 0 || employeeIndex >= lst.size()) {
                System.out.println("❌ Invalid selection.");
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Please enter a valid number.");
            return false;
        }
        int selectedEmployeeId = lst.get(employeeIndex);
        return assign(selectedEmployeeId, branchName, selectedShift, selectedRole);


    }
    private boolean promoteEmloyeeNotAvailable(List<Integer> lst ,Role selectedRole)
    {
        if(lst==null || lst.isEmpty())
        {
            //System.out.println("All employees are available!");
            return false;
        }
        printtList(lst, "\n❌ The following employees are NOT available based on their preferences , you can choose from them :");
        return promoteEmloyee(lst, selectedRole);

    }

    public boolean addManager() {
        findAndAssignEmployeeToShift(Role.SHIFT_MANAGER);
        return this.forShiftManager;
    }
}
