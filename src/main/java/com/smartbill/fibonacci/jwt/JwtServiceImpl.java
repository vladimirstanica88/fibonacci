package com.smartbill.fibonacci.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.smartbill.fibonacci.config.JwtProperties;
import com.smartbill.fibonacci.exception.InvalidJwtException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@AllArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final Algorithm algorithm;

    @Override
    public String extractClientId(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new InvalidJwtException("Missing or invalid Authorization header");
        }
        String token = authHeader.substring(7);
        DecodedJWT decoded = JWT.require(algorithm).build().verify(token);
        String clientId = decoded.getClaim("clientId").asString();
        if (clientId == null) {
            throw new InvalidJwtException("Missing or invalid clientId");
        }
        return clientId;
    }

    @Override
    public String generateToken(String clientId) {
        return JWT.create()
                .withClaim("clientId", clientId)
                .withIssuedAt(new Date())
                .withJWTId(java.util.UUID.randomUUID().toString())
                .sign(algorithm);
    }

}
