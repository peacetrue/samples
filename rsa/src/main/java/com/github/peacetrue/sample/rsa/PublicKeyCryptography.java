package com.github.peacetrue.sample.rsa;

/**
 * 公开密钥加密（非对称加密）
 *
 * @author : xiayx
 * @since : 2021-08-08 15:33
 **/
public interface PublicKeyCryptography<P1, P2> {

    /**
     * 生成密钥对
     */
    KeyPair<P1, P2> generateKeyPair(int bitLength);

    /**
     * 使用公钥加密
     */
    byte[] encrypt(byte[] message, P1 publicKey);

    /**
     * 使用私钥解密
     */
    byte[] decrypt(byte[] message, P2 privateKey);

}
