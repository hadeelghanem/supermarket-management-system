package PresentationLayer.Employee;

import java.util.List;
import java.util.Scanner;


public class ManagerPL {
    private  final StoreControllerPL storesController;
    private static Scanner scanner;
    public ManagerPL(StoreControllerPL storeControllerPL) {
        this.storesController = storeControllerPL;
        scanner = new Scanner(System.in);
    }
    public   void managerMenu() {
        boolean isRunning = true;
        while (isRunning) {
            scanner = new Scanner(System.in);
            System.out.println("\n=== Manager Menu ===");
            System.out.println("Please select an option:");
            System.out.println("1. Add Employee"); //working
            System.out.println("2. Manage Scheduled Shifts");
            System.out.println("3. View Old Shifts"); //working
            System.out.println("4. View Next Shifts"); //working
            System.out.println("5. Edit Branch Status"); //working
            System.out.println("6. Add new Shift"); //working check more edge cases
            System.out.println("7. Log Out"); //working
            int choice;
            try {
                System.out.print("Your choice: ");
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }
            switch (choice) {
                case 1:
                    //addEmployee();//done
                    AddEmployePL addEmployePL = new AddEmployePL(storesController);
                    addEmployePL.addEmployee();
                    break;
                case 2:
                    manageShifts();
                    break;
                case 3:
                    viewOldShifts(); //
                    break;
                case 4:
                    viewNextShifts(); //
                    break;
                case 5:
                    editBranchStatus();//done
                    break;
                case 6:
                    NewShiftPL sh=new NewShiftPL(storesController);
                    sh.addNewShift();
                    break;
                case 7:
                    isRunning = false;
                    System.out.println("Logging out...");
                    logOut();//done

                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void logOut() {
        int id = storesController.getEmployeeLoggedIn().getId();
        if(storesController.logout(id))
        {
            System.out.println("Logged out successfully.");
        }
        else
        {
            System.out.println("Failed to log out. Please try again.");
        }
    }



    private void viewOldShifts() {
        String branchName = enterBranchName();
        if(branchName != null)
        {
            storesController.viewOldShifts(branchName);
        }
    }

    private void viewNextShifts()
    {
       String branchName = enterBranchName();
       if(branchName != null)
       {
           storesController.viewScheduledShifts(branchName);
       }

    }
    private String enterBranchName() {
        System.out.println("\n🏢 Branch Names:");
        List<String> allBranches  = storesController.getAllBranchesNames();
        if (allBranches.isEmpty()) {
            System.out.println("❌ No branches available.");
            return null;
        }
        for (String branchName : allBranches) {
            System.out.println("• " + branchName);
        }
        System.out.print("Enter the name of the branch you want to view: ");
        String branchName = scanner.nextLine().trim();
        while (!allBranches.contains(branchName)) {
            System.out.println("❌ Invalid branch name. Please try again.");
            System.out.print("Enter the name of the branch you want to view: ");
            branchName = scanner.nextLine().trim();
        }
        return branchName;

    }
    private void editBranchStatus() {
        String name=enterBranchName();
        if(storesController.getBranchStatus(name)==null)
        {
            System.out.println("❌ Branch not found.");
            return;
        }
        System.out.println("📌 Current status: " + storesController.getBranchStatus(name));
        System.out.println("Please enter the new status (OPEN/CLOSED):");
        String status = scanner.nextLine().trim().toUpperCase();
        if (!status.equals("OPEN") && !status.equals("CLOSED")) {
            System.out.println("❌ Invalid status. You must enter either OPEN or CLOSED (all caps).");
            return;
        }
        if (storesController.editBranchStatus(name, status)) { // Make sure method accepts branch name & status
            System.out.println("✅ Branch status updated successfully.");
        } else {
            System.out.println("❌ Failed to update branch status. Please try again.");
        }
    }

    private void manageShifts() {
        System.out.println("\n📋 === Manage Shifts ===");
        String branchName = enterBranchName();
        if (branchName == null) {
            System.out.println("❌ Branch not found.");
            return;
        }
        ShiftsPL shiftsPL = new ShiftsPL(storesController, branchName);
        shiftsPL.run();

    }


}
