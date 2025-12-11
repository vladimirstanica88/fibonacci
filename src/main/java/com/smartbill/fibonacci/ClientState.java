package com.smartbill.fibonacci;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
public class ClientState {

    private int position = 0;  // indexul curent în secvență
    private BigInteger prev = BigInteger.valueOf(0);     // F(n-2)
    private BigInteger curr = BigInteger.valueOf(1);     // F(n-1)

}
