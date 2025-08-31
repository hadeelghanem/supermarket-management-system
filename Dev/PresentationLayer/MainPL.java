package PresentationLayer;

import DomainLayer.Employee.StoreBranch;
import DomainLayer.Factory;
import DomainLayer.Shipment.ReportForDes;
import PresentationLayer.Employee.StartEmployee;
import PresentationLayer.Shipment.TransportSystemUI;

import java.io.*;
import java.util.List;
import java.util.Scanner;

//import static PresentationLayer.EmployeePL.scanner;

public class MainPL {

    public static void main(String[] args) {
        MainPL mainPL = new MainPL();
        if( mainPL.start())
        {
            mainPL.run();
        }

    }

    private File extractDatabaseFromJar() throws IOException {
        InputStream input = getClass().getResourceAsStream("/db/mydb.db");
        if (input == null) {
            throw new FileNotFoundException("Database file not found in JAR!");
        }

        File tempDb = File.createTempFile("mydb", ".db");
        tempDb.deleteOnExit(); // optional: deletes file when JVM exits

        try (OutputStream out = new FileOutputStream(tempDb)) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = input.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
        }

        return tempDb;
    }

    public boolean start() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                // Prompt the user
                System.out.print("Do you want to load data? (yes/no): ");
                String answer = scanner.nextLine().trim().toLowerCase();

                // Check the response
                if (answer.equals("yes")) {
                    System.out.println("Loading data...");
                    loadData();
                    return true;
                } else if (answer.equals("no")) {
                    System.out.println("Skipping data load.");
                    return true;
                } else {
                    System.out.println("Invalid input! Please enter 'yes' or 'no' only.");
                    System.out.println("Please try again.\n");
                    // Loop continues, asking for input again
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " );e.printStackTrace();
                System.out.println("An error occurred while reading input. Please try again.");
                scanner.nextLine(); // Clear the buffer
            }
        }
    }




    private static void loadData() {
        // Your data-loading logic goes here
        System.out.println("Data has been loaded successfully!");
        Factory.getInstance().getBranchController().getEmployeeController().LoadEmployeesFromDB();
        StoreBranch Branch0 =Factory.getInstance().getBranchController().getStoreBranch(0);
       StoreBranch Branch1 =Factory.getInstance().getBranchController().getStoreBranch(1);
        Branch0.LoadShiftsFromDB();
        Branch1.LoadShiftsFromDB();
        Factory.getInstance().getTransportManager().LoadPendingReportsDTO();
        Factory.getInstance().getTransportManager().getTruckManager().LoadTrucksFromDB();
        Factory.getInstance().getTransportManager().LoadAvailableDriversFromDB();

        if (!Branch0.getShiftScheduled().isEmpty()) {
            int ShiftIDForBranch0 = Branch0.getShiftScheduled().get(0).getId();

            Branch0.getShiftScheduled().get(0).LoadDriversAssignedFromDB(ShiftIDForBranch0, 0);
           Branch0.getShiftScheduled().get(0).LoadWharehouseAssignedFromDB(ShiftIDForBranch0,0);
        }
       if (!Branch1.getShiftScheduled().isEmpty()) {
           int ShiftIDForBranch1 = Branch1.getShiftScheduled().get(0).getId();
            Branch1.getShiftScheduled().get(0).LoadDriversAssignedFromDB(ShiftIDForBranch1, 1);
            Branch1.getShiftScheduled().get(0).LoadWharehouseAssignedFromDB(ShiftIDForBranch1, 1);
        }
        /// new:
        Factory.getInstance().getBranchController().loadShiftsIdCounter();
        //System.out.println("not implemented yet");
    }

    public void run() {
        boolean running = true;
        Scanner scanner = new Scanner(System.in);

        while (running)
        {
            System.out.println("\n=== Central Transport & Workforce System ===");
            System.out.println("1. Shipment Management");
            System.out.println("2. Workers Management");
            System.out.println("0. Exit");
            System.out.print("Select module: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> {
                    // hand off to your existing shipment UI
                    new TransportSystemUI().start();
                }
                case "2" -> {
                    // hand off to your existing employee UI
                    //System.out.println("we in main PL line 71");
                    new StartEmployee().start();
                }
                case "0" -> {
                    System.out.println("Shutting down. Goodbye!");
                    running = false;
                }
                default -> System.out.println("Unknown option, please try again.");
            }
        }
        scanner.close();
    }


}
