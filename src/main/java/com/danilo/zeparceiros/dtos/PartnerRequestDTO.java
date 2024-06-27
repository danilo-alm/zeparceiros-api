package com.danilo.zeparceiros.dtos;

public record PartnerRequestDTO(String document, String tradingName, String ownerName, GeoJsonMultiPolygonDTO coverageArea, GeoJsonPointDTO address) {
    public PartnerRequestDTO() {
        this("", "", "", new GeoJsonMultiPolygonDTO(), new GeoJsonPointDTO());
    }
}
