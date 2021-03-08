package com.ercandoygun.marsroverexample.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Position {

    private int xdim;
    private int ydim;

    public boolean isOrigin() {
        return xdim == 0 && ydim == 0;
    }
}
