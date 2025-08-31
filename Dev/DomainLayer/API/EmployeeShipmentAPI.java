package DomainLayer.API;

import DomainLayer.Factory;
import DomainLayer.Shipment.Driver;
import DomainLayer.Employee.StoreBranch;
import DomainLayer.Employee.Conrollers.StoresController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class EmployeeShipmentAPI {
   // Called by the Shipment module to update an employee’s
    public EmployeeShipmentAPI() {
        // Constructor
    }
    public List<Driver> getScheduledDrivers(LocalDate date, LocalTime time, int source) {
        return Factory.getInstance().getBranchController().driversInShift(date, time, source);
        //return null;
    }

    public List<String> getStoresNames() {
        return Factory.getInstance().getBranchController().getAllBranchesNames();
    }

    public StoreBranch getStoreByName(String branchName) {
        return Factory.getInstance().getBranchController().getBranchByName(branchName);
    }

    public boolean shiftHasWarehouseKeeper(LocalDate date, LocalTime time, int destination) {
        return Factory.getInstance().getBranchController().shiftHasWarehouseKeeper(date, time, destination);
    }
}
