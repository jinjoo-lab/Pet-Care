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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@SQLRestriction("deleted_at IS NULL")
@EntityListeners(AuditListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "code_group")
public class CodeGroup implements Auditable {
    @Id
    private String id;

    @Embedded
    @Setter
    @Column(nullable = false)
    private BaseEntity baseEntity;

    private String name;

    @OneToMany(
            mappedBy = "codeGroup",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}
    )
    private List<CodeDetail> codeDetails = new ArrayList<>();

    public CodeGroup(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public void updateCodeGroup(String name) {
        this.name = name;
    }
}
