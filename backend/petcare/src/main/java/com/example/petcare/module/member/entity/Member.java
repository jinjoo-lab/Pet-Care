package com.example.petcare.module.member.entity;

import com.example.petcare.global.audit.AuditListener;
import com.example.petcare.global.audit.Auditable;
import com.example.petcare.global.audit.BaseEntity;
import com.example.petcare.module.petsitter.entity.Petsitter;
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
public class Member implements Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    private String name;
    private String email;
    private String password;
    private String phone;
    @Enumerated(EnumType.STRING)
    private Role role;

    @Embedded
    @Setter
    @Column(nullable = false)
    private BaseEntity baseEntity;

    @OneToOne
    @JoinColumn(name = "petsitter_id")
    private Petsitter petsitter;

    public Member(String name, String email, String password, String phone) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = Role.MEMBER;
    }

    public void updateMember(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public void updatePetsitter(Petsitter petsitter) {
        this.petsitter = petsitter;
    }

    public void mandateAdmin() {
        this.role = Role.ADMIN;
    }

    public void updatePassword(String password) {
        this.password = password;
    }
}

