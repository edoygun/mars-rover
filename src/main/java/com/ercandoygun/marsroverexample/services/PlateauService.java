package com.ercandoygun.marsroverexample.services;

import com.ercandoygun.marsroverexample.model.Plateau;
import com.ercandoygun.marsroverexample.model.Position;
import org.springframework.stereotype.Service;

@Service
public class PlateauService {

    Plateau plateau = new Plateau();

    public Plateau findPlateau() {
        return plateau;
    }

    public Plateau savePlateau(Plateau plateau) {
        this.plateau = plateau;
        return plateau;
    }

    public boolean isOutOfGrid(Position roverPosition) {
        return roverPosition.getYdim() < 0 || roverPosition.getYdim() > plateau.getPosition().getYdim()
                || roverPosition.getXdim() > plateau.getPosition().getXdim() || roverPosition.getXdim() < 0;
    }


}
