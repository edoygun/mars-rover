package com.ercandoygun.marsroverexample.services;

import com.ercandoygun.marsroverexample.enums.Direction;
import com.ercandoygun.marsroverexample.exception.OutOfGridException;
import com.ercandoygun.marsroverexample.exception.PossibleCollisionException;
import com.ercandoygun.marsroverexample.model.Plateau;
import com.ercandoygun.marsroverexample.model.Position;
import com.ercandoygun.marsroverexample.model.Rover;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class RoverServiceTest {

    @InjectMocks
    private RoverService service;

    @Mock
    private PlateauService plateauService;

    @Mock
    private RoverMapService roverMapService;

    @Test
    void processRoverCommandWithTargetPositionAvailable() {
        Rover rover = new Rover();
        rover.setId(1L);
        rover.setPosition(new Position(0, 0));
        rover.setDirection(Direction.E);
        String code = "MMLMR";

        Plateau plateau = new Plateau();
        Position area = new Position();
        area.setXdim(10);
        area.setYdim(10);
        plateau.setPosition(area);

        Mockito.when(plateauService.findPlateau()).thenReturn(plateau);

        service.processRoverCommand(rover, code);
        rover.setPosition(new Position(2, 1));

        Mockito.verify(roverMapService).save(rover);
    }

    @Test
    void processRoverCommandWithTargetPositionAvailableForNorthFacingWest() {
        Rover rover = new Rover();
        rover.setId(1L);
        rover.setPosition(new Position(0, 0));
        rover.setDirection(Direction.N);
        String code = "MMML";

        Plateau plateau = new Plateau();
        Position area = new Position();
        area.setXdim(10);
        area.setYdim(10);
        plateau.setPosition(area);

        Mockito.when(plateauService.findPlateau()).thenReturn(plateau);

        service.processRoverCommand(rover, code);

        ArgumentCaptor<Rover> argumentCaptor = ArgumentCaptor.forClass(Rover.class);
        Mockito.verify(roverMapService).save(argumentCaptor.capture());
        assertEquals(argumentCaptor.getValue().getPosition().getXdim(), 0);
        assertEquals(argumentCaptor.getValue().getPosition().getYdim(), 3);
        assertEquals(argumentCaptor.getValue().getDirection(), Direction.W);
    }

    @Test
    void processRoverCommandWithRoverScanningFourDirections() {
        Rover rover = new Rover();
        rover.setId(1L);
        rover.setPosition(new Position(0, 0));
        rover.setDirection(Direction.N);
        String code = "MMMRMMMRMRM";

        Plateau plateau = new Plateau();
        Position area = new Position();
        area.setXdim(10);
        area.setYdim(10);
        plateau.setPosition(area);

        Mockito.when(plateauService.findPlateau()).thenReturn(plateau);

        service.processRoverCommand(rover, code);

        ArgumentCaptor<Rover> argumentCaptor = ArgumentCaptor.forClass(Rover.class);
        Mockito.verify(roverMapService).save(argumentCaptor.capture());
        assertEquals(argumentCaptor.getValue().getPosition().getXdim(), 2);
        assertEquals(argumentCaptor.getValue().getPosition().getYdim(), 2);
        assertEquals(argumentCaptor.getValue().getDirection(), Direction.W);
    }

    @Test
    void processRoverCommandWithTargetPositionOutOfGrid() {
        Rover rover = new Rover();
        rover.setId(1L);
        rover.setPosition(new Position(0, 0));
        rover.setDirection(Direction.E);
        String code = "MMMLMR";

        Plateau plateau = new Plateau();
        Position area = new Position();
        area.setXdim(2);
        area.setYdim(3);
        plateau.setPosition(area);

        Mockito.when(plateauService.findPlateau()).thenReturn(plateau);

        service.processRoverCommand(rover, code);

        rover.setPosition(new Position(2, 0));

        ArgumentCaptor<Rover> argumentCaptor = ArgumentCaptor.forClass(Rover.class);
        Mockito.verify(roverMapService).save(argumentCaptor.capture());
        assertEquals(argumentCaptor.getValue().getPosition().getXdim(), 2);
        assertEquals(argumentCaptor.getValue().getPosition().getYdim(), 0);
    }

    @Test
    void processRoverCommandWithTargetPositionForDeployment() {
        Rover rover = new Rover();
        rover.setId(1L);
        rover.setPosition(new Position(0, 0));
        rover.setDirection(Direction.E);
        String code = "44E";

        Plateau plateau = new Plateau();
        Position area = new Position();
        area.setXdim(10);
        area.setYdim(10);
        plateau.setPosition(area);

        Mockito.when(plateauService.findPlateau()).thenReturn(plateau);

        service.processRoverCommand(rover, code);

        ArgumentCaptor<Rover> argumentCaptor = ArgumentCaptor.forClass(Rover.class);
        Mockito.verify(roverMapService).save(argumentCaptor.capture());
        assertEquals(argumentCaptor.getValue().getPosition().getXdim(), 4);
        assertEquals(argumentCaptor.getValue().getPosition().getYdim(), 4);
        assertEquals(argumentCaptor.getValue().getDirection(), Direction.E);
    }

    @Test
    void processRoverCommandWithOutOfGridTargetPositionForDeployment() {
        Rover rover = new Rover();
        rover.setId(1L);
        rover.setPosition(new Position(0, 0));
        rover.setDirection(Direction.E);
        String code = "44E";

        Plateau plateau = new Plateau();
        Position area = new Position();
        area.setXdim(2);
        area.setYdim(3);
        plateau.setPosition(area);

        Mockito.when(plateauService.findPlateau()).thenReturn(plateau);

        service.processRoverCommand(rover, code);

        ArgumentCaptor<Rover> argumentCaptor = ArgumentCaptor.forClass(Rover.class);
        Mockito.verify(roverMapService).save(argumentCaptor.capture());
        assertEquals(argumentCaptor.getValue().getPosition().getXdim(), 0);
        assertEquals(argumentCaptor.getValue().getPosition().getYdim(), 0);
    }


    @Test
    void processRoverCommandWithTargetPositionOccupiedByAnotherRover() {
        Rover rover = new Rover();
        rover.setId(1L);
        rover.setPosition(new Position(0, 0));
        rover.setDirection(Direction.E);
        String code = "MMMLMR";

        Rover rover2 = new Rover();
        rover2.setId(2L);
        rover2.setPosition(new Position(3, 1));
        rover2.setDirection(Direction.E);

        Plateau plateau = new Plateau();
        Position area = new Position();
        area.setXdim(10);
        area.setYdim(10);
        plateau.setPosition(area);
        Set<Rover> rovers = new HashSet<>();
        rovers.add(rover);
        rovers.add(rover2);

        Mockito.when(plateauService.findPlateau()).thenReturn(plateau);
        Mockito.when(roverMapService.findAll()).thenReturn(rovers);

        service.processRoverCommand(rover, code);

        ArgumentCaptor<Rover> argumentCaptor = ArgumentCaptor.forClass(Rover.class);
        Mockito.verify(roverMapService).save(argumentCaptor.capture());
        assertEquals(argumentCaptor.getValue().getPosition().getXdim(), 3);
        assertEquals(argumentCaptor.getValue().getPosition().getYdim(), 0);
    }

    @Test
    void throwsOutOfGridExceptionIfTargetPositionIfOutOfGrid() {
        Position targetPosition = new Position();
        targetPosition.setXdim(4);
        targetPosition.setYdim(1);

        Plateau plateau = new Plateau();
        Position plateauGrid = new Position();
        plateauGrid.setXdim(3);
        plateauGrid.setYdim(1);
        plateau.setPosition(plateauGrid);

        Mockito.when(plateauService.findPlateau()).thenReturn(plateau);

        assertThrows(OutOfGridException.class, () -> service.checkTargetPosition(targetPosition));
    }

    @Test
    void throwsPossibleCollisionExceptionIfPositionIsOccupied() {
        Position targetPosition = new Position();
        targetPosition.setXdim(4);
        targetPosition.setYdim(1);

        Plateau plateau = new Plateau();
        Position plateauGrid = new Position();
        plateauGrid.setXdim(5);
        plateauGrid.setYdim(5);
        plateau.setPosition(plateauGrid);
        Rover rover2 = new Rover();
        rover2.setId(2L);
        rover2.setPosition(new Position(4, 1));
        rover2.setDirection(Direction.E);

        Set<Rover> rovers = new HashSet<>();
        rovers.add(rover2);

        Mockito.when(plateauService.findPlateau()).thenReturn(plateau);
        Mockito.when(roverMapService.findAll()).thenReturn(rovers);

        assertThrows(PossibleCollisionException.class, () -> service.checkTargetPosition(targetPosition));
    }

}