package PresentationLayer.Shipment;

import DomainLayer.Shipment.Product;
import DomainLayer.Shipment.ReportForDes;
import DomainLayer.Employee.StoreBranch;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReportPL {

    private Scanner scanner;
    private static int employeeId = 1; // Assuming a static employee ID for simplicity
    public ReportPL()
    {
         scanner = new Scanner(System.in);

    }
    public ReportForDes run(StoreBranch destination)
    {
        //StoreBranch destination =DestinationOptions();
        List<Product> products = new ArrayList<>();
        int pCount = promptForInt("How many products? ");
        for (int i = 0; i < pCount; i++) {
            String type;
            do {
                System.out.print((i + 1) + ") Product type: ");
                type = scanner.nextLine().trim();
                if (type.isEmpty()) {
                    System.out.println("Product type cannot be empty.");
                }
            } while (type.isEmpty());
            int weight = promptForInt((i + 1) + ") Weight: ");
            int quantity = promptForInt((i + 1) + ") Quantity: ");
            products.add(new Product(weight, type, quantity));
            System.out.println("----");
        }
        ReportForDes report = new ReportForDes(employeeId,destination, products);
        employeeId++;
        return report;


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


}
