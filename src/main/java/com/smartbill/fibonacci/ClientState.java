package com.smartbill.fibonacci;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ClientState {

    private int position = 0;
    private final List<Long> values = new ArrayList<>();


    public void addValue(long value) {
        values.add(value);
    }

    public void removeLastValue() {
        if (!values.isEmpty()) {
            values.remove(values.size() - 1);
        }
    }
}
