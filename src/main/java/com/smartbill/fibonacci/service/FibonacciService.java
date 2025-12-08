package com.smartbill.fibonacci.service;

import java.util.List;

public interface FibonacciService {
    Long next(String clientId);

    void prev(String clientId);

    List<Long> list(String clientId);
}
