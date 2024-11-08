package com.example.schedule.dto;

import com.example.schedule.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ScheduleResponseDto {
    private Long schedule_id;
    private String title;
    private String contents;
    private String name;
    private String password;
    private LocalDateTime create_date;
    private LocalDateTime update_date;

    public ScheduleResponseDto(Schedule schedule) {
        this.schedule_id = schedule.getSchedule_id();
        this.title = schedule.getTitle();
        this.contents = schedule.getContents();
        this.name = schedule.getName();
        this.password = schedule.getPassword();
        this.create_date = schedule.getCreate_date();
        this.update_date = schedule.getUpdate_date();
    }

}