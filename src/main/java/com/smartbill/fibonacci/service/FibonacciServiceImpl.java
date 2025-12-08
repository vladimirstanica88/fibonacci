package com.smartbill.fibonacci.service;

import com.smartbill.fibonacci.ClientState;
import com.smartbill.fibonacci.storage.ClientStorage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FibonacciServiceImpl implements FibonacciService {

    private final ClientStorage storage;

    public FibonacciServiceImpl(ClientStorage storage) {
        this.storage = storage;
    }

    @Override
    public Long next(String clientId) {
        ClientState state = storage.getOrCreate(clientId);
        long nextValue;
        int pos = state.getPosition();

        if (pos == 0 || pos == 1) {
            nextValue = 1;
        } else {
            List<Long> v = state.getValues();
            nextValue = v.get(v.size() - 1) + v.get(v.size() - 2);
        }

        state.addValue(nextValue);
        state.setPosition(state.getPosition() + 1);
        storage.save(clientId, state);

        return nextValue;
    }

    @Override
    public void prev(String clientId) {
        ClientState state = storage.get(clientId);
        if (state == null || state.getPosition() <= 1) {
            throw new IllegalStateException("Cannot go back");
        }

        state.removeLastValue();
        state.setPosition(state.getPosition() - 1);
        storage.save(clientId, state);

    }

    @Override
    public List<Long> list(String clientId) {
        ClientState state = storage.get(clientId);
        if (state == null) {
            return List.of();
        }
        return state.getValues();
    }
}
