package com.smartbill.fibonacci.jwt;


public interface JwtService {

    /**
     * Extracts the client ID from the provided Authorization header.
     *
     * @param authHeader the Authorization header containing the JWT, typically in the format "Bearer <token>"
     * @return the client ID extracted from the JWT
     * @throws InvalidJwtException if the header is null, malformed, or the token is invalid
     */
    String extractClientId(String authHeader);

    /**
     * Generates a JWT token for the given client ID.
     *
     * @param clientId the ID of the client for which the token is generated
     * @return a signed JWT token containing the client ID as a claim
     */
    String generateToken(String clientId);
}

