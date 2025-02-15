package com.example.petcare.module.reservation.dto.response;

import com.example.petcare.module.member.dto.response.MemberResponse;
import com.example.petcare.module.pet.dto.response.PetResponse;
import com.example.petcare.module.reservation.entity.ReservationStatus;
import com.example.petcare.module.schedule.dto.response.ScheduleResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SimpleReservationResponse {
    private Long id;
    private ReservationStatus status;
    private MemberResponse member;
    private List<PetResponse> pets;
}
