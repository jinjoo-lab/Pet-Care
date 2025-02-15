package com.example.petcare.module.reservation.entity;

public enum ReservationStatus {

    REQUEST("요청"),
    APPROVE("승인"),
    PAID("결제 완료"),
    REJECT("거절"),
    CANCEL("취소");

    private String korean;

    ReservationStatus(String korean) {
        this.korean = korean;
    }

    public String getKorean() {
        return korean;
    }

}
