package com.smartbill.fibonacci.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public interface SequenceGeneratorService {

    /**
     * Computes and returns the next Fibonacci number for the specified client.
     * The sequence is stored per client.
     *
     * @param clientId the ID of the client
     * @return the next Fibonacci number for the client
     */
    BigInteger next(String clientId);

    /**
     * Moves the Fibonacci sequence one step back for the specified client.
     * Removes the last number generated.
     *
     * @param clientId the ID of the client
     * @throws IllegalStateException if there is no previous number to revert to
     */
    void prev(String clientId);

    /**
     * Returns the list of all Fibonacci numbers generated so far for the specified client.
     *
     * @param clientId the ID of the client
     * @return a list of Fibonacci numbers for the client; empty list if no numbers exist
     */
    List<BigInteger> list(String clientId);
}

