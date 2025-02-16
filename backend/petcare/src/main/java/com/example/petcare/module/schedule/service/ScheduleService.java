package com.example.petcare.module.schedule.service;

import com.example.petcare.module.petsitter.entity.Petsitter;
import com.example.petcare.module.petsitter.service.PetsitterService;
import com.example.petcare.module.schedule.dto.request.FindScheduleRequest;
import com.example.petcare.module.schedule.dto.response.ScheduleResponse;
import com.example.petcare.module.schedule.dto.request.SaveScheduleRequest;
import com.example.petcare.module.schedule.dto.response.SimpleScheduleResponse;
import com.example.petcare.module.schedule.entity.Schedule;
import com.example.petcare.module.schedule.mapper.ScheduleMapper;
import com.example.petcare.module.schedule.repository.ScheduleRepository;
import jakarta.persistence.EntityExistsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


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

        Schedule schedule = scheduleMapper.saveScheduleRequestToEntity(petSitter, request);

        scheduleRepository.save(
                scheduleMapper.saveScheduleRequestToEntity(
                        petSitter, request
                )
        );

        return scheduleMapper.scheduleToScheduleResponse(schedule);
    }

    @Transactional(readOnly = true)
    public ScheduleResponse findScheduleBySitterIdAndDate(Long id, String date) {
        Petsitter petSitter = petsitterService.getPetSitterEntityById(id);
        LocalDate convertDate = convertDate(date);

        Schedule schedule = scheduleRepository.findByPetSitterAndDate(
                petSitter, convertDate
        ).orElseThrow(EntityExistsException::new);

        return scheduleMapper.scheduleToScheduleResponse(schedule);
    }

    @Transactional(readOnly = true)
    public ScheduleResponse findScheduleById(Long id) {
        Schedule schedule = scheduleRepository.findById(id).orElseThrow(EntityExistsException::new);
        return scheduleMapper.scheduleToScheduleResponse(schedule);
    }

    @Transactional(readOnly = true)
    public List<ScheduleResponse> findSchedules(Long petSitterId, String date) {
        Petsitter petSitter = petsitterService.getPetSitterEntityById(petSitterId);
        LocalDate convertDate = convertDate(date);

        List<Schedule> schedules = scheduleRepository.findAllByPetSitterAndDate(
                petSitter, convertDate
        );

        return schedules.stream().map(scheduleMapper::scheduleToScheduleResponse).toList();
    }

    public Schedule getScheduleById(Long id) {
        return scheduleRepository.findById(id).orElseThrow(EntityExistsException::new);
    }

    private LocalDate convertDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date, formatter);
    }

    @Transactional(readOnly = true)
    public List<SimpleScheduleResponse> findAllSchedules(FindScheduleRequest request) {
        return scheduleRepository.findScheduleByRequest(
                request.getLocation(),
                convertDate(request.getDate()),
                request.getStartTime(),
                request.getEndTime()
        ).stream().map(
                s -> scheduleMapper.scheduleToSimpleReservationResponse(s)
        ).toList();
    }
}
