package com.example.transactionmanagement.utils;

import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

/**
 * Công cụ hỗ trợ mã hóa và giải mã RSA.
 */
@Component
public class RSAUtil {
    private static final String ALGORITHM = "RSA";
    private static final int KEY_SIZE = 2048;
    private   PrivateKey privateKey ;
    private  PublicKey publicKey ;

    @PostConstruct
    public void init() throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPair  keyPair = generateKeyPair();
        privateKey = keyPair.getPrivate();
        publicKey = keyPair.getPublic();
    }

    private KeyPair generateKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPairGenerator keyPairGenerator  = KeyPairGenerator.getInstance(ALGORITHM);
        keyPairGenerator.initialize(KEY_SIZE);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }



    public  String encrypt(String data)
            throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        if (data == null) {
            throw new IllegalArgumentException("Data must not be null");
        }
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE,publicKey);
        byte[] encryptedValue  = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedValue);
    }

    public String decrypt(String encryptedData)
            throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        if (encryptedData == null) {
            throw new IllegalArgumentException("Encrypted data must not be null");
        }
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE,privateKey);
        byte[] decryptedValue  = cipher.doFinal(Base64.getMimeDecoder().decode(encryptedData));
        return new String(decryptedValue);
    }
}
