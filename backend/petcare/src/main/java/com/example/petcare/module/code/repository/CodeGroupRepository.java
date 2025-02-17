package com.example.petcare.module.code.repository;

import com.example.petcare.module.code.entity.CodeGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeGroupRepository extends JpaRepository<CodeGroup, Long> {
}
