package PresentationLayer.Employee;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Scanner;

public class EmployeePL {
    private  final StoreControllerPL storesController;
    private static Scanner scanner;

    public EmployeePL(StoreControllerPL storeControllerPL) {
        this.storesController = storeControllerPL;
        scanner = new Scanner(System.in);
    }

    public void employeeMenu() {
        boolean isRunning = true;

        while (isRunning) {
            System.out.println("\n=== Employee Menu ===");
            System.out.println("Choose an option:");
            System.out.println("1. Enter when you are available to work");
            System.out.println("2. View scheduled shifts");
            System.out.println("3. Edit bank account");
            System.out.println("4. Logout");
            System.out.print("Your choice: ");
            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    enterAvailability();
                    break;
                case "2":
                    viewScheduledShifts();
                    break;
                case "3":
                    editBankAccount();
                    break;

                case "4":
                    System.out.println("Logging out...");
                    isRunning = false;
                    logOut();
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 5.");
            }
        }
    }



    private void editBankAccount() {
        String bankName = enterBankName();
        int accountNumber = enterAccountNumber();
        if(storesController.editBankAccount( bankName, accountNumber))
        {
            System.out.println("Bank account updated successfully.");
        }
        else
        {
            System.out.println("Failed to update bank account. Please try again.");
        }

    }
    private String enterBankName() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter bank name: ");
            String bank = scanner.nextLine().trim();
            if (!bank.isEmpty()) {
                return bank;
            }
            System.out.println("Bank name cannot be empty. Please try again.");
        }
    }

    private int enterAccountNumber() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter bank account number: ");
            try {
                int acc = Integer.parseInt(scanner.nextLine());
                if (acc > 0) {
                    return acc;
                }
            } catch (NumberFormatException ignored) {}
            System.out.println("Invalid account number. Please enter a positive number.");
        }
    }

    private void viewScheduledShifts()
    {
        storesController.viewScheduledShifts();

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

    private void enterAvailability() {
        DayOfWeek today = LocalDate.now().getDayOfWeek();
        if (today != DayOfWeek.THURSDAY) {
            System.out.println("⛔ You can only submit availability only on Thursdays. Today is " + today + ".");
            return;
        }
        System.out.println("\n📅 Today is Thursday! You may now enter your availability.");
        System.out.println("----------------------------------------------------------");
        int day = 0;
        while (true) {
            System.out.print("Enter the day you're available (1 = Sunday, ..., 7 = Saturday): ");
            String input = scanner.nextLine().trim();
            try {
                int dayNumber = Integer.parseInt(input);
                if (dayNumber >= 1 && dayNumber <= 7) {
                    day = dayNumber;
                    break;
                }
            } catch (NumberFormatException ignored) {}
            System.out.println("❌ Invalid input. Please enter a number between 1 and 7.");
        }
        // Ask for morning availability
        boolean availableMorning = askYesNo("Are you available in the morning? (yes/no): ");
        // Ask for evening availability
        boolean availableEvening = askYesNo("Are you available in the evening? (yes/no): ");
        // Summary
        if(storesController.addAvailability(day, availableMorning, availableEvening))
        {
            System.out.println("\n✅ Availability submitted:");
            System.out.println("🗓 Day: " + day);
            System.out.println("☀ Morning: " + (availableMorning ? "Available" : "Not Available"));
            System.out.println("🌙 Evening: " + (availableEvening ? "Available" : "Not Available"));
        }
        else {
            System.out.println("❌ Failed to submit availability. Please try again.");
        }
    }
    private boolean askYesNo(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("yes")) return true;
            if (input.equals("no")) return false;
            System.out.println("❌ Please answer 'yes' or 'no'.");
        }
    }




    
}
