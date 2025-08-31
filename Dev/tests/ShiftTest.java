package tests;

import static org.junit.jupiter.api.Assertions.*;

import DomainLayer.Employee.Role;
import DomainLayer.Employee.Shift;
import DomainLayer.Employee.ShiftStatus;
import DomainLayer.Employee.ShiftType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ShiftTest {
    private Shift shift;

    @BeforeEach
    public void setUp() {
        shift = new Shift(1, 100, LocalDate.now(), ShiftType.MORNING, ShiftStatus.SCHEDULED);
        shift.setNeededRoles(new ArrayList<>());
        ///to delete :

    }

    @Test
    public void testAddEmployeeRequired() {
        shift.addEmployeeRequired(Role.CASHIER, 101);
        assertEquals(Role.CASHIER, shift.getEmployeesRequired().get(101));

        // Should not overwrite existing employee
        shift.addEmployeeRequired(Role.CLEANING, 101);
        assertEquals(Role.CASHIER, shift.getEmployeesRequired().get(101));
    }

    @Test
    public void testRemoveEmployeeRequired() {
        shift.addEmployeeRequired(Role.CASHIER, 101);
        shift.removeEmployeeRequired(101);
        assertFalse(shift.getEmployeesRequired().containsKey(101));
    }

    @Test
    public void testClearEmployeesRequired() {
        shift.addEmployeeRequired(Role.CASHIER, 101);
        shift.addEmployeeRequired(Role.DELIVERY_MAN, 102);
        shift.clearEmployeesRequired();
        assertTrue(shift.getEmployeesRequired().isEmpty());
    }

    @Test
    public void testAddNeededRole() {
        shift.addNeededRole(Role.CLEANING);
        assertTrue(shift.getNeededRoles().contains(Role.CLEANING));
    }

    @Test
    public void testRemoveNeededRole() {
        shift.addNeededRole(Role.DELIVERY_MAN);
        shift.removeNeededRole(Role.DELIVERY_MAN);
        assertFalse(shift.getNeededRoles().contains(Role.DELIVERY_MAN));
    }

    @Test
    public void testClearNeededRoles() {
        shift.addNeededRole(Role.CASHIER);
        shift.addNeededRole(Role.HR_MANAGER);
        shift.clearNeededRoles();
        assertTrue(shift.getNeededRoles().isEmpty());
    }

    @Test
    public void testToStringFormat() {
        String result = shift.toString();
        assertTrue(result.contains("📅 Date:"));
        assertTrue(result.contains("⏰ Shift Type:"));
    }

}