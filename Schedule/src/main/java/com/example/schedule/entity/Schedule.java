package com.example.schedule.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;


import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Schedule {
    private Long schedule_id;
    private String title;
    private String contents;
    private String name;
    private String password;
    private LocalDateTime create_date;
    private LocalDateTime update_date;

    public Schedule(String title, String contents, String name, String password) {
        this.title = title;
        this.contents = contents;
        this.name = name;
        this.password = password;
        this.create_date = LocalDateTime.now();
        this.update_date = LocalDateTime.now();
    }

    public Schedule(long schedule_id, String title, String contents, String name, String password) {
        this.schedule_id = schedule_id;
        this.title = title;
        this.contents = contents;
        this.name = name;
        this.password = password;

    }


    public void updateSchedule(Long schedule_id, String name, String contents) {
        this.schedule_id = schedule_id;
        this.name = name;
        this.contents = contents;

    }


}
