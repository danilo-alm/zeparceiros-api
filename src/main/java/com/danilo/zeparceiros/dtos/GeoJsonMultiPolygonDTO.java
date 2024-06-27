package com.danilo.zeparceiros.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.MultiPolygon;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class GeoJsonMultiPolygonDTO {
    private final String type;
    private final List<List<List<List<Double>>>> coordinates;

    public GeoJsonMultiPolygonDTO() {
        this.type = "MultiPolygon";
        this.coordinates = new ArrayList<>();
    }

    public GeoJsonMultiPolygonDTO(MultiPolygon multiPolygon) {
        this.type = multiPolygon.getGeometryType();
        this.coordinates = convertToNestedList(multiPolygon.getCoordinates());
    }

    private List<List<List<List<Double>>>> convertToNestedList(Coordinate[] coordinates) {
        List<List<List<List<Double>>>> result = new ArrayList<>();

        List<List<List<Double>>> polygon = new ArrayList<>();
        List<List<Double>> ring = new ArrayList<>();

        for (Coordinate coord : coordinates) {
            List<Double> point = new ArrayList<>();
            point.add(coord.getX());
            point.add(coord.getY());
            ring.add(point);
        }

        polygon.add(ring);
        result.add(polygon);

        return result;
    }
}
