package com.ercandoygun.marsroverexample.controllers;

import com.ercandoygun.marsroverexample.enums.Direction;
import com.ercandoygun.marsroverexample.model.Plateau;
import com.ercandoygun.marsroverexample.model.Position;
import com.ercandoygun.marsroverexample.model.Rover;
import com.ercandoygun.marsroverexample.services.PlateauService;
import com.ercandoygun.marsroverexample.services.RoverMapService;
import com.ercandoygun.marsroverexample.services.RoverService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class OperatorControllerTest {

    @Mock
    private RoverService roverService;

    @Mock
    private RoverMapService roverMapService;

    @Mock
    private PlateauService plateauService;

    @InjectMocks
    OperatorController controller;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }


    @Test
    void operatorControlPanel() throws Exception {
        Plateau plateau = new Plateau();

        when(plateauService.findPlateau()).thenReturn(plateau);

        mockMvc.perform(get("/operator"))
                .andExpect(status().isOk())
                .andExpect(view().name("areaCoordinates"))
                .andExpect(model().attribute("plateau", plateau));
    }

    @Test
    void setAreaCoordinates() throws Exception {
        mockMvc.perform(post("/setArea")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("operatorPanel"));

        verify(plateauService).savePlateau(any());
    }

    @Test
    void deployRover() throws Exception {
        Rover rover = new Rover(null, new Position(0, 0), Direction.N, false);
        Set<Rover> rovers = new HashSet<>();
        rovers.add(rover);

        when(roverMapService.findAll()).thenReturn(new HashSet<>()).thenReturn(rovers);

        mockMvc.perform(get("/deployRover"))
                .andExpect(status().isOk())
                .andExpect(view().name("operatorPanel"))
                .andExpect(model().attribute("rovers", rovers));
    }

    @Test
    void cancelDeploymentBecauseOriginIsOccupied() throws Exception {
        Rover rover = new Rover(null, new Position(0, 0), Direction.N, false);
        Set<Rover> rovers = new HashSet<>();
        rovers.add(rover);

        when(roverMapService.findAll()).thenReturn(rovers);

        mockMvc.perform(get("/deployRover"))
                .andExpect(status().isOk())
                .andExpect(view().name("operatorPanel"))
                .andExpect(model().attribute("rovers", rovers));

        verify(roverMapService, times(1)).findAll();
    }

    @Test
    void moveRover() throws Exception {
        Rover rover = new Rover(1L, new Position(0, 0), Direction.N, false);
        Set<Rover> rovers = new HashSet<>();
        rovers.add(rover);

        when(roverMapService.findById(1L)).thenReturn(rover);
        when(roverMapService.findAll()).thenReturn(rovers);

        mockMvc.perform(get("/moveRover/roverId/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("operatorPanel"))
                .andExpect(model().attribute("rovers", rovers));

        verify(roverService).processRoverCommand(eq(rover), any());
    }
}