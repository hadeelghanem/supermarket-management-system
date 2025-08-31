package DomainLayer.Shipment;

import DomainLayer.Employee.StoreBranch;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Transport {
    private int Transport_id;
    private LocalDate Transport_date;
    private LocalTime Departure_time;
    private Truck truck;
    private Driver driver;
    private List<ReportForDes> Des;
    private StoreBranch Src;
    private int Weight;



    public Transport(int transport_id, LocalDate transport_date, LocalTime departure_time, List<ReportForDes> des, StoreBranch src,  int weight) {
        Transport_id = transport_id;
        Transport_date = transport_date;
        Departure_time = departure_time;
        truck = null;
        driver = null;
        Des = des;
        Src = src;
        Weight = weight;
    }

    public int getTransport_id() {
        return Transport_id;
    }

    public LocalDate getTransport_date() {
        return Transport_date;
    }

    public LocalTime getDeparture_time() {
        return Departure_time;
    }

    public List<ReportForDes> getDes() {
        return Des;
    }

    public StoreBranch getSrc() {
        return Src;
    }

    public int getWeight() {
        return Weight;
    }

    public void assignDriver(Driver driver) {
        this.driver = driver;
        //driver.assignToTransport();
    }

    public void assignTruck(Truck truck) {
        this.truck = truck;
        truck.assignToTransport();
    }

    public String DestinationList() {
        StringBuilder sb = new StringBuilder("Destinations: ");
        for (int i = 0; i < Des.size(); i++) {
            sb.append(Des.get(i).getDestination().getAddress());
            if (i < Des.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

}
