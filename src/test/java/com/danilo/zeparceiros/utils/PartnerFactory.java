package com.danilo.zeparceiros.utils;

import com.danilo.zeparceiros.domain.partner.Partner;
import org.locationtech.jts.geom.*;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class PartnerFactory {
    private static final GeometryFactory geometryFactory = new GeometryFactory();
    private static final Random random = new Random();

    public static Partner createPartner(String tradingName, String document, String ownerName, Point address, MultiPolygon coverageArea) {
        return Partner.builder()
            .document(document)
            .tradingName(tradingName)
            .ownerName(ownerName)
            .coverageArea(coverageArea)
            .address(address)
            .build();
    }

    public static Partner createPartner(String tradingName, String document, String ownerName, Point address) {
        double x = address.getX();
        double y = address.getY();

        // Square coverageArea e.g.: x=0,y=0 -> (0,0),(0,5),(5,5),(5,0),(0,0)
        double offset = 5;
        Coordinate[] coordinates = new Coordinate[] {
            new Coordinate(x, y),
            new Coordinate(x, y + offset),
            new Coordinate(x + offset, y + offset),
            new Coordinate(x + offset, y),
            new Coordinate(x, y)
        };

        MultiPolygon coverageArea = geometryFactory.createMultiPolygon(new Polygon[]{geometryFactory.createPolygon(coordinates)});
        return PartnerFactory.createPartner(tradingName, document, ownerName, address, coverageArea);
    }

    public static Partner createPartner(Point address) {
        return PartnerFactory.createPartner("", String.valueOf(random.nextInt()), "", address);
    }

    public static Partner createPartner() {
        return PartnerFactory.createPartner(geometryFactory.createPoint(new Coordinate(0, 0)));
    }
}