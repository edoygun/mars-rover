package com.ercandoygun.marsroverexample.services;

import com.ercandoygun.marsroverexample.enums.Direction;
import com.ercandoygun.marsroverexample.model.Position;
import com.ercandoygun.marsroverexample.model.Rover;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RoverService {

    @Autowired
    private PlateauService plateauService;

    Map<Long, Rover> map = new HashMap<>();

    public Set<Rover> findAll() {
        return new HashSet<>(map.values());
    }

    public Rover findById(Long id) {
        return map.get(id);
    }

    public Rover save(Rover rover) {
        if(rover != null) {
            if(rover.getId() == null) {
                rover.setId(getNextId());
            }
            map.put(rover.getId(), rover);
        } else {
            throw new RuntimeException("Object cannot be null!");
        }
        return rover;
    }

    public Long getNextId() {
        Long nextId = null;
        try {
            nextId = Collections.max(map.keySet()) + 1;
        } catch (NoSuchElementException e) {
            nextId = 1L;
        }
        return nextId;
    }

    public void processRoverCommand(Rover rover, String code) {
        List<Character> codes = code.chars().mapToObj(i -> (char) i).collect(Collectors.toList());

        codes.forEach(c -> {
            if(c.equals('M')) {
                moveForward(rover);
            } else {
                turnRover(rover, c);
            }
        });

        save(rover);
    }

    private void moveForward(Rover rover) {
        Position targetPosition = new Position();
        Position roverPosition = rover.getPosition();
        targetPosition = roverPosition;
        Direction direction = rover.getDirection();

        if(direction.equals(Direction.NORTH) || direction.equals(Direction.SOUTH)) {
            targetPosition.setYdim(roverPosition.getYdim()+1);
        } else {
            targetPosition.setXdim(roverPosition.getXdim()+1);
        }

        if(checkTargetPosition(targetPosition)) {
            rover.setPosition(targetPosition);
            save(rover);
            log.info("The rover has moved to (" + targetPosition.getXdim() + ", " + targetPosition.getYdim() + ")");
        }
    }

    private boolean checkTargetPosition(Position targetPosition) {
        Position plateauArea = plateauService.findPlateau().getPosition();

        if(targetPosition.getXdim() > plateauArea.getXdim() || targetPosition.getYdim() > plateauArea.getYdim()) {
            log.info("The rover cannot go outside of the grid!");
            return false;
        }

        boolean possibleCollision = findAll().stream().filter(r ->
                targetPosition.getYdim() == r.getPosition().getYdim() && targetPosition.getXdim() == r.getPosition().getXdim())
                .findAny().orElse(null) != null;

        if(possibleCollision) {
            log.info("The rover has stopped because of possible collision with another rover!");
            return false;
        }

        return  true;
    }

    private void turnRover(Rover rover, Character c) {
        if(c.equals('L')) {
            turnLeft(rover);
        } else if(c.equals('R')) {
            turnRight(rover);
        } else {
            return;
        }
    }

    private void turnRight(Rover rover) {
        switch (rover.getDirection().getValue())
        {
            case 'N':
                rover.setDirection(Direction.EAST);
                break;
            case 'E':
                rover.setDirection(Direction.SOUTH);
                break;
            case 'S':
                rover.setDirection(Direction.WEST);
                break;
            case 'W':
                rover.setDirection(Direction.NORTH);
                break;
            default:
                break;
        }
    }

    private void turnLeft(Rover rover) {
        switch (rover.getDirection().getValue())
        {
            case 'N':
                rover.setDirection(Direction.WEST);
                break;
            case 'W':
                rover.setDirection(Direction.SOUTH);
                break;
            case 'S':
                rover.setDirection(Direction.EAST);
                break;
            case 'E':
                rover.setDirection(Direction.NORTH);
                break;
            default:
                break;
        }
    }


}

