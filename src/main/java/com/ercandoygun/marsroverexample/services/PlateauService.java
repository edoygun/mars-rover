package com.ercandoygun.marsroverexample.services;

import com.ercandoygun.marsroverexample.model.Plateau;
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


}
