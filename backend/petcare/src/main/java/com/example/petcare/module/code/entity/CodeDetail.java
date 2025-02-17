package com.example.petcare.module.code.entity;

import com.example.petcare.global.audit.AuditListener;
import com.example.petcare.global.audit.Auditable;
import com.example.petcare.global.audit.BaseEntity;
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
@Table(name = "code_detail")
public class CodeDetail implements Auditable {
    @Id
    private String id;

    @Embedded
    @Setter
    @Column(nullable = false)
    private BaseEntity baseEntity;

    private Boolean isActive;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_group_id")
    private CodeGroup codeGroup;

    public CodeDetail(String id, Boolean isActive, String name, CodeGroup codeGroup) {
        this.id = id;
        this.isActive = isActive;
        this.name = name;
        this.codeGroup = codeGroup;
    }

    public void updateDetailName(String name) {
        this.name = name;
    }

    public void updateActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
