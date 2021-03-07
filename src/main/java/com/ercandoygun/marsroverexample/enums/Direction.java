package com.ercandoygun.marsroverexample.enums;

public enum Direction {
    NORTH('N'), WEST('W'), SOUTH('S'), EAST('E');

    private char value;

    Direction(char value) {
        this.value = value;
    }

    public char getValue() {
        return value;
    }

    public void setValue(char value) {
        this.value = value;
    }
}
