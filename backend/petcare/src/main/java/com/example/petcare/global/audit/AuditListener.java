package com.example.petcare.global.audit;

import java.time.LocalDateTime;
import java.util.Optional;

public class AuditListener {

    public void setCreatedAt(final Auditable auditable) {
        BaseEntity baseEntity = Optional.ofNullable(auditable.getBaseEntity()).orElseGet(BaseEntity::new);
        baseEntity.setCreatedAt(LocalDateTime.now());
        auditable.setBaseEntity(baseEntity);
    }

    public void setUpdatedAt(final Auditable auditable) {
        auditable.getBaseEntity().setUpdatedAt(LocalDateTime.now());
    }

    public void setDeletedAt(final Auditable auditable) {
        auditable.getBaseEntity().setDeletedAt(LocalDateTime.now());
    }
}
