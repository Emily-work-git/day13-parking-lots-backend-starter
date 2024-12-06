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
    void should_return_ticket_when_park_given_plateNumber_and_sequentially_strategy() {
        String plateNumber = "ABC123";
        Ticket ticket = parkingManager.park(plateNumber, "sequentially");
        assertNotNull(ticket);
    }

    @Test
    void should_return_ticket_when_park_given_plateNumber_and_maxavailable_strategy() {
        String plateNumber = "DEF456";
        Ticket ticket = parkingManager.park(plateNumber, "maxavailable");
        assertNotNull(ticket);
    }

    @Test
    void should_return_ticket_when_park_given_plateNumber_and_availablerate_strategy() {
        String plateNumber = "GHI789";
        Ticket ticket = parkingManager.park(plateNumber, "availablerate");
        assertNotNull(ticket);
    }

    @Test
    void should_return_car_when_fetch_given_plate_number() {
        String plateNumber = "JKL012";
        Ticket ticket = parkingManager.park(plateNumber, "sequentially");
        Car fetchedCar = parkingManager.fetch(plateNumber);
        assertEquals(plateNumber, fetchedCar.plateNumber());
    }

    @Test
    void should_throw_exception_when_fetch_given_non_existent_plateNumber() {
        String plateNumber = "MNO345";
        assertThrows(UnrecognizedTicketException.class, () -> parkingManager.fetch(plateNumber));
    }

    @Test
    void should_throw_exception_when_park_given_parking_lots_are_full() {
        for (int i = 0; i < 30; i++) {
            parkingManager.park("CAR" + i, "sequentially");
        }
        assertThrows(NoAvailablePositionException.class, () -> parkingManager.park("PQR678", "sequentially"));
    }

    @Test
    void should_throw_exception_when_fetch_given_an_unrecognized_ticket() {
        String plateNumber = "XYZ123";
        Ticket unrecognizedTicket = new Ticket(CarPlateGenerator.generatePlate(), 1, 1);

        assertThrows(UnrecognizedTicketException.class, () -> parkingManager.fetch(unrecognizedTicket.plateNumber()));
    }

    @Test
    void should_throw_exception_when_fetch_given_a_used_ticket() {
        String plateNumber = "UVW456";
        Ticket ticket = parkingManager.park(plateNumber, "sequentially");
        parkingManager.fetch(plateNumber);
        assertThrows(UnrecognizedTicketException.class, () -> parkingManager.fetch(plateNumber));
    }
}