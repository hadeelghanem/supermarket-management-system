package PresentationLayer.Employee;

import DomainLayer.Employee.Employee;
import DomainLayer.Employee.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AddEmployePL {

    private Scanner scanner;
    private final StoreControllerPL storesController;
    public AddEmployePL(StoreControllerPL storesController) {
        this.storesController = storesController;
        this.scanner = new Scanner(System.in);
    }
    public void addEmployee() {
        System.out.println("\n--- Add New Employee ---");
//        List<Role> qualifications =enterQualifications();
        int id = enterID();
        String name = enterName();
        int storeBranch = enterStoreBranch();
        String bankName = enterBankName();
        int accountNumber = enterAccountNumber();
        double salary = enterSalary();
        List<Role> qualifications = enterQualifications();
        //check if driver
        int license = -1;
        if(qualifications.contains(Role.DELIVERY_MAN))
        {
            license =promptForInt("Enter License Class: ");
        }
        String terms = enterTerms();
        boolean isHRManager = false;
        if(!storesController.addEmployee(id, name, storeBranch, bankName, accountNumber, salary, qualifications, isHRManager,terms,license) )
        {
            System.out.println("❌  Please try again.");
        }
        else
        {
            System.out.println("✅ Employee added successfully");

        }

    }

    private String enterTerms() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter  employee terms: ");
            String bank = scanner.nextLine().trim();
            if (!bank.isEmpty()) {
                return bank;
            }
            System.out.println("cannot be empty. Please try again.");
        }
    }

    private int enterStoreBranch() {
        scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter store branch ID: ");
            try {
                int branch = Integer.parseInt(scanner.nextLine());
                if (branch >= 0) {
                    return branch;
                }
            } catch (NumberFormatException ignored) {}
            System.out.println("Invalid branch ID. Please enter a positive number.");
        }
    }
    private int enterID()
    {  boolean flag = true;
        int id = -1;
        System.out.println("Please enter  ID:");
        while (flag) {
            flag = false;
            //id = scanner.nextInt();
            String idString = scanner.next();
            try {
                id = Integer.parseInt(idString);
            } catch (NumberFormatException e) {
                System.out.println("Invalid ID. Please enter a valid number.");
                flag = true;
                continue;
            }
            if (id < 0) {
                System.out.println("Invalid ID. Please try again.");
                flag = true;
            }
        }
        return id;
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

    private double enterSalary() {
        scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter salary: ");
            try {
                double salary = Double.parseDouble(scanner.nextLine());
                if (salary > 0) {
                    return salary;
                }
            } catch (NumberFormatException ignored) {}
            System.out.println("Invalid salary. Please enter a positive number.");
        }
    }
    private List<Role> enterQualifications() {
        List<Role> roles = new ArrayList<>();
        System.out.println("\nPlease answer for each qualification (yes/no):");

        for (Role role : Role.values()) {
            if (role == Role.HR_MANAGER) continue; // ✅ skip HR_MANAGER completely

            while (true) {
                System.out.print("Is he a " + role.name().replace("_", " ").toLowerCase() + "? (yes/no): ");
                String input = scanner.nextLine().trim().toLowerCase();

                if (input.equals("yes")) {
                    roles.add(role);
                    break;
                } else if (input.equals("no")) {
                    break;
                } else {
                    System.out.println("Invalid input. Please type 'yes' or 'no'.");
                }
            }
        }

        if (roles.isEmpty()) {
            System.out.println("⚠ You must select at least one qualification. Restarting role selection...");
            return enterQualifications();
        }

        return roles;
    }


    private String enterName() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter employee name: ");
            String name = scanner.nextLine().trim();
            if (!name.isEmpty()) {
                return name;
            }
            System.out.println("Name cannot be empty. Please try again.");
        }
    }
    private int promptForInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please try again.");
            }
        }
    }
    public void addHr()
    {
        int id = enterID();
        String name = enterName();
//        int storeBranch = enterStoreBranch();
        String bankName = enterBankName();
        int accountNumber = enterAccountNumber();
        double salary = enterSalary();
        List<Role> qualifications = new ArrayList<>();
        qualifications.add(Role.HR_MANAGER); // HR Manager role is fixed for this method
        //check if driver
        int license = -1;
        String terms = enterTerms();
        boolean isHRManager = true;
        Employee s = storesController.getEmployeeController().addEmployee(id, name, -1, bankName, accountNumber, salary, qualifications, isHRManager,terms,license) ;
        String password = enterPassword();

        if(storesController.register(id, password))
        {

            System.out.println("✅ Employee added successfully");
        }
        else
        {
            System.out.println("❌  Please try again.");
        }

    }

    private String enterPassword()
    {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter password: ");
            String password = scanner.nextLine().trim();
            if (!password.isEmpty()) {
                return password;
            }
            System.out.println("Password cannot be empty. Please try again.");
        }
    }

}
