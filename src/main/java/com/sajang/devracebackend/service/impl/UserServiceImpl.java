package com.sajang.devracebackend.service.impl;

import com.sajang.devracebackend.domain.User;
import com.sajang.devracebackend.domain.mapping.UserRoom;
import com.sajang.devracebackend.dto.user.UserCheckRoomResponseDto;
import com.sajang.devracebackend.dto.user.UserResponseDto;
import com.sajang.devracebackend.dto.user.UserSolvedResponseDto;
import com.sajang.devracebackend.dto.user.UserUpdateRequestDto;
import com.sajang.devracebackend.repository.UserRepository;
import com.sajang.devracebackend.response.exception.exception404.NoSuchBojIdException;
import com.sajang.devracebackend.response.exception.exception404.NoSuchUserException;
import com.sajang.devracebackend.service.AwsS3Service;
import com.sajang.devracebackend.service.UserService;
import com.sajang.devracebackend.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final AwsS3Service awsS3Service;
    private final UserRepository userRepository;


    @Transactional(readOnly = true)
    @Override
    public User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                ()->new NoSuchUserException(String.format("userId = %d", userId)));
    }

    @Transactional(readOnly = true)
    @Override
    public User findLoginUser() {
        Long loginUserId = SecurityUtil.getCurrentMemberId();
        User loginUser = findUser(loginUserId);
        return loginUser;
    }

    @Transactional(readOnly = true)
    @Override
    public <T> List<T> findUsersOriginal(List<Long> userIdList, Boolean isDto) {  // 기존 userId리스트의 순서를 보장하는 User&UserDto리스트 반환 메소드
        List<User> userList = userRepository.findByIdIn(userIdList);
        Map<Long, User> userMap = userList.stream()
                .collect(Collectors.toMap(User::getId, user -> user));

        return userIdList.stream()
                .map(userMap::get)
                .map(user -> isDto ? (T) new UserResponseDto(user) : (T) user)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public UserResponseDto findUserProfile() {
        User user = findLoginUser();
        UserResponseDto userResponseDto = new UserResponseDto(user);
        return userResponseDto;
    }

    @Transactional
    @Override
    public void updateUserProfile(MultipartFile imageFile, UserUpdateRequestDto userUpdateRequestDto) throws IOException {

        // < 회원 프로필 사진 변경조건 >
        // - 사진 변경X : if 'imageFile == null && signupRequestDto.getIsImageChange() == 0' --> AWS S3 업로드X
        // - 사진 변경O : if 'imageFile != null && signupRequestDto.getIsImageChange() == 1' --> AWS S3 업로드O
        // - 기본사진으로 변경O : if 'imageFile == null && signupRequestDto.getIsImageChange() == 1' --> AWS S3 업로드X & User imageUrl값 null로 업데이트

        User user = findLoginUser();

        // 새 프로필 사진을 AWS S3에 업로드 후, 이미지 url 반환.
        if(imageFile != null && userUpdateRequestDto.getIsImageChange() == 1) {  // 사진 변경O 경우
            String uploadImageUrl = awsS3Service.uploadImage(imageFile);
            user.updateImage(uploadImageUrl);  // 새로운 사진 url로 imageUrl 업데이트.
        }
        else if(imageFile == null && userUpdateRequestDto.getIsImageChange() == 1) {  // 기본사진으로 변경O 경우
            user.updateImage(null);  // 기본사진임을 명시하고자 null값으로 imageUrl 업데이트.
        }

        if(userUpdateRequestDto.getNickname() != null) user.updateName(userUpdateRequestDto.getNickname());  // 이름 수정없이 유지할경우, 업데이트 X
    }

    @Transactional(readOnly = true)
    @Override
    public UserSolvedResponseDto checkUserSolvedCount() {
        User user = findLoginUser();
        UserSolvedResponseDto userSolvedResponseDto = getSolvedCount(user.getBojId());

        return userSolvedResponseDto;
    }

    @Transactional(readOnly = true)
    @Override
    public UserCheckRoomResponseDto checkCurrentRoom() {
        User user = findLoginUser();

        Optional<UserRoom> optionalUserRoom = user.getUserRoomList().stream()
                .filter(userRoom -> userRoom.getIsLeave() == 0)  // getIsLeave() 값이 0인 경우만 필터링 (참여중인 방 찾기)
                .findFirst();
        UserRoom userRoom = optionalUserRoom.orElse(null);

        UserCheckRoomResponseDto userCheckRoomResponseDto;
        if(userRoom == null) {  // 참여중인 방이 없을때 X
            userCheckRoomResponseDto = UserCheckRoomResponseDto.builder()
                    .isExistRoom(false)
                    .roomId(null)
                    .build();
        }
        else {  // 참여중인 방이 있을때 O
            userCheckRoomResponseDto = UserCheckRoomResponseDto.builder()
                    .isExistRoom(true)
                    .roomId(userRoom.getRoom().getId())
                    .build();
        }

        return userCheckRoomResponseDto;
    }


    // ========== 유틸성 메소드 ========== //

    public static UserSolvedResponseDto getSolvedCount(String bojId){  // WebClient로 외부 solved API 호출 메소드
        try {
            WebClient webClient = WebClient.builder()
                    .baseUrl("https://solved.ac/api/v3")
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build();

            return webClient.get()
                    .uri("/user/show?handle=" + bojId)
                    .retrieve()
                    .bodyToMono(UserSolvedResponseDto.class)
                    .block();
        }catch (Exception e){
            throw new NoSuchBojIdException("bojId = " + bojId);  // solvedac 서버에 존재하지않는 백준id일경우 예외 처리.
        }
    }
}