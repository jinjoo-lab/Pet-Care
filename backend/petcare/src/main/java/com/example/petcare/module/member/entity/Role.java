package com.example.petcare.module.member.entity;

public enum Role {
    MEMBER("사용자"), ADMIN("관리자");

    private String korean;

    Role(String korean) {
        this.korean = korean;
    }
}
