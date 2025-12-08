package com.smartbill.fibonacci.storage;

import com.smartbill.fibonacci.ClientState;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ConcurrentClientStorage implements ClientStorage {

    private final Map<String, ClientState> states = new ConcurrentHashMap<>();

    @Override
    public ClientState get(String clientId) {
        return states.get(clientId);
    }

    @Override
    public ClientState getOrCreate(String clientId) {
        return states.computeIfAbsent(clientId, id -> new ClientState());
    }

    @Override
    public void save(String clientId, ClientState state) {
        states.put(clientId, state);
    }

    @Override
    public void clear() {
        states.clear();
    }
}
