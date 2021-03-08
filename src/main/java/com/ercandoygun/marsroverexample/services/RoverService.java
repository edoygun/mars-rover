package com.ercandoygun.marsroverexample.services;

import com.ercandoygun.marsroverexample.enums.Direction;
import com.ercandoygun.marsroverexample.exception.OutOfGridException;
import com.ercandoygun.marsroverexample.exception.PossibleCollisionException;
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

    @Autowired
    private RoverMapService roverMapService;

    public void processRoverCommand(Rover rover, String code) {
        try {
            List<Character> codes = code.chars().mapToObj(i -> (char) i).collect(Collectors.toList());

            codes.forEach(c -> {
                if(c.equals('M')) {
                    moveForward(rover);
                } else if(c.equals('L') || c.equals('R')){
                    turnRover(rover, c);
                } else {
                    deployAtGivenPosition(rover, code.toCharArray());
                }
            });
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            roverMapService.save(rover);
        }
    }

    private void deployAtGivenPosition(Rover rover, char[] codes) {
        if(codes.length > 3) {
            throw new RuntimeException("Invalid argument!");
        }

        Position position = new Position();
        position.setXdim(Character.getNumericValue(codes[0]));
        position.setYdim(Character.getNumericValue(codes[1]));

        if(checkTargetPosition(position)) {
            rover.setPosition(position);
            log.info("The rover has moved to (" + position.getXdim() + ", " + position.getYdim() + ")");
        }

        rover.setDirection(Direction.valueOf(String.valueOf(codes[2])));
    }

    private void moveForward(Rover rover) {
        Position targetPosition = new Position();
        Position roverPosition = rover.getPosition();
        targetPosition.setYdim(roverPosition.getYdim());
        targetPosition.setXdim(roverPosition.getXdim());
        switch (rover.getDirection().getValue())
        {
            case 'N':
                targetPosition.setYdim(roverPosition.getYdim()+1);
                break;
            case 'E':
                targetPosition.setXdim(roverPosition.getXdim()+1);
                break;
            case 'S':
                targetPosition.setYdim(roverPosition.getYdim()-1);
                break;
            case 'W':
                targetPosition.setXdim(roverPosition.getXdim()-1);
                break;
            default:
                break;
        }

        if(checkTargetPosition(targetPosition)) {
            rover.setPosition(targetPosition);
            log.info("The rover has moved to (" + targetPosition.getXdim() + ", " + targetPosition.getYdim() + ")");
        }
    }

    public boolean checkTargetPosition(Position targetPosition) {
        Position plateauArea = plateauService.findPlateau().getPosition();

        if(targetPosition.getXdim() > plateauArea.getXdim() || targetPosition.getYdim() > plateauArea.getYdim()) {
            throw new OutOfGridException("The rover cannot go outside of the grid!");
        }

        boolean possibleCollision = roverMapService.findAll().stream().filter(r ->
                targetPosition.getYdim() == r.getPosition().getYdim() && targetPosition.getXdim() == r.getPosition().getXdim())
                .findAny().orElse(null) != null;

        if(possibleCollision) {
            throw new PossibleCollisionException("The rover has stopped because of possible collision with another rover!");
        }

        return  true;
    }

    private void turnRover(Rover rover, Character c) {
        if(c.equals('L')) {
            turnLeft(rover);
        } else if(c.equals('R')) {
            turnRight(rover);
        }
    }

    private void turnRight(Rover rover) {
        switch (rover.getDirection().getValue())
        {
            case 'N':
                rover.setDirection(Direction.E);
                break;
            case 'E':
                rover.setDirection(Direction.S);
                break;
            case 'S':
                rover.setDirection(Direction.W);
                break;
            case 'W':
                rover.setDirection(Direction.N);
                break;
            default:
                break;
        }
    }

    private void turnLeft(Rover rover) {
        switch (rover.getDirection().getValue())
        {
            case 'N':
                rover.setDirection(Direction.W);
                break;
            case 'W':
                rover.setDirection(Direction.S);
                break;
            case 'S':
                rover.setDirection(Direction.E);
                break;
            case 'E':
                rover.setDirection(Direction.N);
                break;
            default:
                break;
        }
    }


}

