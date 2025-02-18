package com.example.petcare.module.petsitter.entity;

import com.example.petcare.global.audit.AuditListener;
import com.example.petcare.global.audit.Auditable;
import com.example.petcare.global.audit.BaseEntity;
import com.example.petcare.module.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SQLRestriction("deleted_at IS NULL")
@EntityListeners(AuditListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Petsitter implements Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String location;

    @Enumerated(EnumType.STRING)
    private Certified certified;

    private Integer startTime;

    private Integer endTime;

    private Integer fee;


    @OneToOne(fetch = FetchType.LAZY, mappedBy = "petsitter")
    private Member member;

    @Embedded
    @Setter
    @Column(nullable = false)
    private BaseEntity baseEntity;

    @OneToMany(
            mappedBy = "petsitter",
            cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE})
    private List<SitterSpecies> sitterSpecies = new ArrayList<>();

    @OneToMany(
            mappedBy = "petsitter",
            cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE}
    )
    private List<SitterCare> sitterCare = new ArrayList<>();

    public Petsitter(String location, Integer startTime, Integer endTime, Integer fee) {
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
        this.fee = fee;
    }

    public void updatePetsitter
            (String location, Integer startTime,
             Integer endTime, Integer fee
             ) {
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
        this.fee = fee;
    }

    public void updateMember(Member member) {
        this.member = member;
        member.updatePetsitter(this);
    }

    public void updateCertified() {
        this.certified = Certified.YES;
    }
}
