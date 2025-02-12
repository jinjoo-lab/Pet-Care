package com.example.petcare.global.audit;

public interface Auditable {
    BaseEntity getBaseEntity();
    void setBaseEntity(BaseEntity baseEntity);
}
