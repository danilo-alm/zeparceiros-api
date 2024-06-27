package com.danilo.zeparceiros.services;

import com.danilo.zeparceiros.domain.partner.Partner;
import com.danilo.zeparceiros.dtos.GeoJsonMultiPolygonDTO;
import com.danilo.zeparceiros.dtos.GeoJsonPointDTO;
import com.danilo.zeparceiros.dtos.PartnerRequestDTO;
import com.danilo.zeparceiros.dtos.PartnerResponseDTO;
import com.danilo.zeparceiros.exceptions.AreaNotCoveredException;
import com.danilo.zeparceiros.exceptions.PartnerNotFoundException;
import com.danilo.zeparceiros.repositories.PartnerRepository;
import com.danilo.zeparceiros.utils.PartnerFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;

@ExtendWith(MockitoExtension.class)
public class PartnerServiceTests {
    @Mock
    private PartnerRepository partnerRepository;

    @Mock
    private GeometryFactory geometryFactoryMock;

    @InjectMocks
    private PartnerService partnerService;

    private GeometryFactory geometryFactory;

    @BeforeEach
    public void setUp() {
        this.geometryFactory = new GeometryFactory();
    }

    @Test
    public void PartnerService_GetPartnerById_ReturnsPartnerResponseDtoWithId() {
        when(partnerRepository.findById(eq(1L))).thenReturn(Optional.of(PartnerFactory.createPartner()));
        PartnerResponseDTO partnerResponseDTO = partnerService.getPartnerById(1L);
        Assertions.assertNotNull(partnerResponseDTO);
    }

    @Test
    public void PartnerService_GetPartnerById_InvalidId_ThrowsPartnerNotFoundException() {
        when(partnerRepository.findById(eq(1L))).thenReturn(Optional.empty());
        Assertions.assertThrows(PartnerNotFoundException.class, () -> partnerService.getPartnerById(1L));
    }

    @Test
    public void PartnerService_CreatePartner_ReturnsPartnerResponseDto() {
        when(partnerRepository.save(any(Partner.class))).thenReturn(PartnerFactory.createPartner());
        when(geometryFactoryMock.createMultiPolygon(any(Polygon[].class))).thenReturn(geometryFactory.createMultiPolygon(new Polygon[]{}));
        when(geometryFactoryMock.createPoint(any(Coordinate.class))).thenReturn(geometryFactory.createPoint(new Coordinate(0, 0)));

        PartnerResponseDTO partnerResponseDTO = this.partnerService.createPartner(new PartnerRequestDTO());

        Assertions.assertNotNull(partnerResponseDTO);
    }

    @Test
    public void PartnerService_CreatePartner_InvalidAddress_ThrowsIllegalArgumentException() {
        PartnerRequestDTO partnerRequestDTO = new PartnerRequestDTO(
            "",
            "",
            "",
            new GeoJsonMultiPolygonDTO(),
            new GeoJsonPointDTO("Not a Point", Collections.emptyList())
            );

        Assertions.assertThrows(IllegalArgumentException.class, () -> this.partnerService.createPartner(partnerRequestDTO));
    }

    @Test
    public void PartnerService_CreatePartner_AddressWithThreeCoordinates_ThrowsIllegalArgumentException() {
        PartnerRequestDTO partnerRequestDTO = new PartnerRequestDTO(
            "",
            "",
            "",
            new GeoJsonMultiPolygonDTO(),
            new GeoJsonPointDTO("Point", List.of(0.0, 0.0, 0.0))
        );

        Assertions.assertThrows(IllegalArgumentException.class, () -> this.partnerService.createPartner(partnerRequestDTO));
    }

    @Test
    public void PartnerService_CreatePartner_InvalidCoverageArea_ThrowsIllegalArgumentException() {
        PartnerRequestDTO partnerRequestDTO = new PartnerRequestDTO(
            "",
            "",
            "",
            new GeoJsonMultiPolygonDTO("Not a MultiPolygon", Collections.emptyList()),
            new GeoJsonPointDTO()
        );

        Assertions.assertThrows(IllegalArgumentException.class, () -> this.partnerService.createPartner(partnerRequestDTO));
    }

    @Test
    public void PartnerService_SearchPartner_ReturnsPartnerResponseDTO() {
        Partner partner = PartnerFactory.createPartner();
        Point point = geometryFactory.createPoint(new Coordinate(0, 0));
        Pageable limit = PageRequest.of(0, 1);

        when(geometryFactoryMock.createPoint(any(Coordinate.class))).thenReturn(point);
        when(partnerRepository.findClosestPartners(eq(point), eq(limit))).thenReturn(List.of(partner));

        PartnerResponseDTO responseDTO = partnerService.searchPartner(1.0, 1.0);

        Assertions.assertNotNull(responseDTO);
        Assertions.assertEquals(partner.getDocument(), responseDTO.document());
    }

    @Test
    public void PartnerService_SearchPartner_NoPartnerFound_ThrowsAreaNotCoveredException() {
        Point point = geometryFactory.createPoint(new Coordinate(0, 0));
        Pageable limit = PageRequest.of(0, 1);

        when(geometryFactoryMock.createPoint(any(Coordinate.class))).thenReturn(point);
        when(partnerRepository.findClosestPartners(eq(point), eq(limit))).thenReturn(Collections.emptyList());

        Assertions.assertThrows(AreaNotCoveredException.class, () -> partnerService.searchPartner(1.0, 1.0));
    }
}
