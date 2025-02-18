package com.example.petcare.module.payment.service;

import com.example.petcare.module.payment.dto.SavePaymentRequest;
import com.example.petcare.module.payment.entity.Payment;
import com.example.petcare.module.payment.repository.PaymentRepository;
import com.example.petcare.module.reservation.entity.Reservation;
import com.example.petcare.module.reservation.service.ReservationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {
    private PaymentRepository paymentRepository;
    private ReservationService reservationService;

    public PaymentService(PaymentRepository paymentRepository, ReservationService reservationService) {
        this.paymentRepository = paymentRepository;
        this.reservationService = reservationService;
    }

    @Transactional
    public void savePayment(SavePaymentRequest request) {

        Reservation reservation = reservationService.getReservationById(request.getReservationId());

        Payment payment = paymentRepository.save(new Payment(
                request.getPaymentCode().toString() , request.getFee(), reservation
        ));

        reservation.updatePayment(payment);
    }
}
