package com.example.schedule.repository;

import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.entity.Schedule;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcTemplateScheduleRepository implements ScheduleRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateScheduleRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public ScheduleResponseDto saveSchedule(Schedule schedule) {

        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert
                .withCatalogName("schedule") // ??
                .withTableName("schedule").usingGeneratedKeyColumns("schedule_id");
        // schedule이라는 테이블에 insert하겠다고 설정 + 이테이블의 키값은 id

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("title", schedule.getTitle());
        parameters.put("contents", schedule.getContents());
        parameters.put("name", schedule.getName());
        parameters.put("password", schedule.getPassword());
        parameters.put("create_date", schedule.getCreate_date());
        parameters.put("update_date", schedule.getUpdate_date());

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));

        return new ScheduleResponseDto(key.longValue(), schedule.getTitle(), schedule.getContents(), schedule.getName(), schedule.getPassword(), schedule.getCreate_date(), schedule.getUpdate_date());
    }

    @Override
    public List<ScheduleResponseDto> findAllSchedules() {
        return jdbcTemplate.query("select * from schedule", scheduleRowMapper());
    }

    @Override
    public Optional<Schedule> findScheduleById(Long schedule_id) {
        List<Schedule> result = jdbcTemplate.query("select * from schedule where schedule_id = ?", scheduleRowMapperV2(), schedule_id);// 맨뒤에 있는 id값이 플레이스 홀더 ?와 치환되면서 값이 됨
        return result.stream().findAny(); // 위 List형태의 결과를 optional로 바꿔 반환
    }

    @Override
    public Schedule findScheduleByIdOrElseThrow(Long schedule_id) {
        List<Schedule> result = jdbcTemplate.query("select * from schedule where schedule_id =?", scheduleRowMapperV2(), schedule_id);
        return result.stream().findAny().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + schedule_id));
    }


    @Override
    public int updateSchedule(Long schedule_id, String name, String contents) {
        return jdbcTemplate.update("update schedule set name = ?, contents = ? where schedule_id = ?", name, contents, schedule_id);
    }

    @Override
    public int deleteSchedule(Long schedule_id) {
        return jdbcTemplate.update("delete from schedule where schedule_id =?", schedule_id);
    }

    private RowMapper<ScheduleResponseDto> scheduleRowMapper () {
        return new RowMapper<ScheduleResponseDto>() {
            @Override
            public ScheduleResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new ScheduleResponseDto(
                        rs.getLong("schedule_id"), // rs는 결과 집합 result set의 약자
                        rs.getString("title"),
                        rs.getString("contents"),
                        rs.getString("name"),
                        rs.getString("password"),
                        rs.getTimestamp("create_date").toLocalDateTime(), // -> toLocalDateTime() 알아보기
                        rs.getTimestamp("update_date").toLocalDateTime()
                );
            }

        };
    }

    private RowMapper<Schedule> scheduleRowMapperV2 () {
        return new RowMapper<Schedule>() {
            @Override
            public Schedule mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Schedule(
                        rs.getLong("schedule_id"),
                        rs.getString("title"),
                        rs.getString("contents"),
                        rs.getString("name"),
                        rs.getString("password")
                );
            }

        };
    }

}


