package com.github.peacetrue.sample.rsa;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;

public class JDKPublicKeyCryptography implements PublicKeyCryptography<PublicKey, PrivateKey> {

    @Override
    public KeyPair<PublicKey, PrivateKey> generateKeyPair(int bitLength) {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(bitLength, new SecureRandom());
            java.security.KeyPair keyPair = generator.generateKeyPair();
            return new KeyPairImpl<>(keyPair.getPublic(), keyPair.getPrivate());
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("this should not happen", e);
        }
    }

    @Override
    public byte[] encrypt(byte[] message, PublicKey publicKey) {
        try {
            Cipher encryptCipher = Cipher.getInstance("RSA");
            encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return encryptCipher.doFinal(message);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new IllegalStateException("to unchecked exception", e);
        }
    }

    @Override
    public byte[] decrypt(byte[] message, PrivateKey privateKey) {
        try {
            Cipher encryptCipher = Cipher.getInstance("RSA");
            encryptCipher.init(Cipher.DECRYPT_MODE, privateKey);
            return encryptCipher.doFinal(message);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new IllegalStateException("to unchecked exception", e);
        }
    }
}
