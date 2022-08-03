package com.github.peacetrue.sample.rsa;

import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * RSA:
 * <p>
 * 加密：m^e % n = c
 * 解密：c^d % n = m
 * 转化：
 * (m^e % n)^d % n = m
 *
 * https://baike.baidu.com/item/%E5%8F%96%E6%A8%A1%E8%BF%90%E7%AE%97/10739384
 *
 * (m^e)^d % n = m
 * m^ed % n = m
 *
 * <p>
 * a≡b(mod m)
 * <p>
 * 基本公式：m^φ(n) ≡ 1(mod n)
 * 加k：
 * (m^φ(n))^k ≡ 1^k(mod n)
 * m^kφ(n) ≡ 1(mod n)
 * 加m：
 * m^φ(n)*m ≡ 1*m(mod n)
 * m^φ(n)+1 ≡ m(mod n)
 * m^φ(n)+1 % n = m
 * <p>
 * 推倒：
 * φ(n)+1=ed
 * kφ(n)+1=ed
 * k(φ(n)+1)=ed
 *
 * @author : xiayx
 * @since : 2021-08-08 15:37
 **/
@Slf4j
public class PeacePublicKeyCryptography implements PublicKeyCryptography<PeacePublicKey, PeacePrivateKey> {

    @Override
    public KeyPair<PeacePublicKey, PeacePrivateKey> generateKeyPair(int bitLength) {
        //随机选取一个质数
        BigInteger p = BigInteger.probablePrime(32, new SecureRandom());
        //随机选取另一个质数
        BigInteger q = BigInteger.probablePrime(32, new SecureRandom());
        //防止 p、q 重复
        while (p.compareTo(q) == 0) q = BigInteger.probablePrime(32, new SecureRandom());
        //欧拉函数的 n，n=pq=32*32=64位
        BigInteger n = p.multiply(q);
        //欧拉函数的值
        BigInteger φn = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        log.info("q: {}, p: {}, n = pq = {}, φn = (q-1)(p-1) = {}", p, q, n, φn);
        //公钥
        BigInteger e = BigInteger.probablePrime(bitLength, new SecureRandom());
        if (e.compareTo(φn) < 1) throw new IllegalStateException(String.format("e(%s) must > φn(%s)", e, φn));
        //私钥，需保证 e 与 φn 互质，(ed-1)%φn 有整数解，d 为模反元素
        //其中一种场景是：e 是质数且 e > φn 则 e 与 φn 互质
        //φn>e 的场景也有解，但不好求
        BigInteger d = e.modInverse(φn);
        //ed 防重复
        while (e.compareTo(d) == 0) {
            e = BigInteger.probablePrime(bitLength, new SecureRandom());
            d = e.modInverse(φn);
        }
        log.info("\ne: {}, \nd: {}, \ne*d+φnx=1 -> x=(e*d-1)/φn={}", e, d, e.multiply(d).subtract(BigInteger.ONE).divide(φn));
        return new KeyPairImpl<>(
                new PeacePublicKey(e, n),
                new PeacePrivateKey(d, n)
        );
    }

    @Override
    public byte[] encrypt(byte[] message, PeacePublicKey publicKey) {
        //message 中可能有负数，需要转码成正数，m 必须是正数
        message = Base64.getEncoder().encode(message);
        return _encrypt(message, publicKey);
    }

    private byte[] _encrypt(byte[] message, PeacePublicKey publicKey) {
        // BigInteger 有范围限制，2^Integer.MAX_VALUE (exclusive) to +2^Integer.MAX_VALUE
        // message 过长会导致溢出，此处需要切割数组
        BigInteger m = new BigInteger(message);
        log.info("{} ^ {} % {}", m, publicKey.getE(), publicKey.getN());
        return m
                .modPow(publicKey.getE(), publicKey.getN())
                .toByteArray();
    }

    @Override
    public byte[] decrypt(byte[] message, PeacePrivateKey privateKey) {
        message = _encrypt(message, new PeacePublicKey(privateKey.getD(), privateKey.getN()));
        return Base64.getDecoder().decode(message);
    }
}
