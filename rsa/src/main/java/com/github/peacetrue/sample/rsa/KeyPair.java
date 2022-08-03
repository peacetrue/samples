package com.github.peacetrue.sample.rsa;

public interface KeyPair<PublicKey, PrivateKey> {

    PublicKey getPublicKey();

    PrivateKey getPrivateKey();
}
