package com.smartbill.fibonacci.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.smartbill.fibonacci.exception.InvalidJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtServiceImpl implements JwtService {

    private static final String BEARER = "Bearer ";
    private static final String MISSING_OR_INVALID_AUTHORIZATION_HEADER_MESSAGE = "Missing or invalid Authorization header";
    private static final String CLIENT_ID = "clientId";
    private static final String MISSING_OR_INVALID_CLIENT_ID_MESSAGE = "Missing or invalid clientId";
    private static final int BEGIN_INDEX = 7;

    private final Algorithm algorithm;

    @Override
    public String extractClientId(String authHeader) {
        log.info("Extracting clientId from Authorization header");

        if (authHeader == null || !authHeader.startsWith(BEARER)) {
            log.warn("Authorization header missing or invalid: {}", authHeader);
            throw new InvalidJwtException(MISSING_OR_INVALID_AUTHORIZATION_HEADER_MESSAGE);
        }

        String token = authHeader.substring(BEGIN_INDEX);
        DecodedJWT decoded;
        try {
            decoded = JWT.require(algorithm).build().verify(token);
        } catch (Exception e) {
            log.warn("JWT verification failed: {}", e.getMessage());
            throw new InvalidJwtException("Invalid token");
        }

        String clientId = decoded.getClaim(CLIENT_ID).asString();
        if (clientId == null) {
            log.warn("Token missing clientId claim");
            throw new InvalidJwtException(MISSING_OR_INVALID_CLIENT_ID_MESSAGE);
        }

        log.debug("Extracted clientId={} from token", clientId);
        return clientId;
    }

    @Override
    public String generateToken(String clientId) {
        log.info("Generating JWT token for clientId={}", clientId);

        String token = JWT.create()
                .withClaim(CLIENT_ID, clientId)
                .withIssuedAt(new Date())
                .withJWTId(java.util.UUID.randomUUID().toString())
                .sign(algorithm);

        log.debug("Generated token={}", token);
        return token;
    }
}

