package com.github.peacetrue.sample.rsa;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Arrays;

/**
 * jdk rsa 使用示例
 *
 * @author : xiayx
 * @since : 2021-08-13 12:44
 **/
@Slf4j
class JDKRSATest {

    @Test
    void basic() throws Exception {
        log.info("生成密钥对");
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(1024, new SecureRandom());
        java.security.KeyPair keyPair = generator.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        log.info("加密信息");
        String message = RandomStringUtils.random(10);
        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
        byte[] messageEncryptedBytes = encrypt(publicKey, messageBytes);
        Assertions.assertFalse(Arrays.equals(messageEncryptedBytes, messageBytes), "加密后消息必须发生变化");

        log.info("解密信息");
        byte[] messageDecryptedBytes = decrypt(privateKey, messageEncryptedBytes);
        Assertions.assertArrayEquals(messageDecryptedBytes, messageBytes, "私钥可以解密");
//        byte[] decrypt = decrypt(publicKey, messageEncryptedBytes);
//        Assertions.assertFalse(Arrays.equals(decrypt, messageBytes), "公钥不能解密");
//        javax.crypto.BadPaddingException: Decryption error
    }

    private byte[] encrypt(Key key, byte[] messageBytes) throws Exception {
        Cipher encryptCipher = Cipher.getInstance("RSA");
        encryptCipher.init(Cipher.ENCRYPT_MODE, key);
        return encryptCipher.doFinal(messageBytes);
    }

    private byte[] decrypt(Key key, byte[] messageDecryptedBytes) throws Exception {
        Cipher decryptCipher = Cipher.getInstance("RSA");
        decryptCipher.init(Cipher.DECRYPT_MODE, key);
        return decryptCipher.doFinal(messageDecryptedBytes);
    }
}
