package PresentationLayer.Employee;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Scanner;

public class StartEmployee {
    private static Scanner scanner;

    public void start() {
       // System.out.println("we in StartEmployee line 11 ");
        StoreControllerPL storeControllerPL = new StoreControllerPL();
        StorePL storePL = new StorePL(storeControllerPL);
        LocalDate today = LocalDate.now();
        if (today.getDayOfWeek() == DayOfWeek.THURSDAY)
        {
            System.out.println("🆕 New week has started!");
            System.out.println("👥 For all Employees: Please update your availability.");
            System.out.println("🧑‍💼 Manager: Please assign shifts for this week.");
            storePL.newWeek();
        }

        scanner = new Scanner(System.in);
        boolean isRunning = true;
        while (isRunning) {
            //////test
            //storeControllerPL.test();
            ////end of test
            System.out.println("=====================================");
            System.out.println("Welcome to the Workers Module!");
            System.out.println("=====================================");
            System.out.println("Please select an option:");
            System.out.println("1. Login"); // is working
            System.out.println("2. Register"); // is working
            System.out.println("3.Register hr manager"); // is working
            System.out.println("4. Exit"); // is working
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    storePL.logIn();
                    break;
                case "2":
                    storePL.register();
                    break;
                case "3":
                    storePL.registerHRManager();

                    break;
                case "4":
                    isRunning = false;
                    System.out.println("Exiting the application. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

    }




}
