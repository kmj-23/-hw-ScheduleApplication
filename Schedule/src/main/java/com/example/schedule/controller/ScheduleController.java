package com.example.schedule.controller;

import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.service.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // @Controller + @ResponseBody
@RequestMapping("/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;}

    @PostMapping
    // RequestBody를 통해 요청을 받고
    public ResponseEntity<ScheduleResponseDto> createSchedule(@RequestBody ScheduleRequestDto dto) {
        // Service Layer 호출, 응답
        return new ResponseEntity<>(scheduleService.saveSchedule(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public List<ScheduleResponseDto> findAllMemos() {

        return scheduleService.findAllSchedules();

    }

    @GetMapping("/{schedule_id}")
    public ResponseEntity<ScheduleResponseDto> findScheduleById(@PathVariable Long schedule_id) {
        return new ResponseEntity<>(scheduleService.findScheduleById(schedule_id), HttpStatus.OK);

    }

    @PutMapping("/{schedule_id}")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(
            //파라미터 지정
            @PathVariable Long schedule_id,
            @RequestBody ScheduleRequestDto dto
    ) {
        return new ResponseEntity<>(scheduleService.updateSchedule(schedule_id, dto.getName(), dto.getContents()), HttpStatus.OK);
    }

    @DeleteMapping("/{schedule_id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long schedule_id) {
        scheduleService.deleteSchedule(schedule_id);

        return new ResponseEntity<>(HttpStatus.OK);
    }


}

