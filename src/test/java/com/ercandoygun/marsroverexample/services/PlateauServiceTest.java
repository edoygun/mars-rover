package com.ercandoygun.marsroverexample.services;

import com.ercandoygun.marsroverexample.model.Plateau;
import com.ercandoygun.marsroverexample.model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class PlateauServiceTest {

    @InjectMocks
    private PlateauService service;

    @BeforeEach
    void setUp() {
        Plateau plateau = new Plateau();
        Position grid = new Position();
        grid.setXdim(10);
        grid.setYdim(10);
        plateau.setPosition(grid);

        service.savePlateau(plateau);
    }

    @Test
    void findPlateau() {
        Plateau plateau = service.findPlateau();
        assertEquals(plateau.getPosition().getXdim(), 10);
        assertEquals(plateau.getPosition().getYdim(), 10);
    }

    @Test
    void savePlateau() {
        Plateau plateau = new Plateau();
        Position grid = new Position();
        grid.setXdim(5);
        grid.setYdim(5);
        plateau.setPosition(grid);

        Plateau savedPlateau = service.savePlateau(plateau);

        assertEquals(savedPlateau.getPosition().getXdim(), 5);
        assertEquals(savedPlateau.getPosition().getYdim(), 5);
    }
}