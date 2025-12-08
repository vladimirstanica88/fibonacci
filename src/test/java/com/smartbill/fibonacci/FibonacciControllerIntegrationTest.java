package com.smartbill.fibonacci;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.smartbill.fibonacci.config.JwtProperties;
import com.smartbill.fibonacci.service.FibonacciService;
import com.smartbill.fibonacci.storage.ClientStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = FibonacciApplication.class)
@AutoConfigureMockMvc
class FibonacciControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private ClientStorage clientStorage;

    @Autowired
    private FibonacciService fibonacciService;

    private String token;

    @BeforeEach
    void setUp() throws Exception {
        clientStorage.clear();
        token = mockMvc.perform(get("/fibonacci/token")
                        .param("clientId", "test-client"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    void next_ShouldReturnNextFibonacciNumber() throws Exception {
        mockMvc.perform(post("/fibonacci/next")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        mockMvc.perform(post("/fibonacci/next")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        mockMvc.perform(post("/fibonacci/next")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string("2"));

        mockMvc.perform(post("/fibonacci/next")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));

        mockMvc.perform(post("/fibonacci/next")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    void prev_ShouldReturnToPreviousFibonacciNumber() throws Exception {
        mockMvc.perform(post("/fibonacci/next")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        mockMvc.perform(post("/fibonacci/next")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        mockMvc.perform(post("/fibonacci/next")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string("2"));

        mockMvc.perform(post("/fibonacci/next")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));

        mockMvc.perform(post("/fibonacci/prev")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        mockMvc.perform(get("/fibonacci")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().json("[1,1,2]"));
    }

    @Test
    void prev_ShouldReturnBadRequestWhenNoPrevious() throws Exception {
        mockMvc.perform(post("/fibonacci/prev")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }

    @Test
    void list_ShouldReturnNumbersSoFar() throws Exception {
        mockMvc.perform(post("/fibonacci/next")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        mockMvc.perform(post("/fibonacci/next")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        mockMvc.perform(post("/fibonacci/next")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        mockMvc.perform(get("/fibonacci")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().json("[1,1,2]"));
    }

    @Test
    void generateToken_ShouldReturnValidToken() throws Exception {
        String token = mockMvc.perform(get("/fibonacci/token")
                        .param("clientId", "another-client"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertNotNull(token);

        DecodedJWT decoded = JWT.decode(token);

        assertEquals("HS256", decoded.getAlgorithm());
        assertEquals("another-client", decoded.getClaim("clientId").asString());
        assertNotNull(decoded.getIssuedAt());
        assertNotNull(decoded.getId());
    }

}
