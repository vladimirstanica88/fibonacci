package com.smartbill.fibonacci.service;

import com.smartbill.fibonacci.ClientState;
import com.smartbill.fibonacci.storage.ClientStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SequenceGeneratorServiceImpl implements SequenceGeneratorService {

    private static final String CANNOT_GO_BACK_EXCEPTION_MESSAGE = "Cannot go back";
    private final ClientStorage storage;

    @Override
    public BigInteger next(String clientId) {
        log.info("Calculating next Fibonacci number for clientId={}", clientId);
        ClientState state = storage.getOrCreate(clientId);
        BigInteger nextValue;

        if (state.getPosition() == 0) {
            nextValue = BigInteger.ONE;
            state.setPrev(BigInteger.ZERO);
            state.setCurr(BigInteger.ONE);
        } else {
            nextValue = state.getPrev().add(state.getCurr());
            state.setPrev(state.getCurr());
            state.setCurr(nextValue);
        }

        state.setPosition(state.getPosition() + 1);
        storage.save(clientId, state);

        log.debug("Next value for clientId={} is {}, new position={}, prev={}, curr={}",
                clientId, nextValue, state.getPosition(), state.getPrev(), state.getCurr());
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

        BigInteger newCurr = state.getPrev();
        BigInteger newPrev = state.getCurr().subtract(state.getPrev());

        state.setCurr(newCurr);
        state.setPrev(newPrev);
        state.setPosition(state.getPosition() - 1);

        storage.save(clientId, state);

        log.debug("Reverted Fibonacci sequence for clientId={}, new position={}, prev={}, curr={}",
                clientId, state.getPosition(), state.getPrev(), state.getCurr());
    }

    @Override
    public List<BigInteger> list(String clientId) {
        log.info("Listing Fibonacci sequence for clientId={}", clientId);
        ClientState state = storage.get(clientId);

        if (state == null || state.getPosition() == 0) {
            log.debug("No state found for clientId={}", clientId);
            return List.of();
        }

        List<BigInteger> sequence = new ArrayList<>();
        BigInteger a = BigInteger.ZERO;
        BigInteger b = BigInteger.ONE;

        for (int i = 1; i <= state.getPosition(); i++) {
            BigInteger next = (i == 1) ? BigInteger.ONE : a.add(b);
            sequence.add(next);
            a = b;
            b = next;
        }

        log.debug("Fibonacci sequence for clientId={} (position={}): {}",
                clientId, state.getPosition(), sequence);
        return sequence;
    }
}
