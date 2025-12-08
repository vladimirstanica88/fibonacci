package com.smartbill.fibonacci.storage;

import com.smartbill.fibonacci.ClientState;

public interface ClientStorage {

    /**
     * Retrieves the current state of the specified client.
     *
     * @param clientId the ID of the client
     * @return the {@link ClientState} of the client, or {@code null} if no state exists
     */
    ClientState get(String clientId);

    /**
     * Retrieves the current state of the specified client,
     * or creates a new state if it does not exist.
     *
     * @param clientId the ID of the client
     * @return the existing or newly created {@link ClientState} of the client
     */
    ClientState getOrCreate(String clientId);

    /**
     * Saves the state of the specified client.
     *
     * @param clientId the ID of the client
     * @param state    the {@link ClientState} to save
     */
    void save(String clientId, ClientState state);

    /**
     * Clears all client states from the storage.
     */
    void clear();
}

