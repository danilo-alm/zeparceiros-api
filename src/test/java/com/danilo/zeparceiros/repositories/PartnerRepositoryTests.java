package com.danilo.zeparceiros.repositories;

import com.danilo.zeparceiros.domain.partner.Partner;
import com.danilo.zeparceiros.utils.PartnerFactory;
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
    Partner partner1;
    Partner partner2;

    @BeforeEach
    public void setUp() {
        geometryFactory = new GeometryFactory();
        partner1 = PartnerFactory.createPartner(geometryFactory.createPoint(new Coordinate(0, 0)));
        partner2 = PartnerFactory.createPartner(geometryFactory.createPoint(new Coordinate(2, 2)));
    }

    @Test
    public void PartnerRepository_SaveAll_ReturnsSavedPartner() {
        partnerRepository.save(partner1);

        Assertions.assertNotNull(partner1);
        Assertions.assertTrue(partner1.getId() > 0);
    }

    @Test
    public void PartnerRepository_FindClosestPartners_ReturnsListContainingClosestPartners() {
        partnerRepository.save(partner1);
        partnerRepository.save(partner2);

        Pageable limit = PageRequest.of(0, 2);
        Point testLocation = geometryFactory.createPoint(new Coordinate(3, 3));

        List<Partner> closestPartners = partnerRepository.findClosestPartners(testLocation, limit);

        Assertions.assertNotNull(closestPartners);
        Assertions.assertEquals(2, closestPartners.size());
        Assertions.assertEquals(partner2.getDocument(), closestPartners.get(0).getDocument());
    }

    @Test
    public void PartnerRepository_FindClosestPartners_ReturnsEmptyList() {
        partnerRepository.save(partner1);
        partnerRepository.save(partner2);

        Pageable limit = PageRequest.of(0, 2);
        Point testLocation = geometryFactory.createPoint(new Coordinate(-1, -1));

        List<Partner> closestPartners = partnerRepository.findClosestPartners(testLocation, limit);

        Assertions.assertNotNull(closestPartners);
        Assertions.assertTrue(closestPartners.isEmpty());
    }
}
