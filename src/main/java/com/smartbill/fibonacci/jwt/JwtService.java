package com.smartbill.fibonacci.jwt;

public interface JwtService {
    String extractClientId(String authHeader);

    String generateToken(String authHeader);
}
