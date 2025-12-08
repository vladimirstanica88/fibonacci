package jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.smartbill.fibonacci.exception.InvalidJwtException;
import com.smartbill.fibonacci.jwt.JwtServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceImplTest {

    private Algorithm algorithm;
    private JwtServiceImpl jwtService;

    @BeforeEach
    void setUp() {
        algorithm = Algorithm.HMAC256("test-secret");
        jwtService = new JwtServiceImpl(algorithm);
    }

    @Test
    void generateToken_ShouldReturnNonNullAndNonEmptyToken() {
        String token = jwtService.generateToken("client1");
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void generateToken_ShouldCreateDifferentTokensEachCall() {
        String token1 = jwtService.generateToken("client1");
        String token2 = jwtService.generateToken("client1");
        assertNotEquals(token1, token2, "Each generated token should be unique");
    }

    @Test
    void generateToken_ShouldContainClientIdClaim() {
        String clientId = "client1";
        String token = jwtService.generateToken(clientId);
        String extracted = jwtService.extractClientId("Bearer " + token);
        assertEquals(clientId, extracted);
    }

    @Test
    void extractClientId_ShouldReturnClientId_WhenTokenIsValid() {
        String clientId = "client1";
        String token = jwtService.generateToken(clientId);
        String authHeader = "Bearer " + token;

        String result = jwtService.extractClientId(authHeader);
        assertEquals(clientId, result);
    }

    @Test
    void extractClientId_ShouldThrowInvalidJwtException_WhenHeaderIsNull() {
        InvalidJwtException ex = assertThrows(InvalidJwtException.class,
                () -> jwtService.extractClientId(null));
        assertEquals("Missing or invalid Authorization header", ex.getMessage());
    }

    @Test
    void extractClientId_ShouldThrowInvalidJwtException_WhenHeaderDoesNotStartWithBearer() {
        InvalidJwtException ex = assertThrows(InvalidJwtException.class,
                () -> jwtService.extractClientId("InvalidHeader"));
        assertEquals("Missing or invalid Authorization header", ex.getMessage());
    }

    @Test
    void extractClientId_ShouldThrowInvalidJwtException_WhenClientIdClaimIsMissing() {
        String tokenWithoutClaim = JWT.create()
                .withJWTId(java.util.UUID.randomUUID().toString())
                .sign(algorithm);

        String authHeader = "Bearer " + tokenWithoutClaim;

        InvalidJwtException ex = assertThrows(InvalidJwtException.class,
                () -> jwtService.extractClientId(authHeader));
        assertEquals("Missing or invalid clientId", ex.getMessage());
    }

    @Test
    void extractClientId_ShouldThrowInvalidJwtException_WhenTokenIsTampered() {
        String token = jwtService.generateToken("client1");
        String tampered = token.substring(0, token.length() - 1) + "x";
        String authHeader = "Bearer " + tampered;

        assertThrows(InvalidJwtException.class, () -> jwtService.extractClientId(authHeader));
    }

    @Test
    void extractClientId_ShouldThrowInvalidJwtException_WhenSignedWithWrongSecret() {
        Algorithm wrongAlg = Algorithm.HMAC256("wrong-secret");
        String token = JWT.create().withClaim("clientId", "client1").sign(wrongAlg);
        String authHeader = "Bearer " + token;

        assertThrows(InvalidJwtException.class, () -> jwtService.extractClientId(authHeader));
    }

}
