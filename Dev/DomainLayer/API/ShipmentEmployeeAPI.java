package DomainLayer.API;

import DomainLayer.Employee.ShiftType;
import DomainLayer.Employee.StoreBranch;
import DomainLayer.Factory;
import DomainLayer.Shipment.TransportManager;

import java.time.LocalDate;
import java.util.Date;

public class ShipmentEmployeeAPI {
    //Called by the Employee module

    public ShipmentEmployeeAPI() {
        // Constructor
    }

    public boolean shipmentExists(StoreBranch storeBranch, LocalDate date, ShiftType type, boolean isDriver) {
        return Factory.getInstance().getTransportManager().shipmentExists(storeBranch, date, type,isDriver);
    }
}
