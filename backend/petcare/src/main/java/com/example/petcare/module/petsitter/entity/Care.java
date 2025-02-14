package com.example.petcare.module.petsitter.entity;

import com.example.petcare.global.audit.AuditListener;
import com.example.petcare.global.audit.Auditable;
import com.example.petcare.global.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SQLRestriction("deleted_at IS NULL")
@EntityListeners(AuditListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Care implements Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(
            mappedBy = "service",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}
    )
    private List<SitterCare> sitterCare = new ArrayList<>();

    @Embedded
    @Setter
    @Column(nullable = false)
    private BaseEntity baseEntity;

    public Care(String name) {
        this.name = name;
    }
}
