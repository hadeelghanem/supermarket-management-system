package PresentationLayer.Shipment;

import DTO.PendingReportsDTO;
import DTO.TruckDTO;
import DataAccessLayer.*;
import DomainLayer.*;
import DomainLayer.Employee.StoreBranch;
import DomainLayer.Shipment.*;
import PresentationLayer.Shipment.ReportPL;

import java.sql.SQLException;
import java.util.*;

public class TransportSystemUI {
    private Scanner scanner;
    private final TransportManager transportManager;

    private JdbcTruckDAO jdbcTruckDAO;
     private JdbcPendingReportsDAO jdbcPendingReportsDAO;



     //private/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public TransportSystemUI() {

        jdbcTruckDAO = new JdbcTruckDAO();
        jdbcPendingReportsDAO = new JdbcPendingReportsDAO();
        scanner = new Scanner(System.in);
        transportManager = Factory.getInstance().getTransportManager();
        enterSource();

    }

    private void enterSource()
    {
        List<String> allBranches  = transportManager.getAllBranchesNames(true);
        if (allBranches.isEmpty()) {
            System.out.println("❌ No branches available.");
            return ;
        }
        System.out.println("\n=== Select Source Store Branch ===");
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
        this.transportManager.setSource(branchName);

    }

    public void start() {
        if(this.transportManager.getsource()==null)
        {
            //System.out.println("there are no branches ");
            return;
        }
        while (true) {
            this.transportManager.setDrivers();
            System.out.println(" Transport Management System");
            // System.out.println("1. Add Driver"); removed
            System.out.println("1. Add Truck");
            System.out.println("2. Create Report");
            System.out.println("3. Dispatch Deliveries");
            System.out.println("4. View Pending Reports");
            System.out.println("5. View Complete transport");
            System.out.println("6. Print Available Drivers List");
            System.out.println("7. Print Available Trucks List");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                switch (choice) {
                    case 1 -> addTruckUI();          // was case 2
                    case 2 -> createReportUI();      // was case 3
                    case 3 -> dispatchUI();          // was case 4
                    case 4 -> viewPending();         // was case 5
                    case 5 -> viewCompleted();       // was case 6
                    case 6 -> viewDrivers();         // was case 7
                    case 7 -> viewTrucks();          // was case 8
                    case 0 -> {
                        System.out.println("Exiting...");
                        return;
                    }
                    default -> System.out.println("Invalid option. Please enter 0-5.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private void viewTrucks() {
        List<Truck> trucks = transportManager.getTruckManager().getAvailableTrucks();
        if (trucks.isEmpty()) {
            System.out.println("No available trucks.");
        } else {
            System.out.println("Available Trucks:");
            System.out.println(transportManager.getTruckManager().availableTruckList());
            }
    }

    private void viewDrivers() {
        List<Driver> drivers = transportManager.getDriverManager().getAvailableDrivers();
        if (drivers==null || drivers.isEmpty()) {
            System.out.println("No available drivers.");
        } else {
            System.out.println("Available Drivers:");
            for (Driver driver : drivers) {
                if(driver.isAvailable())
                {
                    System.out.println("-Driver ID: " + driver.getId() + " ,Name: " + driver.getName() + " ,License: " + driver.getLicense());
                }
            }
        }
    }

    private void viewCompleted() {
    List<Transport> completed = transportManager.getCompletedTransports();
        if (completed.isEmpty()) {
            System.out.println("No completed reports.");
        } else {
            System.out.println("Completed Reports:");
            for (Transport transport : completed) {
                System.out.println("Transport ID: " + transport.getTransport_id() + " Date: " + transport.getTransport_date() + " Departure Time: " + transport.getDeparture_time() + " Source: " + transport.getSrc().getAddress() + " "+transport.DestinationList());
            }
        }
    }


    private void addTruckUI() {
        int license = promptForInt("Enter License Number: ");
        String model;
        do {
            System.out.print("Enter Model: ");
            model = scanner.nextLine().trim();
            if (model.isEmpty()) {
                System.out.println("Model name cannot be empty. Please enter a valid model.");
            }
        } while (model.isEmpty());
        int net = promptForInt("Enter Net Weight: ");
        int max = promptForInt("Enter Max Weight: ");
        Truck truck = new Truck(license, model, transportManager.getTruckManager().getTruckId(), net, max);
        transportManager.getTruckManager().addTruck(truck);
        TruckDTO truckDTO = new TruckDTO(truck.getId_number(), model, license, net, max);
        try {
            jdbcTruckDAO.insert(truckDTO);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Truck added.");
    }

    private void createReportUI() {
        ReportPL reportPL = new ReportPL();
        StoreBranch destination = DestinationOptions();
        if (destination == null) {
            System.out.println("❌ No branches available.");
            return;
        }
        ReportForDes report= reportPL.run(destination);
        if (report.getDestination().getAddress().equals("Address 1")) {
            PendingReportsDTO pendingReportsDTO = new PendingReportsDTO(
                    "Branch1",
                    report.weightForDes(),
                    report.PrintProducts(),
                    report.getReportId()
            );
            try {
                jdbcPendingReportsDAO.insert(pendingReportsDTO);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        if (report.getDestination().getAddress().equals("Address 0")) {
            PendingReportsDTO pendingReportsDTO = new PendingReportsDTO(
                    "Branch0",
                    report.weightForDes(),
                    report.PrintProducts(),
                    report.getReportId()
            );
            try {
                jdbcPendingReportsDAO.insert(pendingReportsDTO);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        transportManager.addReport(report);
        System.out.println("Report added to pending.");

    }
    private StoreBranch DestinationOptions() {
        List<String> allBranches = transportManager.getAllBranchesNames(false);
        if (allBranches.isEmpty()) {
            System.out.println("❌ No branches available.");
            return null;
        }
        System.out.println("\n=== Select Destination Store Branch ===");
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
        StoreBranch destination = transportManager.getStore(branchName);
        return destination;

    }

    private void dispatchUI() {
        if (transportManager.getPendingReports().isEmpty()) {
            System.out.println("No transport to dispatch.");
        } else {
            transportManager.manageAllDeliveries();

            //transportManager.manageAllDeliveries();
        }
    }

    private void viewPending() {
        List<ReportForDes> pending = transportManager.getPendingReports();
        if (pending.isEmpty()) {
            System.out.println("No pending reports.");
        } else {
            System.out.println("Pending Reports:");
            for (ReportForDes report : pending) {
                System.out.println("Printing products. Size = " + report.getProductsForDes().size());

                System.out.println("Area: " + report.getDestination().getArea() + " Destination: " + report.getDestination().getAddress() + " Products: " + report.PrintProducts());
            }
        }
    }

    // Helper for getting safe integer input
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
}
