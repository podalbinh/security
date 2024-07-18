package com.example.transactionmanagement.utils;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.stereotype.Component;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Utility class for AES encryption and decryption.
 */
@Component
public class AESUtil {
    private static final String ALGORITHM = "AES";
    private static final int KEY_SIZE = 128;
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";
    private static final SecretKeySpec secretKey;

    static {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
            keyGenerator.init(KEY_SIZE);
            SecretKey secret = keyGenerator.generateKey();
            secretKey = new SecretKeySpec(secret.getEncoded(), ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error initializing AES key", e);
        }
    }

    /**
     * Encrypts a string using AES encryption.
     *
     * @param data The data to encrypt.
     * @return Encrypted data as Base64-encoded string.
     * @throws NoSuchPaddingException    If no padding is available.
     * @throws NoSuchAlgorithmException  If no such algorithm exists.
     * @throws InvalidKeyException       If the key is invalid.
     * @throws BadPaddingException       If the padding is invalid.
     * @throws IllegalBlockSizeException If the block size is invalid.
     */
    public static String encrypt(String data)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {
        if (data == null) {
            throw new IllegalArgumentException("Data must not be null");
        }
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * Decrypts a Base64-encoded string using AES decryption.
     *
     * @param encryptedData The Base64-encoded encrypted data.
     * @return Decrypted data as a string.
     * @throws NoSuchPaddingException    If no padding is available.
     * @throws NoSuchAlgorithmException  If no such algorithm exists.
     * @throws InvalidKeyException       If the key is invalid.
     * @throws BadPaddingException       If the padding is invalid.
     * @throws IllegalBlockSizeException If the block size is invalid.
     */
    public static String decrypt(String encryptedData)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {
        if (encryptedData == null) {
            throw new IllegalArgumentException("Encrypted Data must not be null");
        }
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes);
    }
}
