package com.example.petcare.schedule.mapper;

import com.example.petcare.module.petsitter.entity.Petsitter;
import com.example.petcare.module.petsitter.mapper.PetsitterMapper;
import com.example.petcare.schedule.dto.request.SaveScheduleRequest;
import com.example.petcare.schedule.dto.response.ScheduleResponse;
import com.example.petcare.schedule.entity.Schedule;
import org.springframework.stereotype.Component;

@Component
public class ScheduleMapper {

    PetsitterMapper petsitterMapper;

    public ScheduleMapper(PetsitterMapper petsitterMapper) {
        this.petsitterMapper = petsitterMapper;
    }

    public Schedule saveScheduleRequestToEntity(Petsitter sitter, SaveScheduleRequest request) {
        return new Schedule(
                sitter,
                request.getDate(),
                request.getStartTime(),
                request.getEndTime(),
                request.getTimeFee()
        );
    }

    public ScheduleResponse scheduleToScheduleResponse(Schedule schedule) {
        return new ScheduleResponse(
                schedule.getId(),
                petsitterMapper.petSitterToResponse(schedule.getPetSitter()),
                schedule.getDate(),
                schedule.getStartTime(),
                schedule.getEndTime(),
                schedule.getTimeFee()
        );
    }
}
