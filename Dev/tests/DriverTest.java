package tests;

import DomainLayer.Employee.BankAccount;
import DomainLayer.Employee.Role;
import DomainLayer.Shipment.Driver;
import DomainLayer.Shipment.Truck;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DriverTest {

//    Driver Abed = new Driver(1,"Abed", 5);
    BankAccount abedBank = new BankAccount("Bank Hapoalim", 111, 1234567, "Abed");
    List<Role> abedQualifications = Arrays.asList(Role.DELIVERY_MAN, Role.CLEANING);
    Driver Abed = new Driver(5, 1, "Abed", 100, abedBank, 5200.0, abedQualifications, false, "Full-time");
    Truck Scania = new Truck(5, "Scania", 1, 1000, 2000);
    Truck Volvo = new Truck(6, "Volvo", 1, 1000, 2000);
    Truck Daf = new Truck(3,"Daf", 1, 1000, 2000);

    @Test
    void canDriveSameLicense() {
        assertTrue(Abed.canDrive(Scania));
    }

    @Test
    void canDriveBiggerLicense() {
        assertFalse(Abed.canDrive(Volvo));
    }

    @Test
    void canDriveSmallerLicense() {
        assertTrue(Abed.canDrive(Daf));
    }


}