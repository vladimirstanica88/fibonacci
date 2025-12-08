package com.smartbill.fibonacci.service;

import com.smartbill.fibonacci.ClientState;
import com.smartbill.fibonacci.storage.ClientStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FibonacciServiceImpl implements FibonacciService {

    private static final String CANNOT_GO_BACK_EXCEPTION_MESSAGE = "Cannot go back";
    private final ClientStorage storage;

    @Override
    public Long next(String clientId) {
        log.info("Calculating next Fibonacci number for clientId={}", clientId);
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

        log.debug("Next value for clientId={} is {}, new position={}", clientId, nextValue, state.getPosition());
        return nextValue;
    }

    @Override
    public void prev(String clientId) {
        log.info("Reverting to previous Fibonacci number for clientId={}", clientId);
        ClientState state = storage.get(clientId);
        if (state == null || state.getPosition() <= 1) {
            log.warn("Cannot go back for clientId={}", clientId);
            throw new IllegalStateException(CANNOT_GO_BACK_EXCEPTION_MESSAGE);
        }

        state.removeLastValue();
        state.setPosition(state.getPosition() - 1);
        storage.save(clientId, state);

        log.debug("Reverted Fibonacci sequence for clientId={}, new position={}", clientId, state.getPosition());
    }

    @Override
    public List<Long> list(String clientId) {
        log.info("Listing Fibonacci numbers for clientId={}", clientId);
        ClientState state = storage.get(clientId);
        if (state == null) {
            log.debug("No state found for clientId={}", clientId);
            return List.of();
        }
        log.debug("Fibonacci sequence for clientId={}: {}", clientId, state.getValues());
        return state.getValues();
    }
}

