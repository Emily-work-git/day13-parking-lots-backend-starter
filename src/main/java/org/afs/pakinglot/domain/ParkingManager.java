package org.afs.pakinglot.domain;

import org.afs.pakinglot.domain.exception.UnrecognizedTicketException;
import org.afs.pakinglot.domain.strategies.AvailableRateStrategy;
import org.afs.pakinglot.domain.strategies.MaxAvailableStrategy;
import org.afs.pakinglot.domain.strategies.SequentiallyStrategy;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class ParkingManager {
    private final List<ParkingLot> parkingLots;
    private final ParkingBoy standardParkingBoy;
    private final ParkingBoy smartParkingBoy;
    private final ParkingBoy superSmartParkingBoy;

    private static final Pattern LICENSE_PLATE_PATTERN = Pattern.compile("^[A-Z]{2}-\\d{4}$");

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
        validateLicensePlate(plateNumber);
        Car car = new Car(plateNumber);

        switch (strategy.toLowerCase()) {
            case "standard":
                return standardParkingBoy.park(car);
            case "smart":
                return smartParkingBoy.park(car);
            case "supersmart":
                return superSmartParkingBoy.park(car);
            default:
                throw new IllegalArgumentException("Invalid parking strategy");
        }
    }

    public Car fetch(String plateNumber) {
        validateLicensePlate(plateNumber);
        Optional<Ticket> ticketOptional = parkingLots.stream()
                .flatMap(parkingLot -> parkingLot.getTickets().stream())
                .filter(ticket -> ticket.plateNumber().equals(plateNumber))
                .findFirst();

        if (ticketOptional.isEmpty()) {
            System.out.println("line 57, ticket not found");
            throw new UnrecognizedTicketException();
        }

        Ticket ticket = ticketOptional.get();
        return parkingLots.stream()
                .filter(parkingLot -> parkingLot.contains(ticket))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Parking lot not found for ticket"))
                .fetch(ticket);
    }

    public void validateLicensePlate(String plateNumber) {
        if (plateNumber == null || !LICENSE_PLATE_PATTERN.matcher(plateNumber).matches()) {
            throw new IllegalArgumentException("Invalid license plate format");
        }
    }
}