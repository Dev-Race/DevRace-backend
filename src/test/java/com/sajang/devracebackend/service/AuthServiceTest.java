package com.sajang.devracebackend.service;

import com.sajang.devracebackend.domain.Room;
import com.sajang.devracebackend.domain.User;
import com.sajang.devracebackend.domain.enums.Role;
import com.sajang.devracebackend.domain.mapping.UserRoom;
import com.sajang.devracebackend.repository.RoomRepository;
import com.sajang.devracebackend.repository.UserRepository;
import com.sajang.devracebackend.repository.UserRoomBatchRepository;
import com.sajang.devracebackend.repository.UserRoomRepository;
import com.sajang.devracebackend.security.jwt.TokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

// @SpringBootTest
public class AuthServiceTest {

    @Autowired
    private AuthService authService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private UserRoomRepository userRoomRepository;
    @Autowired
    private UserRoomBatchRepository userRoomBatchRepository;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private JdbcTemplate jdbcTemplate;


    // @Test
    @Transactional
    @DisplayName("회원탈퇴 - JDBC Batch Delete Test")  // UserRooms Batch Delete 시간 측정
    void withdrawal_Test() {

        // 기초 데이터 관리
        Integer inputUserRoomsCount = 10000;  // 10000개의 UserRooms 더미데이터 생성
        Long userId = 1L;  // user_id=1인 User 엔티티 활용 예정
        Long roomId = 1L;  // room_id=1인 Room 엔티티 활용 예정

        // UserRoom 더미데이터 생성
        Integer startUserRoomsCount = userRoomRepository.findAll().size();  // 초기 UserRooms 데이터 개수 측정
        List<UserRoom> userRoomList = makeFakeUserRooms(userId, roomId, inputUserRoomsCount);
        userRoomBatchRepository.batchInsert(userRoomList);

        // 로그인
        String jwt = tokenProvider.generateAccessToken(userId, Role.ROLE_USER);
        Authentication authentication = tokenProvider.getAuthentication(jwt);  // 사용자를 인증
        SecurityContextHolder.getContext().setAuthentication(authentication);  // SecurityContextHolder에 인증 정보를 설정

        // '회원탈퇴' -> UserRooms 더미데이터 삭제
        LocalDateTime startTime = LocalDateTime.now();  // 시작시각 기록
        authService.withdrawal();
        LocalDateTime endTime = LocalDateTime.now();  // 종료시각 기록
        Integer endUserRoomsCount = userRoomRepository.findAll().size();  // 삭제후 UserRooms 데이터 개수 측정

        // 실행시간 출력
        String printTime = getPrintTime(startTime, endTime);
        System.out.println("\n< JDBC Batch Delete 사용 (JPA deleteAll X, JPA deleteAllInBatch X) >");
        System.out.println(String.format("- %d개 방의 회원탈퇴 실행시간:", inputUserRoomsCount) + printTime);  // 출력

        // DB 롤백 검증
        assertThat(startUserRoomsCount).isEqualTo(endUserRoomsCount);  // UserRooms 더미데이터 삭제 검증
    }


    // ========== 유틸성 메소드 ========== //

    public List<UserRoom> makeFakeUserRooms(Long userId, Long roomId, int userRoomsCount) {  // UserRooms 더미데이터 생성
        User user = userRepository.findById(userId).orElseThrow();
        Room room = roomRepository.findById(roomId).orElseThrow();

        List<UserRoom> userRoomList = new ArrayList<>();
        for(int i=0; i<userRoomsCount; i++) {
            UserRoom userRoom = UserRoom.UserRoomSaveBuilder()
                    .user(user)
                    .room(room)
                    .build();
            userRoomList.add(userRoom);
        }
        return userRoomList;
    }

    public String getPrintTime(LocalDateTime startTime, LocalDateTime endTime) {  // 실행시간 출력 메세지 반환
        Duration duration = Duration.between(startTime, endTime);  // 메소드 실행시간 측정
        long milliseconds = duration.toMillis();  // 밀리초(ms)
        double seconds = milliseconds / 1000.0;  // 밀리초를 초로 변환(s)
        String printTime = String.format(" %dms (%.2fs)\n", milliseconds, seconds);
        return printTime;
    }
}
