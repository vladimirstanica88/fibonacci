package com.smartbill.fibonacci.service;

import java.util.List;
import java.util.Map;

public interface FibonacciService {
    Map<String, Object> next(String clientId);

    Map<String, String> prev(String clientId);

    Map<String, List<Long>> list(String clientId);
}
