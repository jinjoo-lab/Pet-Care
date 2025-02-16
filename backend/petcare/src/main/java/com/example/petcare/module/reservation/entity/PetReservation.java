package com.example.petcare.module.reservation.entity;

import com.example.petcare.global.audit.AuditListener;
import com.example.petcare.global.audit.Auditable;
import com.example.petcare.global.audit.BaseEntity;
import com.example.petcare.module.pet.entity.Pet;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@SQLRestriction("deleted_at IS NULL")
@EntityListeners(AuditListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PetReservation implements Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @Embedded
    @Setter
    @Column(nullable = false)
    private BaseEntity baseEntity;

    public PetReservation(Pet pet, Reservation reservation) {
        this.pet = pet;
        this.reservation = reservation;
    }
}
