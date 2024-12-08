package org.afs.pakinglot.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.afs.pakinglot.domain.exception.NoAvailablePositionException;
import org.afs.pakinglot.domain.exception.UnrecognizedTicketException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ParkingManagerTest {
    private ParkingManager parkingManager;

    @BeforeEach
    void setUp() {
        parkingManager = new ParkingManager();
    }

    @Test
    void should_return_all_parking_lots_when_getAllParkingLots_given_new_parkingManager() {
        var parkingLots = parkingManager.getAllParkingLots();
        assertEquals(3, parkingLots.size());
    }

    @Test
    void should_return_ticket_when_park_given_plateNumber_and_standard_strategy() {
        String plateNumber = "AB-1234";
        Ticket ticket = parkingManager.park(plateNumber, "standard");
        assertNotNull(ticket);
    }

    @Test
    void should_return_ticket_when_park_given_plateNumber_and_smart_strategy() {
        String plateNumber = "DE-4567";
        Ticket ticket = parkingManager.park(plateNumber, "smart");
        assertNotNull(ticket);
    }

    @Test
    void should_return_ticket_when_park_given_plateNumber_and_superSmart_strategy() {
        String plateNumber = "GH-7891";
        Ticket ticket = parkingManager.park(plateNumber, "superSmart");
        assertNotNull(ticket);
    }

    @Test
    void should_return_car_when_fetch_given_plate_number() {
        String plateNumber = "JK-0122";
        Ticket ticket = parkingManager.park(plateNumber, "standard");
        Car fetchedCar = parkingManager.fetch(plateNumber);
        assertEquals(plateNumber, fetchedCar.plateNumber());
    }

    @Test
    void should_throw_exception_when_fetch_given_non_existent_plateNumber() {
        String plateNumber = "MN-3456";
        assertThrows(UnrecognizedTicketException.class, () -> parkingManager.fetch(plateNumber));
    }

    @Test
    void should_throw_exception_when_park_given_parking_lots_are_full() {
        for (int i = 0; i < 30; i++) {
            String plateNumber = String.format("%s-%04d", "AB", i);
            System.out.println(plateNumber);
            parkingManager.park(plateNumber, "standard");
        }
        assertThrows(NoAvailablePositionException.class, () -> parkingManager.park("PQ-1678", "standard"));
    }

    @Test
    void should_throw_exception_when_fetch_given_an_unrecognized_ticket() {
        String plateNumber = "XY-1123";
        Ticket unrecognizedTicket = new Ticket(CarPlateGenerator.generatePlate(), 1, 1);

        assertThrows(UnrecognizedTicketException.class, () -> parkingManager.fetch(unrecognizedTicket.plateNumber()));
    }

    @Test
    void should_throw_exception_when_fetch_given_a_used_ticket() {
        String plateNumber = "UV-1456";
        Ticket ticket = parkingManager.park(plateNumber, "standard");
        parkingManager.fetch(plateNumber);
        assertThrows(UnrecognizedTicketException.class, () -> parkingManager.fetch(plateNumber));
    }

    @Test
    void should_throw_exception_when_license_plate_is_null_given_validateLicensePlate() {
        assertThrows(IllegalArgumentException.class, () -> parkingManager.validateLicensePlate(null));
    }

    @Test
    void should_throw_exception_when_license_plate_is_invalid_given_validateLicensePlate() {
        assertThrows(IllegalArgumentException.class, () -> parkingManager.validateLicensePlate("123-ABCD"));
    }

    @Test
    void should_not_throw_exception_when_license_plate_is_valid_given_validateLicensePlate() {
        parkingManager.validateLicensePlate("AB-1234");
    }
}