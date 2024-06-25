package com.danilo.zeparceiros.dtos;

import org.locationtech.jts.geom.Point;

import java.util.Arrays;
import java.util.List;

public record GeoJsonPointDTO(String type, List<Double> coordinates) {
    public GeoJsonPointDTO(Point p) {
        this(
            p.getGeometryType(),
            Arrays.asList(p.getCoordinates()[0].x, p.getCoordinates()[0].y)
        );
    }
}
