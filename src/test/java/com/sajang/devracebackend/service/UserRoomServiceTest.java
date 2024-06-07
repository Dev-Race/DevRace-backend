package com.sajang.devracebackend.service;

import com.sajang.devracebackend.domain.User;
import com.sajang.devracebackend.domain.enums.SocialType;
import com.sajang.devracebackend.domain.mapping.UserRoom;
import com.sajang.devracebackend.repository.RoomRepository;
import com.sajang.devracebackend.repository.UserRepository;
import com.sajang.devracebackend.repository.UserRoomBatchRepository;
import com.sajang.devracebackend.repository.UserRoomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

// @SpringBootTest
public class UserRoomServiceTest {

    @Autowired
    private UserRoomService userRoomService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private UserRoomRepository userRoomRepository;
    @Autowired
    private UserRoomBatchRepository userRoomBatchRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;


    // @Test
    @Transactional
    @DisplayName("다중 사용자 동시입장 - Batch Insert Test")  // UserRooms Batch Insert 시간 측정
    void usersEnterRoom_Test() {

        // 기초 데이터 관리
        Integer inputUsersCount = 10000;  // 10000명의 Users 더미데이터 생성
        Long roomId = 1L;  // room_id=1인 Room 엔티티 활용 예정

        // Users 더미데이터 생성
        Integer startUserRoomsCount = userRoomRepository.findAll().size();  // 초기 UserRooms 데이터 개수 측정
        Integer startUsersCount = userRepository.findAll().size();  // 초기 Users 데이터 개수 측정
        List<User> userList = makeFakeUsers(inputUsersCount);
        userRepository.saveAll(userList);

        // waiting 속성 생성
        String userIdsString = userList.stream()
                .map(User::getId)
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        // Room의 waiting 업데이트
        String waiting = userIdsString;
        String sql = "UPDATE room SET waiting = ? WHERE room_id = ?";
        jdbcTemplate.update(sql, waiting, roomId);

        // waiting(입장 대기열)의 '다중 사용자 동시입장' -> UserRooms 더미데이터 생성
        LocalDateTime startTime = LocalDateTime.now();  // 시작시각 기록
        userRoomService.usersEnterRoom(roomId);
        LocalDateTime endTime = LocalDateTime.now();  // 종료시각 기록

        // 생성한 UserRooms 삭제 (자식 엔티티)
        List<UserRoom> userRoomList = roomRepository.findById(roomId).orElseThrow().getUserRoomList();
        deleteFakeUserRooms(userRoomList);
        Integer endUserRoomsCount = userRoomRepository.findAll().size();  // 삭제후 UserRooms 데이터 개수 측정

        // 생성한 Users 삭제 (부모 엔티티)
        deleteFakeUsers(userList);
        Integer endUsersCount = userRepository.findAll().size();  // 삭제후 User 데이터 개수 측정

        // 실행시간 출력
        String printTime = getPrintTime(startTime, endTime);
        System.out.println("\n< JDBC Batch Insert 사용 (JPA saveAll X) >");
        System.out.println(String.format("- %d명 동시입장 실행시간:", inputUsersCount) + printTime);  // 출력

        // DB 롤백 검증
        assertThat(startUserRoomsCount).isEqualTo(endUserRoomsCount);  // UserRooms 더미데이터 삭제 검증
        assertThat(startUsersCount).isEqualTo(endUsersCount);  // Users 더미데이터 삭제 검증
    }

    // ========== 유틸성 메소드 ========== //

    public List<User> makeFakeUsers(int usersCount) {  // Users 더미데이터 생성
        Long fakeNum = 1L;
        List<User> userList = new ArrayList<>();
        for(int i=0; i<usersCount; i++) {
            String fakeStr = String.valueOf(fakeNum + i);

            User user = User.UserSaveBuilder()
                    .email("test@email.com" + fakeStr)
                    .socialType(SocialType.GOOGLE)
                    .socialId("testSocialId" + fakeStr)
                    .nickname("testName" + fakeStr)
                    .imageUrl("testUrl" + fakeStr)
                    .build();
            userList.add(user);
        }
        return userList;
    }

    public void deleteFakeUsers(List<User> userList) {  // Users 더미데이터 삭제
        userRepository.deleteAll(userList);
    }

    public void deleteFakeUserRooms(List<UserRoom> userRoomList) {  // UserRooms 더미데이터 삭제
        userRoomBatchRepository.batchDelete(userRoomList);
    }

    public String getPrintTime(LocalDateTime startTime, LocalDateTime endTime) {  // 실행시간 출력 메세지 반환
        Duration duration = Duration.between(startTime, endTime);  // 메소드 실행시간 측정
        long milliseconds = duration.toMillis();  // 밀리초(ms)
        double seconds = milliseconds / 1000.0;  // 밀리초를 초로 변환(s)
        String printTime = String.format(" %dms (%.2fs)\n", milliseconds, seconds);
        return printTime;
    }
}
