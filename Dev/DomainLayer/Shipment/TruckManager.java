package DomainLayer.Shipment;

import DTO.TruckDTO;
import DataAccessLayer.JdbcTruckDAO;
import DomainLayer.Shipment.Truck;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TruckManager {
    private List<Truck> availableTruckList;
    private  static int TruckId = 0;
    private  JdbcTruckDAO jdbcTruckDAO ;




    public TruckManager() {
        availableTruckList = new ArrayList<>();
        jdbcTruckDAO = new JdbcTruckDAO();
    }

    public void addTruck(Truck newTruck){
        availableTruckList.add(newTruck);

        TruckId=TruckId+1;
    }

    public int getTruckId() {
        return TruckId;
    }

    public void removeTruck(Truck truckToUse) {
        availableTruckList.remove(truckToUse);
    }

    public List<Truck> getAvailableTrucks() {
        return availableTruckList;
    }

    public Truck assignTruck(int totalWeight) {
        for (Truck truck : availableTruckList) {
            if (truck.canCarry(totalWeight) && truck.isAvailable()) {
                truck.assignToTransport();
                availableTruckList.remove(truck);
                return truck;
            }
        }
        System.out.println("No available trucks can carry this weight.");
        return null;
    }

    public void releaseTruck(Truck truck) {
        truck.releaseFromTransport();
        if (!availableTruckList.contains(truck)) {
            availableTruckList.add(truck);
        }
    }

    public String availableTruckList() {
        StringBuilder sb = new StringBuilder();
        for (Truck truck : availableTruckList) {
            sb.append("Truck ID: ").append(truck.getId_number())
                    .append(", Model: ").append(truck.getModel())
                    .append(", License Number: ").append(truck.getLicense_number())
                    .append(", Net Weight: ").append(truck.getNet_Weight())
                    .append(", Maximum Weight: ").append(truck.getMaximum_weight())
                    .append("\n");
        }
        return sb.toString();
    }

    public void LoadTrucksFromDB() {
        List<TruckDTO> trucksFromDB = null;
        try {
            trucksFromDB = jdbcTruckDAO.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (TruckDTO truck : trucksFromDB) {
            Truck newTruck = new Truck(truck.License_number(), truck.Model(), truck.Id_number(), truck.Net_Weight(), truck.Maximum_Weight());
            availableTruckList.add(newTruck);
        }
    }


}
