package DomainLayer.Shipment;

import DTO.DriverDTO;
import DTO.EmployeeDTO;
import DataAccessLayer.JdbcDriverDAO;
import DataAccessLayer.JdbcEmployeeDAO;
import DomainLayer.API.EmployeeShipmentAPI;
import DomainLayer.Employee.Employee;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DriverManager {
    private List<Driver> availableDrivers;
    private static int DriverId = 0;
    private JdbcDriverDAO jdbcDriverDAO;
    private JdbcEmployeeDAO jdbcEmployeeDAO;

    //new
//    private List<Driver> drivers;


    public DriverManager() {
        availableDrivers = new ArrayList<>();
        jdbcDriverDAO = new JdbcDriverDAO();
        jdbcEmployeeDAO = new JdbcEmployeeDAO();
//            drivers = new ArrayList<>();
    }

//    public void addDriver(Driver driver) {
//        availableDrivers.add(driver);
//        DriverId=DriverId+1;
//    }

//    public void removeDriver(Driver driver) {
//        availableDrivers.remove(driver);
//    }

    public List<Driver> getAvailableDrivers() {
        return availableDrivers;
    }

    public int getDriverId() {
        return DriverId;
    }

    public Driver assignDriver(Truck truck) {
        if (availableDrivers == null || availableDrivers.isEmpty()) {
            System.out.println("No available drivers to assign.");
            return null;
        }
        for (Driver driver : availableDrivers) {
            if (driver.canDrive(truck) && driver.isAvailable()) {
                driver.assignToTransport();
                //remove the driver from shift?
                availableDrivers.remove(driver);
                return driver;
            }
        }
        System.out.println("No available drivers can drive this truck.");
        return null;
    }

    public String availableDriverList() {
        StringBuilder sb = new StringBuilder();
        for (Driver driver : availableDrivers) {
            sb.append("Driver ID: ").append(driver.getId())
                    .append(", Name: ").append(driver.getName())
                    .append(", License Class: ").append(driver.getDriving_Licence())
                    .append("\n");
        }
        return sb.toString();
    }


    public void setAvailableDrivers(List<Driver> drivers) {

        if (drivers == null || drivers.isEmpty()) {
            this.availableDrivers = null;
            return;
        }
        if( this.availableDrivers != null) {
            availableDrivers.clear();
        }
        this.availableDrivers.addAll(drivers);
    }


}