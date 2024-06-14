package com.sajang.devracebackend.repository;

import com.sajang.devracebackend.domain.mapping.UserRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRoomBatchRepository {  // 대용량 데이터의 batch 처리를 위한 JDBC Repository

    private final JdbcTemplate jdbcTemplate;


    public void batchInsert(List<UserRoom> userRoomList) {
        String sql = "INSERT INTO user_room (language, code, is_pass, is_leave, leave_time, created_time, modified_time, user_id, room_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                UserRoom userRoom = userRoomList.get(i);
                ps.setString(1, userRoom.getLanguage().name());
                ps.setString(2, userRoom.getCode());  // setString 메소드는 자동으로 null을 처리해줌.
                ps.setInt(3, userRoom.getIsPass());
                ps.setInt(4, userRoom.getIsLeave());
                ps.setTimestamp(5, userRoom.getLeaveTime() != null ? Timestamp.valueOf(userRoom.getLeaveTime()) : null);
                ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));  // JDBC 사용으로 JPA Auditing가 작동하지 않기 때문에, 직접 넣어줘야함.
                ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));  // JDBC 사용으로 JPA Auditing가 작동하지 않기 때문에, 직접 넣어줘야함.
                ps.setLong(8, userRoom.getUser().getId());
                ps.setLong(9, userRoom.getRoom().getId());
            }

            @Override
            public int getBatchSize() {
                return userRoomList.size();
            }
        });
    }

    public void batchDelete(List<UserRoom> userRoomList) {
        String sql = "DELETE FROM user_room WHERE user_room_id = ?";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                UserRoom userRoom = userRoomList.get(i);
                ps.setLong(1, userRoom.getId());
            }

            @Override
            public int getBatchSize() {
                return userRoomList.size();
            }
        });
    }
}
