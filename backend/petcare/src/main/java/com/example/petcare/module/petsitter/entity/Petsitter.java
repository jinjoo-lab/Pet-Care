package com.example.petcare.module.petsitter.entity;

import com.example.petcare.global.audit.AuditListener;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SQLRestriction("deleted_at IS NULL")
@EntityListeners(AuditListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Petsitter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String location;

    @Enumerated(EnumType.STRING)
    private Certified certified;

    @OneToMany(
            mappedBy = "petsitter",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<SitterSpecies> sitterSpecies = new ArrayList<>();

    public Petsitter(String location) {
        this.location = location;
    }

    public void updateCertified() {
        this.certified = Certified.YES;
    }
}
