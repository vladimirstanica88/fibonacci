package com.smartbill.fibonacci.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.smartbill.fibonacci.config.JwtProperties;
import org.springframework.stereotype.Component;

@Component
public class JwtServiceImpl implements JwtService {

    private final Algorithm algorithm;

    public JwtServiceImpl(JwtProperties jwtProperties) {
        this.algorithm = Algorithm.HMAC256(jwtProperties.getSecret());
    }

    @Override
    public String extractClientId(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }
        String token = authHeader.substring(7);
        DecodedJWT decoded = JWT.require(algorithm).build().verify(token);
        return decoded.getClaim("clientId").asString();
    }

    @Override
    public String generateToken(String clientId) {
        return JWT.create()
                .withClaim("clientId", clientId)
                .sign(algorithm);
    }

}
