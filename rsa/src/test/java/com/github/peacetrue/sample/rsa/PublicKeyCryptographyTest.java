package com.github.peacetrue.sample.rsa;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author : xiayx
 * @since : 2021-08-13 12:43
 **/
class PublicKeyCryptographyTest {

    @Test
    void jdk() {
        test(new JDKPublicKeyCryptography());
    }

    @RepeatedTest(10)
    void custom() {
        test(new PeacePublicKeyCryptography(), 1000);
    }

    public <PublicKey, PrivateKey> void test(PublicKeyCryptography<PublicKey, PrivateKey> cryptography) {
        test(cryptography, 1024);
    }

    public <PublicKey, PrivateKey> void test(PublicKeyCryptography<PublicKey, PrivateKey> cryptography, int bitLength) {
        KeyPair<PublicKey, PrivateKey> keyPair = cryptography.generateKeyPair(bitLength);
        String message = RandomStringUtils.random(2);
        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
        byte[] encryptedMessageBytes = cryptography.encrypt(messageBytes, keyPair.getPublicKey());
        byte[] decryptMessageBytes = cryptography.decrypt(encryptedMessageBytes, keyPair.getPrivateKey());
        Assertions.assertArrayEquals(messageBytes, decryptMessageBytes, "原文应该等于加密后解密出来的数据");
    }

    @Test
    void name() {
        System.out.println(Math.pow(32, 2));
        char c = '我';
        System.out.println((int) c);
        System.out.println(BigInteger.valueOf(c));
        byte[] bytes = "我".getBytes(StandardCharsets.UTF_8);
        System.out.println(new BigInteger(bytes));
    }
}
