package com.example.petcare.global.audit;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;
import java.util.Optional;

public class AuditListener {

    @PrePersist
    public void setCreatedAt(final Auditable auditable) {
        BaseEntity baseEntity = Optional.ofNullable(auditable.getBaseEntity()).orElseGet(BaseEntity::new);
        baseEntity.setCreatedAt(LocalDateTime.now());
        auditable.setBaseEntity(baseEntity);
    }

    @PreUpdate
    public void setUpdatedAt(final Auditable auditable) {
        auditable.getBaseEntity().setUpdatedAt(LocalDateTime.now());
    }
}
