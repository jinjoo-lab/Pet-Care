package com.example.petcare.module.pet.entity;

import com.example.petcare.global.audit.AuditListener;
import com.example.petcare.global.audit.Auditable;
import com.example.petcare.global.audit.BaseEntity;
import com.example.petcare.module.member.entity.Member;
import com.example.petcare.module.petsitter.entity.Species;
import com.example.petcare.module.reservation.entity.PetReservation;
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
public class Pet implements Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private PetType petType;

    private Integer age;

    private String photo;

    @Enumerated(EnumType.STRING)
    private PetSize size;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Embedded
    @Setter
    @Column(nullable = false)
    private BaseEntity baseEntity;

    @OneToMany(mappedBy = "pet", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<PetReservation> petReservations = new ArrayList<>();

    public Pet(String name, PetType petType, Integer age, String photo, PetSize size, Member member) {
        this.name = name;
        this.petType = petType;
        this.age = age;
        this.photo = photo;
        this.size = size;
        this.member = member;
    }
}
