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

@Entity
@NoArgsConstructor
@Table(name ="user")
@Getter
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

    // 밑처럼 과도한 N+1 문제를 유발할 서비스 로직이 없을뿐더러 활용 빈도수도 낮기때문에, fetch join이나 @EntityGraph를 사용하지 않은채로 지연 로딩을 유지하도록 했음.
    // 과도한 N+1 예시: Users를 JPA의 findAll()로 호출하고, 각 User를 순회하며 user.getUserRoomList.get내부속성()으로 접근하는 경우.
    @OneToMany(mappedBy = "user")  // User-UserRoom 양방향매핑 (읽기 전용 필드)
    private List<UserRoom> userRoomList = new ArrayList<>();  // '사용자의 참여중인 방 찾는 용도 & 회원탈퇴 용도'에 활용.


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
