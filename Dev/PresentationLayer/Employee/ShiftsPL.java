package PresentationLayer.Employee;

import DomainLayer.Employee.Role;
import DomainLayer.Employee.Shift;
import DomainLayer.Employee.ShiftStatus;

import java.util.*;

public class ShiftsPL {
    private  final StoreControllerPL storesControllerPL;
    private static Scanner scanner;
    
    private String branchName;
    private int selectedShift;

    public ShiftsPL(StoreControllerPL storeControllerPL, String branchName) {
        this.storesControllerPL = storeControllerPL;
        scanner = new Scanner(System.in);
        this.branchName = branchName;
        this.selectedShift = -1;
    }

    public void run() {
        this.selectedShift = selectShift();
        if (selectedShift == -1) {
            //System.out.println("❌ No shift selected.");
        } else {
            boolean managing = true;
            while (managing) {
                Shift shift = storesControllerPL.getShiftById(branchName, selectedShift);
                if(shift.getStatus()!= ShiftStatus.SCHEDULED) {
                    System.out.println("❌ shift is not scheduled.");

                    break;
                }
                System.out.println("\n🔧 Managing Shift: " + selectedShift);
                System.out.println("Choose an option:");
                System.out.println("1. Add roles"); //work
                System.out.println("2. Remove roles"); //work
                System.out.println("3. Add employee");
                System.out.println("4. Remove employee");
                System.out.println("5. Cancel shift ");
                System.out.println("6. Back");

                System.out.print("Your choice: ");
                String choice = scanner.nextLine();

                switch (choice) {
                    case "1":
                        addRolesToShift();//
                        break;
                    case "2":
                        removeRolesFromShift();//
                        break;
                    case "3":
                        AddEmployeeToShiftPL addEmp= new AddEmployeeToShiftPL(storesControllerPL, branchName, selectedShift);
                        addEmp.addEmployeeToShift();//
                        break;
                    case "4":
                        removeEmployeeFromShift();//
                        //removeEmployeeFromShift();//
                        break;
                    case "5":
                        if(cancleShift())
                        {
                            managing = false; // Exit the managing loop after cancellation
                        }
                        break;
                    case "6":
                        managing = false;
                        this.selectedShift = -1;
                        System.out.println("🔙 Back to main menu.");
                        break;
                    default:
                        System.out.println("❌ Invalid option. Please try again.");
                }


            }

        }
    }

    private boolean cancleShift() {
        Shift shift = storesControllerPL.getShiftById(branchName, selectedShift);
        if (shift == null) {
            System.out.println("❌ Shift not found.");
            return false;
        }
        if (storesControllerPL.cancelShift(branchName, selectedShift)) {
            System.out.println("✅ Shift " + selectedShift + " has been canceled.");
            return true;
            //this.managing = false; // Exit the managing loop after cancellation
        } else {
            System.out.println("❌ Failed to cancel shift.");
            return false;

        }
    }


    private void removeEmployeeFromShift()
    {
        Shift shift = storesControllerPL.getShiftById(branchName, selectedShift);
        if (shift == null) {
            System.out.println("❌ Shift not found.");
            return;
        }
        HashMap< Integer,Role> emp= shift.getEmployeesRequired();
        if (emp.isEmpty()) {
            System.out.println("⚠️ This shift has no employee to remove.");
            return;
        }
        System.out.println("\n👥 Employees assigned to this shift:");
        List<Map.Entry<Integer, Role>> entries = new ArrayList<>(emp.entrySet());
        for (int i = 0; i < entries.size(); i++) {
            Map.Entry<Integer, Role> entry = entries.get(i);
            System.out.println((i + 1) + ". Employee ID: " + entry.getKey() + " | Role: " + entry.getValue());
        }
        System.out.print("Select the number of the employee to remove: ");
        String employeeInput = scanner.nextLine().trim();
        int employeeIndex;
        try {
            employeeIndex = Integer.parseInt(employeeInput) - 1;
            if (employeeIndex < 0 || employeeIndex >= entries.size()) {
                System.out.println("❌ Invalid selection.");
                return;
            }

            int employeeIdToRemove = entries.get(employeeIndex).getKey();
            //System.out.println("✅ Employee ID " + employeeIdToRemove + " has been removed from the shift.");

        } catch (NumberFormatException e) {
            System.out.println("❌ Please enter a valid number.");
            return;
        }
        
        int selectedEmployeeId = entries.get(employeeIndex).getKey(); // ✅ Correct
        Role selectedRole = entries.get(employeeIndex).getValue(); // ✅ Correct
        if(storesControllerPL.removeEmployeeFromShift(selectedEmployeeId, branchName, selectedShift, selectedRole))

        {
            System.out.println("✅ Employee ID " + selectedEmployeeId + " removed from shift with role: " + selectedRole.name());
        }
        else
        {
            System.out.println("❌ Failed to remove employee from shift.");
        }
        

    }


    private void removeRolesFromShift()
    {
        System.out.println("Removing roles from shift...");
        Role selectedRole = selectRole();
        if (selectedRole != null) {
            if(storesControllerPL.removeRoleFromShift(branchName, selectedShift, selectedRole))
            {
                System.out.println("✅ Role " + selectedRole.name() + " removed from shift.");
            }

        } else {
            System.out.println("❌ Invalid role selection.");
        }

    }

    private void addRolesToShift() {
        System.out.println("Adding roles to shift...");
        Role selectedRole = selectRole();
        if (selectedRole != null) {

            if(storesControllerPL.addRoleToShift(branchName, selectedShift, selectedRole))
            {
                System.out.println("✅ Role " + selectedRole.name() + " added to shift.");
            }

        } else {
            System.out.println("❌ Invalid role selection.");
        }

    }
    private Role selectRole() {
        System.out.println("\n🔧 Available roles:");
        Role[] roles = Role.values();
        for (int i = 0; i < roles.length; i++) {
            System.out.println((i + 1) + ". " + roles[i].name());
        }
        System.out.print("Select a role: ");
        int roleIndex = Integer.parseInt(scanner.nextLine()) - 1;
        if (roleIndex >= 0 && roleIndex < roles.length) {
            return roles[roleIndex];
        } else {
            System.out.println("❌ Invalid role selection.");
            return null;
        }
    }
    private int selectShift() {
        if (branchName != null) {
            List<Shift> shifts = storesControllerPL.getShiftScheduled(branchName);
            if (shifts.isEmpty()) {
                System.out.println("❌ No scheduled shifts for this branch.");
                return -1;
            }
            System.out.println("\n📋 Scheduled Shifts for Branch: " + branchName);
            for (int i = 0; i < shifts.size(); i++) {
                Shift shift = shifts.get(i);
                System.out.println(i+1 +" : " +shift.toString());
            }

            // Optional: let user select a shift
            System.out.print("\nEnter the number of the shift to manage: ");
            try {
                int index = Integer.parseInt(scanner.nextLine()) - 1;
                if (index >= 0 && index < shifts.size()) {
                    Shift selectedShift = shifts.get(index);
                    System.out.println("✅ You selected: \n" + selectedShift);
                    return selectedShift.getId();
                    // Now you can continue to manage this shift...
                } else {
                    System.out.println("❌ Invalid selection.");
                    return -1;
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid input. Please enter a number.");
                return -1;
            }
        } else {
            System.out.println("❌ Branch name is null.");
            return -1;

        }

    }

}
