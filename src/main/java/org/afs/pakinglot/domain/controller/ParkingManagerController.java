package org.afs.pakinglot.domain.controller;

import org.afs.pakinglot.domain.ParkingManager;
import org.afs.pakinglot.domain.Ticket;
import org.afs.pakinglot.domain.Car;
import org.afs.pakinglot.domain.ParkingLot;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(
)

public class ParkingManagerController {

    private final ParkingManager parkingManager;

    public ParkingManagerController() {
        this.parkingManager = new ParkingManager();
    }

    @GetMapping("/parking-strategies")
    public ResponseEntity<List<String>> getAllAvailableParkingStrategy() {
        List<String> strategies = List.of("Standard", "Smart", "Supersmart");
        return ResponseEntity.ok(strategies);
    }

    @PostMapping("/parking-strategies/{strategy}")
    public ResponseEntity<Ticket> parkCar(@PathVariable String strategy, @RequestBody String plateNumber) {
        Ticket ticket = parkingManager.park(plateNumber, strategy);
        return ResponseEntity.ok(ticket);
    }

    @GetMapping("/parking-lots")
    public ResponseEntity<List<ParkingLot>> getAllParkingLots() {
        List<ParkingLot> parkingLots = parkingManager.getAllParkingLots();
        return ResponseEntity.ok(parkingLots);
    }

    @PostMapping("/parking-lots/cars/{plateNumber}")
    public ResponseEntity<Car> fetchCar(@PathVariable String plateNumber) {
        Car fetchedCar = parkingManager.fetch(plateNumber);
        return ResponseEntity.ok(fetchedCar);
    }
}