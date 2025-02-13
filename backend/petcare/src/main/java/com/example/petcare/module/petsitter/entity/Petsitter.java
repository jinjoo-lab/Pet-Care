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
public class Petsitter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String location;
    private String name;
    @Enumerated(EnumType.STRING)
    private Certified certified;


    public Petsitter(String location, String name) {
        this.location = location;
        this.name = name;
    }

    public void updateCertified() {
        this.certified = Certified.YES;
    }
}
