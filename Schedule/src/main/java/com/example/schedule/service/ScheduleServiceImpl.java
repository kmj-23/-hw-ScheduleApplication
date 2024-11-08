package com.example.schedule.service;

import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.entity.Schedule;
import com.example.schedule.repository.ScheduleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }


    @Override
    public ScheduleResponseDto saveSchedule(ScheduleRequestDto dto) {

        Schedule schedule = new Schedule(dto.getTitle(), dto.getContents(), dto.getName(), dto.getPassword());

        // DB 저장
        return scheduleRepository.saveSchedule(schedule);
    }

    @Override
    public List<ScheduleResponseDto> findAllSchedules() {
        return scheduleRepository.findAllSchedules();
    }

    @Override
    public ScheduleResponseDto findScheduleById(Long id) {
        Schedule schedule = scheduleRepository.findScheduleByIdOrElseThrow(id); // 메모가 있는 경우에만 반환

        return new ScheduleResponseDto(schedule);
    }

    @Transactional // 밑에 있는 updatSchedule 파트가 논리있는 작업단위로 묶여
    @Override
    public ScheduleResponseDto updateSchedule(Long schedule_id, String name, String contents) {
        if (name == null || contents == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The title and content are required values.");
        }

        int updatedRow = scheduleRepository.updateSchedule(schedule_id, name, contents);

        // 이 메모를 검증
        if (updatedRow == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + schedule_id);
        }

        Schedule schedule = scheduleRepository.findScheduleByIdOrElseThrow(schedule_id);

        return new ScheduleResponseDto(schedule);
    }

    @Override
    public void deleteSchedule(Long schedule_id) {
        int deletedRow = scheduleRepository.deleteSchedule(schedule_id);

        if (deletedRow ==0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + schedule_id);
        }
    }
}
