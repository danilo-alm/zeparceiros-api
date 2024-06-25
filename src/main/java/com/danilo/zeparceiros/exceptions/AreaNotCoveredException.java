package com.danilo.zeparceiros.exceptions;

public class AreaNotCoveredException extends RuntimeException {
    public AreaNotCoveredException(Double lat, Double lon) {
        super(String.format("No partner covers this location (%f,%f)", lat, lon));
    }
}
