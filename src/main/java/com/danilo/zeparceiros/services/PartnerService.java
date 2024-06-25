package com.danilo.zeparceiros.services;

import com.danilo.zeparceiros.domain.partner.Partner;
import com.danilo.zeparceiros.dtos.GeoJsonMultiPolygonDTO;
import com.danilo.zeparceiros.dtos.GeoJsonPointDTO;
import com.danilo.zeparceiros.dtos.PartnerRequestDTO;
import com.danilo.zeparceiros.dtos.PartnerResponseDTO;
import com.danilo.zeparceiros.exceptions.PartnerNotFoundException;
import com.danilo.zeparceiros.repositories.PartnerRepository;
import lombok.AllArgsConstructor;
import org.locationtech.jts.geom.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PartnerService {
    private final PartnerRepository partnerRepository;
    private final GeometryFactory geometryFactory;

    public PartnerResponseDTO getPartnerById(Long id) {
        Optional<Partner> partner = this.partnerRepository.findById(id);
        return partner.map(PartnerResponseDTO::new).orElseThrow(
            () -> new PartnerNotFoundException(id)
        );
    }

    public PartnerResponseDTO createPartner(PartnerRequestDTO data) {
        Partner partner = Partner.builder()
            .document(data.document())
            .tradingName(data.tradingName())
            .ownerName(data.ownerName())
            .coverageArea(this.convertGeoJsonToJtsMultiPolygon(data.coverageArea()))
            .address(this.convertGeoJsonToJtsPoint(data.address()))
            .build();

        this.partnerRepository.save(partner);

        return new PartnerResponseDTO(partner);
    }

    public MultiPolygon convertGeoJsonToJtsMultiPolygon(GeoJsonMultiPolygonDTO geoJsonDto) throws IllegalArgumentException {
        if (!geoJsonDto.getType().equalsIgnoreCase("MultiPolygon")) {
            throw new IllegalArgumentException("Input GeoJSON is not of type MultiPolygon");
        }

        List<Polygon> polygons = new ArrayList<>();

        for (List<List<List<Double>>> polygonCoordinates : geoJsonDto.getCoordinates()) {
            for (List<List<Double>> ringCoordinates : polygonCoordinates) {
                LinearRing shell = createLinearRing(ringCoordinates, geometryFactory);
                polygons.add(geometryFactory.createPolygon(shell, null));
            }
        }

        Polygon[] polygonArray = new Polygon[polygons.size()];
        return geometryFactory.createMultiPolygon(polygons.toArray(polygonArray));
    }

    private LinearRing createLinearRing(List<List<Double>> coordinates, GeometryFactory geometryFactory) {
        int size = coordinates.size();
        Coordinate[] coords = new Coordinate[size];
        for (int i = 0; i < size; i++) {
            List<Double> point = coordinates.get(i);
            coords[i] = new Coordinate(point.get(0), point.get(1));
        }
        return geometryFactory.createLinearRing(coords);
    }

    private Point convertGeoJsonToJtsPoint(GeoJsonPointDTO geoJsonDto) throws IllegalArgumentException {
        if (!geoJsonDto.type().equalsIgnoreCase("Point")) {
            throw new IllegalArgumentException("Input GeoJSON is not of type Point");
        }

        List<Double> coordinates = geoJsonDto.coordinates();
        if (coordinates.size() != 2) {
            throw new IllegalArgumentException("Point GeoJSON must have exactly 2 coordinates");
        }

        return geometryFactory.createPoint(new Coordinate(coordinates.get(0), coordinates.get(1)));
    }
}
