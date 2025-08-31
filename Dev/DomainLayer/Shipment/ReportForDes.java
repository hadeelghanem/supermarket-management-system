package DomainLayer.Shipment;

import DomainLayer.Employee.StoreBranch;
import DomainLayer.Shipment.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReportForDes {
    private StoreBranch destination;
    private int weight;
    private List<Product> ProductsForDes;

    private int ReportId;




    public ReportForDes(int ReportId, StoreBranch destination, List<Product> ProductsForDes) {
        this.destination = destination;
        this.ProductsForDes = ProductsForDes;
        weight = weightForDes();
        this.ReportId = ReportId;
    }

    public List<Product> getProductsForDes() {
        return ProductsForDes;

    }

    public int weightForDes() {
        int sum = 0;
        for (Product temp : ProductsForDes) {
            sum = sum + (temp.getWeight() * temp.getQuantity());
        }
        return sum;
    }

    public StoreBranch getDestination() {
        return destination;
    }

    public void deleteProducts(List<Product> productsToDelete) {
        for (Product product : productsToDelete) {
                ProductsForDes.remove(product);
        }
    }

    public String PrintProducts() {
        StringBuilder sb = new StringBuilder();
        for (Product product : ProductsForDes) {
            sb.append("Type: ").append(product.getType())
                    .append(", Weight: ").append(product.getWeight())
                    .append(", Quantity: ").append(product.getQuantity())
                    .append("\n");
        }
        return sb.toString();
    }


    public List<Product> parseProducts(String productsString) {
        List<Product> products = new ArrayList<>();
        if (productsString == null || productsString.trim().isEmpty()) {
            return products;
        }

        // one regex to pull out Type, Weight and Quantity
        Pattern p = Pattern.compile("^Type: (.*), Weight: ([0-9]+(?:\\.[0-9]+)?), Quantity: (\\d+)$");
        for (String line : productsString.split("\\r?\\n")) {
            Matcher m = p.matcher(line.trim());
            if (!m.matches()) {
                // skip bad lines (or throw new IllegalArgumentException("Bad format: " + line));
                continue;
            }

            Product prod = new Product();
            prod.setType(m.group(1));
            prod.setWeight(Integer.parseInt(m.group(2)));
            prod.setQuantity(Integer.parseInt(m.group(3)));
            products.add(prod);
        }
        return products;
    }

    public int getReportId() {
        return ReportId;
    }
}