package com.example.petcare.module.payment.controller;

import com.example.petcare.module.payment.dto.SavePaymentRequest;
import com.example.petcare.module.payment.service.PaymentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {
    private PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public void savePayment(@RequestBody SavePaymentRequest payment) {
        paymentService.savePayment(payment);
    }
}
