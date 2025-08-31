package tests;

//import Availability;
//import BankAccount;
import DomainLayer.Employee.Availability;
import DomainLayer.Employee.BankAccount;
import DomainLayer.Employee.Employee;
import DomainLayer.Employee.Role;
//import org.junit.Test;
//import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.List;

//import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmployeeTest {
    private Employee employee;


    @BeforeEach
    public void setUp() throws Exception {
        BankAccount account = new BankAccount("Leumi", 123, 456789, "Ali");
        List<Role> qualifications = new ArrayList<>();
        qualifications.add(Role.CASHIER);
        employee = new Employee(1, "Yara", 101, account, 5000.0, qualifications, false,"terms");
    }

    @Test
    public void testSetNegativeSalaryShouldFail() {
        boolean result = employee.setSalary(-1000.0);
        assertFalse(result);
        assertEquals(5000.0, employee.getSalary());
    }

    @Test
    public void testAddAndRemoveQualification() {
        assertTrue(employee.addQualification(Role.CLEANING));
        assertTrue(employee.getQualifications().contains(Role.CLEANING));

        assertTrue(employee.removeQualification(Role.CLEANING));
        assertFalse(employee.getQualifications().contains(Role.CLEANING));
    }

    @Test
    public void testAddNullQualificationShouldFail() {
        assertFalse(employee.addQualification(null));
    }

    @Test
    public void testAddShiftScheduledAndRemoveItToHistory() {
        boolean added = employee.addShiftScheduled(1001, Role.CASHIER);
        assertTrue(added);
        assertEquals(1, employee.getShiftScheduled().size());

        boolean removed = employee.removeShiftScheduled(1001);
        assertTrue(removed);
        assertEquals(0, employee.getShiftScheduled().size());
        assertEquals(1, employee.getShiftHistory().size());
    }

    @Test
    public void testAddAvailabilityAndCheck() {
        Availability availability = new Availability(1, 2, true, false);
        assertTrue(employee.addAvailability(availability));
        assertTrue(employee.isShiftAvailableForEmployee(availability));
    }

    @Test
    public void testIsShiftAvailableFalse() {
        Availability notAvailable = new Availability(1, 3, false, true);
        assertFalse(employee.isShiftAvailableForEmployee(notAvailable));
    }

    @Test
    public void testNewWeek() {
        employee.addShiftScheduled(2000, Role.SHIFT_MANAGER);
        employee.addAvailability(new Availability(1, 1, true, true));

        employee.newWeek();

        assertEquals(0, employee.getShiftScheduled().size());
        assertEquals(0, employee.getAvailability().size());
        assertEquals(1, employee.getShiftHistory().size());
    }

    @Test
    public void testGetShiftIDsFromAssignments() {
        employee.addShiftScheduled(3000, Role.DELIVERY_MAN);
        employee.addShiftScheduled(3001, Role.CLEANING);

        List<Integer> ids = employee.getShiftIDsFromAssignments();
        assertTrue(ids.contains(3000));
        assertTrue(ids.contains(3001));
        assertEquals(2, ids.size());
    }

    @Test
    public void testCanWorkTrueAndFalse() {
        Availability a = new Availability(1, 5, true, false); // Thursday
        //employee.addAvailability(a);
        assertTrue(employee.addAvailability(a));
        assertTrue(employee.canWork(5, true, false));
        assertFalse(employee.canWork(5, false, true));
    }


}