package com.github.peacetrue.sample.rsa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class KeyPairImpl<PublicKey, PrivateKey> implements KeyPair<PublicKey, PrivateKey> {

    private PublicKey publicKey;
    private PrivateKey privateKey;

}
