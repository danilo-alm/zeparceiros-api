package com.danilo.zeparceiros.repositories;

import com.danilo.zeparceiros.domain.partner.Partner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartnerRepository extends JpaRepository<Partner, Long> {
}
