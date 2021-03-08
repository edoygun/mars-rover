package com.ercandoygun.marsroverexample.enums;

import java.util.Arrays;

public enum Direction {
    N('N'), W('W'), S('S'), E('E');

    private char value;

    Direction(char value) {
        this.value = value;
    }

    public char getValue() {
        return value;
    }
}
