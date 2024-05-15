package com.sajang.devracebackend.domain.user;


import com.sajang.devracebackend.domain.common.BaseEntity;
import com.sajang.devracebackend.domain.mapping.UserRoom;
import jakarta.persistence.*;
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
    @Column(name = "nickname")
    private String nickname;
    @Column(name = "boj_id", unique = true)
    private String bojId;
    @Column(name = "image_url")
    private String imageUrl;
    @Column(name = "social_id")
    private String socialId;
    @Column(name = "refresh_token")
    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Enumerated(EnumType.STRING)
    private Role role;


    @OneToMany(mappedBy = "user")  // User-UserRoom 양방향매핑
    private List<UserRoom> userRoomList = new ArrayList<>();



    public void updateRole() {  // 추가정보 입력후, Role을 GUEST->USER로 업데이트. (헤더의 jwt 토큰에 등록해둔 권한도 수정해야하기에, Access 토큰도 따로 재발급해야함.)
        this.role = Role.ROLE_USER;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
