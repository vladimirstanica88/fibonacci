package com.smartbill.fibonacci.jwt;

import com.auth0.jwt.algorithms.Algorithm;
import com.smartbill.fibonacci.config.JwtProperties;
import com.smartbill.fibonacci.exception.InvalidJwtException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    private static final String JWT_SECRET_CANNOT_BE_NULL_MESSAGE = "JWT secret cannot be null";

    @Bean
    public Algorithm jwtAlgorithm(JwtProperties jwtProperties) {
        if (jwtProperties.getSecret() == null) {
            throw new InvalidJwtException(JWT_SECRET_CANNOT_BE_NULL_MESSAGE);
        }
        return Algorithm.HMAC256(jwtProperties.getSecret());
    }
}
