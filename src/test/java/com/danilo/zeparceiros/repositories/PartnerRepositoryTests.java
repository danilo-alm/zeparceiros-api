package com.danilo.zeparceiros.repositories;

import com.danilo.zeparceiros.domain.partner.Partner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Transactional
public class PartnerRepositoryTests {
    @Autowired
    PartnerRepository partnerRepository;
    GeometryFactory geometryFactory;

    private Partner createPartner(String tradingName, String document, String ownerName, Point address, MultiPolygon coverageArea) {
        return Partner.builder().tradingName(tradingName).document(document).ownerName(ownerName).address(address).coverageArea(coverageArea).build();
    }

    private List<Partner> getTestPartners() {
        Coordinate[] coordinates1 = new Coordinate[] {
            new Coordinate(0, 0),
            new Coordinate(0, 5),
            new Coordinate(5, 5),
            new Coordinate(5, 0),
            new Coordinate(0, 0)
        };
        MultiPolygon coverageArea1 = geometryFactory.createMultiPolygon(new Polygon[]{geometryFactory.createPolygon(coordinates1)});

        Coordinate[] coordinates2 = new Coordinate[]{
            new Coordinate(1, 1),
            new Coordinate(1, 4),
            new Coordinate(4, 4),
            new Coordinate(4, 1),
            new Coordinate(1, 1)
        };
        MultiPolygon coverageArea2 = geometryFactory.createMultiPolygon(new Polygon[]{geometryFactory.createPolygon(coordinates2)});

        Partner partner1 = createPartner("Partner 1", "Document 1", "Owner 1", geometryFactory.createPoint(new Coordinate(1, 1)), coverageArea1);
        Partner partner2 = createPartner("Partner 2", "Document 2", "Owner 2", geometryFactory.createPoint(new Coordinate(2, 2)), coverageArea2);

        return List.of(partner1, partner2);
    }

    @BeforeEach
    public void setUp() {
        geometryFactory = new GeometryFactory();
    }

    @Test
    public void PartnerRepository_SaveAll_ReturnsSavedPartner() {
        Partner partner = this.getTestPartners().get(0);
        partnerRepository.save(partner);

        Assertions.assertNotNull(partner);
        Assertions.assertTrue(partner.getId() > 0);
    }

    @Test
    public void PartnerRepository_FindClosestPartners_ReturnsListContainingClosestPartners() {
        partnerRepository.saveAll(this.getTestPartners());

        Pageable limit = PageRequest.of(0, 2);
        Point testLocation = geometryFactory.createPoint(new Coordinate(3, 3));

        List<Partner> closestPartners = partnerRepository.findClosestPartners(testLocation, limit);

        Assertions.assertNotNull(closestPartners);
        Assertions.assertEquals(2, closestPartners.size());
        Assertions.assertEquals("Partner 2", closestPartners.get(0).getTradingName());
    }

    @Test
    public void PartnerRepository_FindClosestPartners_ReturnsEmptyList() {
        partnerRepository.saveAll(this.getTestPartners());

        Pageable limit = PageRequest.of(0, 2);
        Point testLocation = geometryFactory.createPoint(new Coordinate(6, 6));

        List<Partner> closestPartners = partnerRepository.findClosestPartners(testLocation, limit);

        Assertions.assertNotNull(closestPartners);
        Assertions.assertTrue(closestPartners.isEmpty());
    }
}
