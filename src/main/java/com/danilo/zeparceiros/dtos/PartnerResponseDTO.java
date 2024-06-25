package com.danilo.zeparceiros.dtos;

import com.danilo.zeparceiros.domain.partner.Partner;

public record PartnerResponseDTO(Long id, String document, String tradingName, String ownerName, GeoJsonMultiPolygonDTO coverageArea, GeoJsonPointDTO address) {
    public PartnerResponseDTO(Partner p) {
        this(
            p.getId(),
            p.getDocument(),
            p.getTradingName(),
            p.getOwnerName(),
            new GeoJsonMultiPolygonDTO(p.getCoverageArea()),
            new GeoJsonPointDTO(p.getAddress())
        );
    }
}
