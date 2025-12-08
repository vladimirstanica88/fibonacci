package com.smartbill.fibonacci.jwt;

import com.auth0.jwt.algorithms.Algorithm;
import com.smartbill.fibonacci.config.JwtProperties;
import com.smartbill.fibonacci.exception.InvalidJwtException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Bean
    public Algorithm jwtAlgorithm(JwtProperties jwtProperties) {
        if (jwtProperties.getSecret() == null) {
            throw new InvalidJwtException("JWT secret cannot be null");
        }
        return Algorithm.HMAC256(jwtProperties.getSecret());
    }
}
