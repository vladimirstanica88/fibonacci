package com.smartbill.fibonacci.controller;

import com.smartbill.fibonacci.jwt.JwtService;
import com.smartbill.fibonacci.service.FibonacciService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fibonacci")
@AllArgsConstructor
public class FibonacciController {

    private final FibonacciService fibonacciService;
    private final JwtService jwtService;


    @PostMapping("/next")
    public ResponseEntity<?> next(@RequestHeader("Authorization") String authHeader) {
        String clientId = jwtService.extractClientId(authHeader);
        Long next = fibonacciService.next(clientId);
        return ResponseEntity.ok(next);
    }

    @PostMapping("/prev")
    public ResponseEntity<?> prev(@RequestHeader("Authorization") String authHeader) {
        String clientId = jwtService.extractClientId(authHeader);
        fibonacciService.prev(clientId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<?> list(@RequestHeader("Authorization") String authHeader) {
        String clientId = jwtService.extractClientId(authHeader);
        List<Long> numbers = fibonacciService.list(clientId);
        return ResponseEntity.ok(numbers);
    }

    @GetMapping("/token")
    public ResponseEntity<?> generateToken(@RequestParam("clientId") String clientId) {
        String token = jwtService.generateToken(clientId);
        return ResponseEntity.ok().body(token);
    }
}
