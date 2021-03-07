package com.ercandoygun.marsroverexample.model;

import com.ercandoygun.marsroverexample.enums.Direction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Rover {

    private Long id;
    private Position position;
    private Direction direction;

}
