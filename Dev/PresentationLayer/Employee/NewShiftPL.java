package PresentationLayer.Employee;

import DomainLayer.Employee.ShiftStatus;
import DomainLayer.Employee.ShiftType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class NewShiftPL {
    private StoreControllerPL storesController;
    private static Scanner scanner;

    public NewShiftPL(StoreControllerPL _storesController)
    {
        this.storesController=_storesController;
        this.scanner = new Scanner(System.in);

    }
    public void addNewShift() {

        String branchName = enterBranchName();
        if (branchName == null) {
            System.out.println("❌ Branch not found.");
            return;
        }
        System.out.println("\n➕ Add New Shift");

        System.out.print("Enter shift date (yyyy-MM-dd): ");
        String dateInput = scanner.nextLine().trim();

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate shiftDate;
        try {
            shiftDate = LocalDate.parse(dateInput, fmt);
        } catch (DateTimeParseException e) {
            System.out.println("❌ Invalid format. Use yyyy-MM-dd (e.g. 2025-05-27).");
            return;
        }

        LocalDate today  = LocalDate.now();
        LocalDate weekFromNow = today.plusDays(7);

        // inclusive check: shiftDate ≥ today && shiftDate ≤ weekFromNow
        if (!shiftDate.isBefore(today) && !shiftDate.isAfter(weekFromNow)) {
            System.out.println("✅ The date is valid ");
            // store shiftDate or continue processing…
        } else {
            System.out.println("The date must be within the next 7 days.");
            return;
        }


        System.out.print("Enter shift type (MORNING/NIGHT): ");
        String typeInput = scanner.nextLine().trim().toUpperCase();
        ShiftType type;
        try {
            type = ShiftType.valueOf(typeInput);
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Invalid shift type.");
            return;
        }

        ShiftStatus status = ShiftStatus.SCHEDULED; // Default status when created


        //extract type
        boolean isMorning = ( type == ShiftType.MORNING);

        boolean check=storesController.check(branchName);
        if(!check)
        {
            System.out.println("❌ No shift manager in this branch.");
            return;
        }
        //end of new code
        int shiftId=storesController.addNewShift(branchName, shiftDate, type, status);
        if(shiftId!=-1)
        {
            //System.out.println("shiftId");
            if(enterShiftManager(branchName,  type,shiftId))
            {
                System.out.println("✅ Shift manager assigned successfully.");
            }
            else
            {
                storesController.cancelShift(branchName, shiftId);
            }
        }
        else
        {
            System.out.println("❌ Failed to add shift. Please try again.");
        }
    }
    private boolean enterShiftManager(String branchName, ShiftType type,int shiftId) {

        AddEmployeeToShiftPL addEmp=new AddEmployeeToShiftPL(storesController,branchName,shiftId);
        addEmp.setForShiftManager(true);
        return  addEmp.addManager();

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
}
