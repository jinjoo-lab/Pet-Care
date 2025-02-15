package com.example.petcare.schedule.controller;

import com.example.petcare.schedule.dto.request.SaveScheduleRequest;
import com.example.petcare.schedule.dto.response.ScheduleResponse;
import com.example.petcare.schedule.service.ScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


}
