package com.example.petcare.module.schedule.controller;

import com.example.petcare.module.schedule.dto.request.FindScheduleRequest;
import com.example.petcare.module.schedule.dto.request.SaveScheduleRequest;
import com.example.petcare.module.schedule.dto.request.UpdateScheduleRequest;
import com.example.petcare.module.schedule.dto.response.ScheduleResponse;
import com.example.petcare.module.schedule.dto.response.SimpleScheduleResponse;
import com.example.petcare.module.schedule.service.ScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/schedule")
public class ScheduleController {
    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping
    public ResponseEntity<ScheduleResponse> saveSchedule(@RequestBody SaveScheduleRequest request) {
        return ResponseEntity.ok(scheduleService.saveSchedule(request));
    }

    @GetMapping("/{id}/{date}")
    public ResponseEntity<ScheduleResponse> getSchedule(@PathVariable("id") Long id, @PathVariable("date") String date) {
        return ResponseEntity.ok(scheduleService.findScheduleBySitterIdAndDate(id,date));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponse> getScheduleById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(scheduleService.findScheduleById(id));
    }

    @GetMapping("/list/{id}/{date}")
    public ResponseEntity<List<ScheduleResponse>> findScheduleBySitterId(@PathVariable("id") Long petSitterId, @PathVariable("date") String date) {
        return ResponseEntity.ok(scheduleService.findSchedules(petSitterId,date));
    }

    @PostMapping("/list")
    public ResponseEntity<List<SimpleScheduleResponse>> findSchedules(@RequestBody FindScheduleRequest request) {
        return ResponseEntity.ok(scheduleService.findAllSchedules(request));
    }

    @PutMapping
    public ResponseEntity<ScheduleResponse> updateSchedule(@RequestBody UpdateScheduleRequest request) {
        return ResponseEntity.ok(scheduleService.updateSchedule(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable("id") Long id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }
}
