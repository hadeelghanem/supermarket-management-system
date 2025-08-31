package tests;

import DomainLayer.Shipment.Truck;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TruckTest {
    Truck Scania = new Truck(5, "Scania", 1, 1000, 2000);
    @Test
    void isAvailable() {
        assertTrue(Scania.isAvailable());
    }

    @Test
    void isAvailableWhenNAvailable() {
        Scania.assignToTransport();
        assertFalse(Scania.isAvailable());
    }

    @Test
    void canCarry() {
        assertTrue(Scania.canCarry(1000));
    }

    @Test
    void canCarryWhenCant() {
        assertFalse(Scania.canCarry(2001));
    }


    @Test
    void assignToTransport() {
        assertTrue(Scania.isAvailable());
    }

    @Test
    void assignToTransportWhenNotAvailable() {
        Scania.assignToTransport();
        assertFalse(Scania.isAvailable());
    }

    @Test
    void releaseFromTransport() {
        Scania.assignToTransport();
        Scania.releaseFromTransport();
        assertTrue(Scania.isAvailable());
    }
}