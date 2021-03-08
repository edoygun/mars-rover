package com.ercandoygun.marsroverexample.services;

import com.ercandoygun.marsroverexample.enums.Direction;
import com.ercandoygun.marsroverexample.model.Position;
import com.ercandoygun.marsroverexample.model.Rover;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class RoverMapServiceTest {

    @InjectMocks
    private RoverMapService service;

    @Test
    void findAll() {
        Rover rover = new Rover();
        rover.setId(1L);
        rover.setPosition(new Position(0,0));
        rover.setDirection(Direction.E);

        Rover rover2 = new Rover();
        rover2.setId(2L);
        rover2.setPosition(new Position(0,1));
        rover2.setDirection(Direction.E);

        service.save(rover);
        service.save(rover2);

        Set<Rover> rovers = service.findAll();
        assertEquals(rovers.size(), 2);
    }

    @Test
    void findById() {
        Rover rover = new Rover();
        rover.setId(1L);
        rover.setPosition(new Position(0,0));
        rover.setDirection(Direction.E);

        Rover rover2 = new Rover();
        rover2.setId(2L);
        rover2.setPosition(new Position(0,1));
        rover2.setDirection(Direction.E);

        service.save(rover);
        service.save(rover2);

        Rover resultRover = service.findById(1L);
        Position position = resultRover.getPosition();
        assertEquals(resultRover.getDirection(), Direction.E);
        assertEquals(position.getXdim(), 0);
        assertEquals(position.getYdim(), 0);
    }

    @Test
    void saveWithId() {
        Rover rover = new Rover();
        rover.setId(3L);
        rover.setDirection(Direction.S);
        rover.setPosition(new Position(2,4));

        Rover savedRover = service.save(rover);

        assertEquals(savedRover.getId(), rover.getId());
    }

    @Test
    void saveWithoutId() {
        Rover rover = new Rover();
        rover.setDirection(Direction.S);
        rover.setPosition(new Position(2,4));

        Rover savedRover = service.save(rover);

        assertEquals(savedRover.getId(), 1);
    }

    @Test
    void throwRunTimExceptionWhileSavingNullRover() {
        assertThrows(RuntimeException.class, () -> service.save(null));
    }

    @Test
    void getNextId() {
        Rover rover = new Rover();
        rover.setId(1L);
        rover.setDirection(Direction.S);
        rover.setPosition(new Position(2,4));

        Rover rover2 = new Rover();
        rover2.setId(2L);
        rover2.setDirection(Direction.S);
        rover2.setPosition(new Position(2,4));

        service.save(rover);
        service.save(rover2);

        assertEquals(service.getNextId(), 3L);
    }
}