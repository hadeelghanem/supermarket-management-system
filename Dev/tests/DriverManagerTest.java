package tests;

import DomainLayer.Employee.BankAccount;
import DomainLayer.Employee.Role;
import DomainLayer.Shipment.Driver;
import DomainLayer.Shipment.DriverManager;
import DomainLayer.Shipment.Truck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
class DriverManagerTest {



    private DriverManager manager;
    private Driver driverA;
    private Driver driverB;
    private Truck truckLow;
    private Truck truckHigh;

    @BeforeEach
    public void setUp() {
        manager = new DriverManager();

        BankAccount acctA = new BankAccount("BankA", 10, 1000, "Alice");
        driverA = new Driver(
                3,              // drivingLicense
                1,              // id
                "Alice",        // name
                101,            // storeBranch
                acctA,          // bankDetails
                4000.0,         // salary
                new ArrayList<>(), // qualifications
                false,          // isHRManager
                "Full-time"     // employeeTerms
        );

        BankAccount acctB = new BankAccount("BankB", 20, 2000, "Bob");
        driverB = new Driver(
                5,              // drivingLicense
                2,              // id
                "Bob",          // name
                102,            // storeBranch
                acctB,          // bankDetails
                4500.0,         // salary
                new ArrayList<>(), // qualifications
                false,          // isHRManager
                "Part-time"     // employeeTerms
        );

        // Truck that requires license ≥ 2
        truckLow = new Truck(2, "TruckLow", 501, 1000, 1500);
        // Truck that requires license ≥ 6
        truckHigh = new Truck(6, "TruckHigh", 502, 1500, 2000);
    }

    //
    @Test
    public void testGetAvailableDriversInitiallyEmpty() {
        assertEquals(0, manager.getAvailableDrivers().size());
    }

    @Test
    public void testSetAvailableDriversAddsDriver() {
        List<Driver> list = new ArrayList<>();
        list.add(driverA);
        manager.setAvailableDrivers(list);
        assertTrue(manager.getAvailableDrivers().contains(driverA));
    }

    @Test
    public void testSetAvailableDriversWithEmptyListClears() {
        manager.getAvailableDrivers().add(driverA);
        manager.setAvailableDrivers(new ArrayList<>());
        assertEquals(0, manager.getAvailableDrivers().size());
    }

    @Test
    public void testGetDriverIdIsZero() {
        assertEquals(0, manager.getDriverId());
    }

    @Test
    public void testAssignDriverReturnsEligibleDriver() {
        manager.setAvailableDrivers(List.of(driverA, driverB));
        assertEquals(driverA, manager.assignDriver(truckLow));
    }

    @Test
    public void testAssignDriverRemovesDriverFromList() {
        manager.setAvailableDrivers(List.of(driverA));
        manager.assignDriver(truckLow);
        assertFalse(manager.getAvailableDrivers().contains(driverA));
    }

    @Test
    public void testAssignDriverReturnsNullWhenNoneEligible() {
        manager.setAvailableDrivers(List.of(driverA));
        assertNull(manager.assignDriver(truckHigh));
    }

    @Test
    public void testAvailableDriverListEmpty() {
        assertEquals("", manager.availableDriverList());
    }

    @Test
    public void testAssignDriverDoesNotThatCantDrive1() {
        manager.getAvailableDrivers().add(driverA);
        Driver result = manager.assignDriver(truckHigh);
        assertNull(result);
    }
    public void testAssignDriverDoesNotThatCantDrive2() {
        manager.getAvailableDrivers().add(driverA);
        Driver result = manager.assignDriver(truckHigh);
        assertTrue(manager.getAvailableDrivers().contains(driverA));
    }



}