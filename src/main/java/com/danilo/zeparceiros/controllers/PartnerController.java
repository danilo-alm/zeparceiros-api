package com.danilo.zeparceiros.controllers;

import com.danilo.zeparceiros.dtos.PartnerRequestDTO;
import com.danilo.zeparceiros.dtos.PartnerResponseDTO;
import com.danilo.zeparceiros.services.PartnerService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/partners")
@AllArgsConstructor
public class PartnerController {
    private final PartnerService partnerService;

    @GetMapping("/{id}")
    public ResponseEntity<PartnerResponseDTO> getPartnerById(@PathVariable Long id) {
        PartnerResponseDTO partner = this.partnerService.getPartnerById(id);
        return ResponseEntity.ok().body(partner);
    }

    @PostMapping
    public ResponseEntity<PartnerResponseDTO> createPartner(@RequestBody PartnerRequestDTO data) {
        PartnerResponseDTO partner = this.partnerService.createPartner(data);
        URI uri = URI.create("/api/partners/" + partner.id());
        return ResponseEntity.created(uri).body(partner);
    }
}
