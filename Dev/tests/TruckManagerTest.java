package tests;

import DomainLayer.Shipment.Truck;
import DomainLayer.Shipment.TruckManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TruckManagerTest {

    TruckManager truckManager = new TruckManager();
    Truck Scania = new Truck(5, "Scania", 1, 1000, 2000);
    Truck Volvo = new Truck(3, "Volvo", 2, 1000, 2000);

    @Test
    void addTruck() {
        truckManager.addTruck(Scania);
        assertTrue(truckManager.getAvailableTrucks().contains(Scania));
    }

    @Test
    void removeTruck() {
        truckManager.addTruck(Scania);
        assertTrue(truckManager.getAvailableTrucks().contains(Scania));
    }


    @Test
    void assignTruck() {
        truckManager.addTruck(Scania);
        Truck assignedTruck = truckManager.assignTruck(1500);
        assertEquals(assignedTruck, Scania);
    }

}