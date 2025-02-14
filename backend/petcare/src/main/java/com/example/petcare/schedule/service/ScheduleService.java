package com.example.petcare.schedule.service;

import com.example.petcare.module.petsitter.entity.Petsitter;
import com.example.petcare.module.petsitter.service.PetsitterService;
import com.example.petcare.schedule.dto.request.SaveScheduleRequest;
import com.example.petcare.schedule.dto.response.ScheduleResponse;
import com.example.petcare.schedule.entity.Schedule;
import com.example.petcare.schedule.mapper.ScheduleMapper;
import com.example.petcare.schedule.repository.ScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.util.Date;

@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final PetsitterService petsitterService;
    private final ScheduleMapper scheduleMapper;

    public ScheduleService(ScheduleRepository scheduleRepository, PetsitterService petsitterService,
                           ScheduleMapper scheduleMapper) {
        this.scheduleRepository = scheduleRepository;
        this.petsitterService = petsitterService;
        this.scheduleMapper = scheduleMapper;
    }

    @Transactional
    public ScheduleResponse saveSchedule(SaveScheduleRequest request) {
        Petsitter petSitter = petsitterService.getPetSitterEntityById(request.getPetSitterId());

        Schedule schedule = scheduleRepository.save(
                scheduleMapper.saveScheduleRequestToEntity(
                        petSitter, request
                )
        );

        return scheduleMapper.scheduleToScheduleResponse(schedule);
    }

    @Transactional(readOnly = true)
    public void findScheduleBySitterIdAndDate(Long id, String date) {
        Petsitter petSitter = petsitterService.getPetSitterEntityById(id);

    }
}
