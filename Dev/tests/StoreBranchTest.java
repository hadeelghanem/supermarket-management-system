package tests;

import DomainLayer.*;

import DomainLayer.Employee.*;
import DomainLayer.Employee.ShiftStatus;
import DomainLayer.Employee.ShiftType;
import DomainLayer.Employee.StoreBranch;
import DomainLayer.Shipment.Area;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StoreBranchTest {
    private StoreBranch branch=null;
    private Shift shift1;
    private Shift shift2;

    @BeforeEach
    public void setUp() {
        branch = new StoreBranch(1, "Main Branch", "123 Main St", StoreStatus.OPEN,"tel","name", Area.NORTH);
        shift1 = new Shift(101, 1, LocalDate.now(), ShiftType.MORNING, ShiftStatus.SCHEDULED);
        shift2 = new Shift(102, 1, LocalDate.now(), ShiftType.NIGHT, ShiftStatus.SCHEDULED);
        shift1.setNeededRoles(new ArrayList<>());
        shift2.setNeededRoles(new ArrayList<>());
    }

    @Test
    public void testAddAndRemoveShiftScheduled() {
        branch.addShiftScheduled(shift1);
        assertTrue(branch.getShiftScheduled().contains(shift1));

        branch.removeShiftScheduled(shift1);
        assertFalse(branch.getShiftScheduled().contains(shift1));
        assertTrue(branch.getShiftHistory().contains(shift1));
    }

    @Test
    public void testAddAndRemoveEmployeeToShift() {
        branch.addEmployeeToShift(shift1, 200, Role.CASHIER);
        assertEquals(Role.CASHIER, shift1.getEmployeesRequired().get(200));

        branch.removeEmployeeFromShift(shift1, 200);
        assertFalse(shift1.getEmployeesRequired().containsKey(200));
    }

    @Test
    public void testAddAndRemoveRoleToShift() {
        branch.addRoleToShift(shift1, Role.CLEANING);
        assertTrue(shift1.getNeededRoles().contains(Role.CLEANING));

        branch.removeRoleFromShift(shift1, Role.CLEANING);
        assertFalse(shift1.getNeededRoles().contains(Role.CLEANING));
    }



    @Test
    public void testCancelShiftScheduled() {
        branch.addShiftScheduled(shift1);
        branch.cancelShiftScheduled(shift1);
        assertFalse(branch.getShiftScheduled().contains(shift1));
        // You could also check if it's added to a "cancelled" list if you use one
    }

    @Test
    public void testNewWeekMovesShiftsToHistory() {
        branch.addShiftScheduled(shift1);
        branch.addShiftScheduled(shift2);

        branch.newWeek();

        assertTrue(branch.getShiftHistory().contains(shift1));
        assertTrue(branch.getShiftHistory().contains(shift2));
        assertTrue(branch.getShiftScheduled().isEmpty());
        assertEquals(ShiftStatus.DONE, shift1.getStatus());
    }

   }