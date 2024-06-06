package com.sajang.devracebackend.domain;

import com.sajang.devracebackend.domain.common.BaseEntity;
import com.sajang.devracebackend.domain.enums.Role;
import com.sajang.devracebackend.domain.enums.SocialType;
import com.sajang.devracebackend.domain.mapping.UserRoom;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Getter
@NoArgsConstructor

@Table(name ="user")
@Entity
public class User extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "email", unique = true)
    private String email;

    private String nickname;

    @Column(name = "boj_id", unique = true)
    private String bojId;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "social_id")
    private String socialId;  // 소셜 식별값

    @Enumerated(EnumType.STRING)
    private SocialType socialType;  // 소셜 종류

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "refresh_token")
    private String refreshToken;

    // User 조회시 User.userRoomList N+1 문제 해결 ==> Eager 조회 : '사용자의 참여중인 방 찾는 용도', Lazy 조회 : '기본 지연로딩 용도 & 회원탈퇴 용도'
    @OneToMany(mappedBy = "user")  // User-UserRoom 양방향매핑 (읽기 전용 필드)
    private List<UserRoom> userRoomList = new ArrayList<>();


    @Builder(builderClassName = "UserSaveBuilder", builderMethodName = "UserSaveBuilder")
    public User(String email, Role role, SocialType socialType, String socialId, String nickname, String imageUrl) {
        // 이 빌더는 사용자 회원가입때만 사용할 용도 (refreshToken과 이 외의 속성값들은 전부 null로 들어감.)
        this.email = email;
        this.role = role;

        this.socialType = socialType;

        this.socialId = socialId;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
    }


    public void updateBojId(String bojId) {
        this.bojId = bojId;
    }

    public void updateName(String nickname) {
        this.nickname = nickname;
    }

    public void updateImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void updateRole() {  // 추가정보 입력후, Role을 GUEST->USER로 업데이트. (헤더의 jwt 토큰에 등록해둔 권한도 수정해야하기에, Access 토큰도 따로 재발급해야함.)
        this.role = Role.ROLE_USER;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void deleteAccount() {  // 회원 계정탈퇴 (imageUrl 초기화는 따로 구성) - soft delete
        this.email = null;
        this.nickname = "(탈퇴한 사용자)";
        this.bojId = null;
        this.socialId = null;
        this.refreshToken = null;
    }
}
