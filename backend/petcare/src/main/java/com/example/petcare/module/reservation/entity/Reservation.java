package com.example.petcare.module.reservation.entity;

import com.example.petcare.global.audit.AuditListener;
import com.example.petcare.global.audit.Auditable;
import com.example.petcare.global.audit.BaseEntity;
import com.example.petcare.module.member.entity.Member;
import com.example.petcare.module.payment.entity.Payment;
import com.example.petcare.module.schedule.entity.Schedule;
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
public class Reservation implements Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private Integer startTime;
    private Integer endTime;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    private Schedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Embedded
    @Setter
    @Column(nullable = false)
    private BaseEntity baseEntity;

    @OneToMany(mappedBy = "reservation", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<PetReservation> petReservations = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    private String description;

    public Reservation(
            Schedule schedule,
            Member member,
            LocalDate date, Integer startTime,
            Integer endTime, ReservationStatus status,
            String description) {
        this.schedule = schedule;
        this.member = member;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.description = description;
    }

    public void updateStatus(ReservationStatus status) {
        this.status = status;
    }

    public void updatePayment(Payment payment) {
        this.payment = payment;
    }

    public void updateReservation(Integer startTime, Integer endTime, String description) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
    }
}
