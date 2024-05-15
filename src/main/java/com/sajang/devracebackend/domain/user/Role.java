package com.sajang.devracebackend.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ROLE_GUEST, ROLE_USER, ROLE_ADMIN
}