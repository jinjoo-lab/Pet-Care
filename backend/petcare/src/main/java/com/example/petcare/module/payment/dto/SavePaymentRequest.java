package com.example.petcare.module.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SavePaymentRequest {
    private String paymentCode;
    private Integer fee;
    private Long reservationId;
}
