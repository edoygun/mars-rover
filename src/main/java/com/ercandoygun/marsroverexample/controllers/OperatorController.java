package com.ercandoygun.marsroverexample.controllers;

import com.ercandoygun.marsroverexample.command.MoveCommand;
import com.ercandoygun.marsroverexample.enums.Direction;
import com.ercandoygun.marsroverexample.model.Plateau;
import com.ercandoygun.marsroverexample.model.Position;
import com.ercandoygun.marsroverexample.model.Rover;
import com.ercandoygun.marsroverexample.services.PlateauService;
import com.ercandoygun.marsroverexample.services.RoverMapService;
import com.ercandoygun.marsroverexample.services.RoverService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Slf4j
@Controller
public class OperatorController {

    @Autowired
    private RoverService roverService;

    @Autowired
    private RoverMapService roverMapService;

    @Autowired
    private PlateauService plateauService;

    @RequestMapping("/operator")
    public String operatorControlPanel(Model model) {
        model.addAttribute("plateau", plateauService.findPlateau());
        return "areaCoordinates";
    }

    @PostMapping
    @RequestMapping("/setArea")
    public String setAreaCoordinates(@ModelAttribute Plateau command) {
        plateauService.savePlateau(command);
        return "operatorPanel";
    }

    @GetMapping
    @RequestMapping("/deployRover")
    public String deployRover(Model model) {
        Set<Rover> rovers = roverMapService.findAll();
        boolean isOriginOccupied = rovers.stream().filter(r -> r.getPosition().isOrigin()).findAny().orElse(null) != null;

        if (isOriginOccupied) {
            log.info("Deployment area is occupied by another rover!");
            model.addAttribute("rovers", rovers);
            model.addAttribute("moveCommand", new MoveCommand());
            return "operatorPanel";
        }

        Rover rover = new Rover(null, new Position(0,0), Direction.N);
        roverMapService.save(rover);

        model.addAttribute("rovers", roverMapService.findAll());
        model.addAttribute("moveCommand", new MoveCommand());
        return "operatorPanel";
    }

    @GetMapping
    @RequestMapping("/moveRover/roverId/{roverId}")
    public String moveRover(@ModelAttribute MoveCommand command, @PathVariable Long roverId, Model model) {
        Rover rover = roverMapService.findById(roverId);
        roverService.processRoverCommand(rover, command.getCode());
        model.addAttribute("rovers", roverMapService.findAll());
        return "operatorPanel";
    }
}
