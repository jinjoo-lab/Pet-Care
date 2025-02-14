package com.example.petcare.schedule.entity;

import com.example.petcare.global.audit.AuditListener;
import com.example.petcare.module.petsitter.entity.Petsitter;
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
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Petsitter petSitter;

    private LocalDate date;

    private Integer startTime;

    private Integer endTime;

    private Integer timeFee;

    public Schedule(Petsitter petSitter, LocalDate date, Integer startTime, Integer endTime, Integer timeFee) {
        this.petSitter = petSitter;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.timeFee = timeFee;
    }
}
