package com.example.petcare.module.reservation.entity;

import com.example.petcare.global.audit.AuditListener;
import com.example.petcare.module.member.entity.Member;
import com.example.petcare.module.schedule.entity.Schedule;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;

@Entity
@Getter
@SQLRestriction("deleted_at IS NULL")
@EntityListeners(AuditListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation {
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

    public Reservation(
            Schedule schedule,
            Member member,
            LocalDate date, Integer startTime,
            Integer endTime, ReservationStatus status) {
        this.schedule = schedule;
        this.member = member;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }
}
