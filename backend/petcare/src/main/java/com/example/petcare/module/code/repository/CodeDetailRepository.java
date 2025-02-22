package com.example.petcare.module.code.repository;

import com.example.petcare.module.code.entity.CodeDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeDetailRepository extends JpaRepository<CodeDetail, String> {
}
