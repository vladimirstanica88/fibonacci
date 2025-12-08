package com.smartbill.fibonacci.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.smartbill.fibonacci.exception.InvalidJwtException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@AllArgsConstructor
public class JwtServiceImpl implements JwtService {

    private static final String BEARER = "Bearer ";
    private static final String MISSING_OR_INVALID_AUTHORIZATION_HEADER_MESSAGE = "Missing or invalid Authorization header";
    private static final String CLIENT_ID = "clientId";
    private static final String MISSING_OR_INVALID_CLIENT_ID_MESSAGE = "Missing or invalid clientId";
    private static final int BEGIN_INDEX = 7;

    private final Algorithm algorithm;

    @Override
    public String extractClientId(String authHeader) {
        if (authHeader == null || !authHeader.startsWith(BEARER)) {
            throw new InvalidJwtException(MISSING_OR_INVALID_AUTHORIZATION_HEADER_MESSAGE);
        }
        String token = authHeader.substring(BEGIN_INDEX);
        DecodedJWT decoded = JWT.require(algorithm).build().verify(token);
        String clientId = decoded.getClaim(CLIENT_ID).asString();
        if (clientId == null) {
            throw new InvalidJwtException(MISSING_OR_INVALID_CLIENT_ID_MESSAGE);
        }
        return clientId;
    }

    @Override
    public String generateToken(String clientId) {
        return JWT.create()
                .withClaim(CLIENT_ID, clientId)
                .withIssuedAt(new Date())
                .withJWTId(java.util.UUID.randomUUID().toString())
                .sign(algorithm);
    }

}
