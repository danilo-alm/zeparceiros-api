package com.danilo.zeparceiros.domain.partner;

import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;

@Entity
@Table
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Partner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String document;

    private String tradingName;
    private String ownerName;

    private MultiPolygon coverageArea;
    private Point address;
}
