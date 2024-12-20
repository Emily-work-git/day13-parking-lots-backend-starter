package org.afs.pakinglot.domain;

import org.afs.pakinglot.domain.ParkingLot;
import org.afs.pakinglot.domain.dto.FetchCarDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class ParkingManagerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void should_return_all_available_parking_strategies() throws Exception {
        mockMvc.perform(get("/parking-strategies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("Standard"))
                .andExpect(jsonPath("$[1]").value("Smart"))
                .andExpect(jsonPath("$[2]").value("Supersmart"));
    }


    @Test
    void should_return_all_parking_lots_when_GetAllParkingLots() throws Exception {
        mockMvc.perform(get("/parking-lots"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("The Plaza Park"))
                .andExpect(jsonPath("$[0].capacity").value(9))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("City Mall Garage"))
                .andExpect(jsonPath("$[1].capacity").value(12))
                .andExpect(jsonPath("$[2].id").value(3))
                .andExpect(jsonPath("$[2].name").value("Office Tower Parking"))
                .andExpect(jsonPath("$[2].capacity").value(9));
    }

    @Test
    void should_return_ticket_when_park_car_given_plate_number_and_strategy() throws Exception {

        mockMvc.perform(post("/parking-strategies/standard")
                        .content("{\"plateNumber\":\"XY-8563\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.plateNumber").value("XY-8563"))
                .andExpect(jsonPath("$.parkingLot").exists())
                .andExpect(jsonPath("$.position").exists());
    }

    @Test
    void should_return_car_when_fetch_car_given_plate_number() throws Exception {
        // First park the car to get a ticket
        mockMvc.perform(post("/parking-strategies/standard")
                        .content("{\"plateNumber\":\"XY-8563\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Then fetch the car using the plate number
        mockMvc.perform(post("/parking-lots/cars/XY-8563"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.plateNumber").value("XY-8563"));
    }
}
