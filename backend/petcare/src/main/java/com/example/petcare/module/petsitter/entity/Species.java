package com.example.petcare.module.petsitter.entity;

import com.example.petcare.global.audit.AuditListener;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@SQLRestriction("deleted_at IS NULL")
@EntityListeners(AuditListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Species {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String topSpecies;

    private String subSpecies;

    public Species(String topSpecies, String subSpecies) {
        this.topSpecies = topSpecies;
        this.subSpecies = subSpecies;
    }

}
