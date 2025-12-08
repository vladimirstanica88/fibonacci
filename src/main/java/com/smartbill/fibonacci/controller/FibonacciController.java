package com.smartbill.fibonacci.controller;

import com.smartbill.fibonacci.jwt.JwtService;
import com.smartbill.fibonacci.service.FibonacciService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fib")
@AllArgsConstructor
public class FibonacciController {

    private final FibonacciService fibonacciService;
    private final JwtService jwtService;


    @PostMapping("/next")
    public ResponseEntity<?> next(@RequestHeader("Authorization") String authHeader) {
        String clientId = jwtService.extractClientId(authHeader);
        return ResponseEntity.ok(fibonacciService.next(clientId));
    }

    @PostMapping("/prev")
    public ResponseEntity<?> prev(@RequestHeader("Authorization") String authHeader) {
        String clientId = jwtService.extractClientId(authHeader);
        return ResponseEntity.ok(fibonacciService.prev(clientId));
    }

    @GetMapping
    public ResponseEntity<?> list(@RequestHeader("Authorization") String authHeader) {
        String clientId = jwtService.extractClientId(authHeader);
        return ResponseEntity.ok(fibonacciService.list(clientId));
    }

    @GetMapping("/token")
    public ResponseEntity<?> generateToken(@RequestParam("clientId") String clientId) {
        String token = jwtService.generateToken(clientId);
        return ResponseEntity.ok().body(token);
    }
}
