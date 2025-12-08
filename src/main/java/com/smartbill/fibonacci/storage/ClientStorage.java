package com.smartbill.fibonacci.storage;


import com.smartbill.fibonacci.ClientState;

public interface ClientStorage {
    ClientState get(String clientId);

    ClientState getOrCreate(String clientId);

    void save(String clientId, ClientState state);

    void clear();
}
