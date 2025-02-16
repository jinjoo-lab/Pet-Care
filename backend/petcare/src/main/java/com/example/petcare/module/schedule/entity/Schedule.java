package com.example.petcare.module.schedule.entity;

import com.example.petcare.global.audit.AuditListener;
import com.example.petcare.global.audit.Auditable;
import com.example.petcare.global.audit.BaseEntity;
import com.example.petcare.module.petsitter.entity.Petsitter;
import com.example.petcare.module.reservation.entity.Reservation;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SQLRestriction("deleted_at IS NULL")
@EntityListeners(AuditListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule implements Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Petsitter petSitter;

    @OneToMany(
            mappedBy = "schedule",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}
    )
    List<Reservation> reservations = new ArrayList<>();

    private LocalDate date;

    private Integer startTime;

    private Integer endTime;

    private Integer timeFee;

    @Embedded
    @Setter
    @Column(nullable = false)
    private BaseEntity baseEntity;

    public Schedule(Petsitter petSitter, LocalDate date, Integer startTime, Integer endTime, Integer timeFee) {
        this.petSitter = petSitter;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.timeFee = timeFee;
    }
}
