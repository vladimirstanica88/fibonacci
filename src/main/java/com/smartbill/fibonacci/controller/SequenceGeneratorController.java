package com.smartbill.fibonacci.controller;

import com.smartbill.fibonacci.jwt.JwtService;
import com.smartbill.fibonacci.service.SequenceGeneratorService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/fibonacci")
@AllArgsConstructor
public class SequenceGeneratorController {

    private final SequenceGeneratorService sequenceGeneratorService;
    private final JwtService jwtService;


    @PostMapping("/next")
    public ResponseEntity<?> next(@RequestHeader("Authorization") String authHeader) {
        String clientId = jwtService.extractClientId(authHeader);
        BigInteger next = sequenceGeneratorService.next(clientId);
        return ResponseEntity.ok(next);
    }

    @DeleteMapping("/prev")
    public ResponseEntity<?> prev(@RequestHeader("Authorization") String authHeader) {
        String clientId = jwtService.extractClientId(authHeader);
        sequenceGeneratorService.prev(clientId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<?> list(@RequestHeader("Authorization") String authHeader) {
        String clientId = jwtService.extractClientId(authHeader);
        List<BigInteger> numbers = sequenceGeneratorService.list(clientId);
        return ResponseEntity.ok(numbers);
    }

    @GetMapping("/token")
    public ResponseEntity<?> generateToken(@RequestParam("clientId") String clientId) {
        String token = jwtService.generateToken(clientId);
        return ResponseEntity.ok().body(token);
    }
}
