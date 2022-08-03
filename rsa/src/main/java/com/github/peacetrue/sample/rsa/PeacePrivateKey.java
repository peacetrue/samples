package com.github.peacetrue.sample.rsa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigInteger;

/**
 * @author : xiayx
 * @since : 2021-08-14 16:37
 **/
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PeacePrivateKey {
    private BigInteger d;
    private BigInteger n;
}
