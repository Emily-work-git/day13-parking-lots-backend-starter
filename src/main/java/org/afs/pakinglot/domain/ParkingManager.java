package org.afs.pakinglot.domain;

import org.afs.pakinglot.domain.exception.UnrecognizedTicketException;
import org.afs.pakinglot.domain.strategies.AvailableRateStrategy;
import org.afs.pakinglot.domain.strategies.MaxAvailableStrategy;
import org.afs.pakinglot.domain.strategies.SequentiallyStrategy;

import java.util.List;
import java.util.Optional;

public class ParkingManager {
    private final List<ParkingLot> parkingLots;
    private final ParkingBoy standardParkingBoy;
    private final ParkingBoy smartParkingBoy;
    private final ParkingBoy superSmartParkingBoy;

    public ParkingManager() {
        ParkingLot plazaPark = new ParkingLot(1, "The Plaza Park", 9);
        ParkingLot cityMallGarage = new ParkingLot(2, "City Mall Garage", 12);
        ParkingLot officeTowerParking = new ParkingLot(3, "Office Tower Parking", 9);

        this.parkingLots = List.of(plazaPark, cityMallGarage, officeTowerParking);

        this.standardParkingBoy = new ParkingBoy(parkingLots, new SequentiallyStrategy());
        this.smartParkingBoy = new ParkingBoy(parkingLots, new MaxAvailableStrategy());
        this.superSmartParkingBoy = new ParkingBoy(parkingLots, new AvailableRateStrategy());
    }

    public List<ParkingLot> getAllParkingLots() {
        return parkingLots;
    }

    public Ticket park(String plateNumber, String strategy) {
        Car car = new Car(plateNumber);
        switch (strategy.toLowerCase()) {
            case "sequentially":
                return standardParkingBoy.park(car);
            case "maxavailable":
                return smartParkingBoy.park(car);
            case "availablerate":
                return superSmartParkingBoy.park(car);
            default:
                throw new IllegalArgumentException("Invalid parking strategy");
        }
    }

    public Car fetch(String plateNumber) {
        Optional<Ticket> ticketOptional = parkingLots.stream()
                .flatMap(parkingLot -> parkingLot.getTickets().stream())
                .filter(ticket -> ticket.plateNumber().equals(plateNumber))
                .findFirst();

        if (ticketOptional.isEmpty()) {
            throw new UnrecognizedTicketException();
        }

        Ticket ticket = ticketOptional.get();
        return parkingLots.stream()
                .filter(parkingLot -> parkingLot.contains(ticket))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Parking lot not found for ticket"))
                .fetch(ticket);
    }
}