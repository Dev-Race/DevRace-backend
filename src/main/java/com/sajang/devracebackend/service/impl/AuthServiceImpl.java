package com.sajang.devracebackend.service.impl;

import com.sajang.devracebackend.domain.User;
import com.sajang.devracebackend.domain.enums.Role;
import com.sajang.devracebackend.dto.auth.SignupRequestDto;
import com.sajang.devracebackend.dto.auth.SignupResponseDto;
import com.sajang.devracebackend.dto.auth.TokenDto;
import com.sajang.devracebackend.dto.user.UserResponseDto;
import com.sajang.devracebackend.repository.UserRepository;
import com.sajang.devracebackend.response.exception.exception400.BojIdDuplicateException;
import com.sajang.devracebackend.response.exception.exception400.UserBadRequestException;
import com.sajang.devracebackend.security.jwt.TokenProvider;
import com.sajang.devracebackend.service.AuthService;
import com.sajang.devracebackend.service.AwsS3Service;
import com.sajang.devracebackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AwsS3Service awsS3Service;
    private final UserService userService;
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;


    @Transactional
    @Override
    public SignupResponseDto signup(MultipartFile imageFile, SignupRequestDto signupRequestDto) throws IOException {

        // < 회원 프로필 사진 변경조건 >
        // - 사진 변경X : if 'imageFile == null && signupRequestDto.getIsImageChange() == 0' --> AWS S3 업로드X
        // - 사진 변경O : if 'imageFile != null && signupRequestDto.getIsImageChange() == 1' --> AWS S3 업로드O
        // - 기본사진으로 변경O : if 'imageFile == null && signupRequestDto.getIsImageChange() == 1' --> AWS S3 업로드X & User imageUrl값 null로 업데이트

        if(signupRequestDto.getBojId() == null) {
            throw new UserBadRequestException("회원가입 백준id null 에러");
        }
        if(userRepository.existsByBojId(signupRequestDto.getBojId()) == true) {  // 이미 해당 백준id로 가입한 사용자가 존재하는경우, 예외 처리.
            throw new BojIdDuplicateException(signupRequestDto.getBojId());
        }
        UserServiceImpl.getSolvedCount(signupRequestDto.getBojId());  // solvedac 서버에 존재하지않는 백준id일경우 예외 처리.

        User user = userService.findLoginUser();

        // signup은 Role이 GUEST인 사용자만 이용가능한 API임.
        if(!user.getRole().equals(Role.ROLE_GUEST) || user.getBojId() != null) {
            // 이 로직을 SecurityConfig의 hasAuthority("ROLE_GUEST") 외에도 여기 또 써줘야하는 이유는,
            // reissue로 인한 재발급 이후에도 이전 엑세스 토큰으로 '/signup' 경로에 다시 접근할 경우, 토큰 내의 권한은 GUEST가 맞겠지만 DB 내의 권한은 USER이기에 이러한 비정상적인 접근을 방지할 수 있기 때문임.
            throw new UserBadRequestException("이미 가입완료 되어있는 사용자입니다.");
        }

        // 새 프로필 사진을 AWS S3에 업로드 후, 이미지 url 반환.
        if(imageFile != null && signupRequestDto.getIsImageChange() == 1) {  // 사진 변경O 경우
            String uploadImageUrl = awsS3Service.uploadImage(imageFile);
            user.updateImage(uploadImageUrl);  // 새로운 사진 url로 imageUrl 업데이트.
        }
        else if(imageFile == null && signupRequestDto.getIsImageChange() == 1) {  // 기본사진으로 변경O 경우
            user.updateImage(null);  // 기본사진임을 명시하고자 null값으로 imageUrl 업데이트.
        }

        if(signupRequestDto.getNickname() != null) user.updateName(signupRequestDto.getNickname());  // 이름 수정없이 유지할경우, 업데이트 X
        user.updateBojId(signupRequestDto.getBojId());
        user.updateRole();
        UserResponseDto userResponseDto = new UserResponseDto(user);

        // 추가정보 입력후, 위에서 Role을 GUEST->USER로 업데이트했지만, 헤더의 jwt 토큰에 등록해둔 권한도 수정해야하기에, Access 토큰도 따로 재발급해야함.
        TokenDto tokenDto = tokenProvider.generateAccessTokenByRefreshToken(user.getId(), Role.ROLE_USER, user.getRefreshToken());

        SignupResponseDto signupResponseDto = SignupResponseDto.builder()
                .userResponseDto(userResponseDto)
                .tokenDto(tokenDto)
                .build();

        return signupResponseDto;
    }
}
