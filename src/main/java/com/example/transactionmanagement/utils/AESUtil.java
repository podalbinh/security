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
    private static final String SECRET_KEY="2B7E151628AED2A6ABF7158809CF4F3C";
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
            throw new IllegalArgumentException(MessagesConstants.DATA_NOT_NULL);
        }
        SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedData = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedData);
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
            throw new IllegalArgumentException(MessagesConstants.ENCRYPT_DATA_NOT_NULL);
        }
        SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptedData = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
            return new String(decryptedData);
    }
}
