package com.danilo.zeparceiros.repositories;

import com.danilo.zeparceiros.domain.partner.Partner;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PartnerRepository extends JpaRepository<Partner, Long> {
    @Query("""
            SELECT p FROM Partner p
            WHERE ST_Contains(ST_SetSRID(p.coverageArea, 4326), ST_SetSRID(:location, 4326)) = true
            ORDER BY ST_Distance(p.address, ST_SetSRID(:location, 4326)) ASC""")
    List<Partner> findClosestPartners(@Param("location") Point location, Pageable pageable);
}
