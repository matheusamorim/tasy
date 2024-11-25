package com.youx.tasy.controller.login;


public record LoginResponse(
        String accessToken,
        Long expiresIn) {
}
