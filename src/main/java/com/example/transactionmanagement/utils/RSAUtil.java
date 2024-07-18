package com.example.transactionmanagement.utils;

import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Utility class for RSA encryption and decryption operations.
 */
@Component
public class RSAUtil {
    private static final String ALGORITHM = "RSA";
    // private static final int KEY_SIZE = 2048;
    private static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCgFGVfrY4jQSoZQWWygZ83roKXWD4YeT2x2p41dGkPixe73rT2IW04glagN2vgoZoHuOPqa5and6kAmK2ujmCHu6D1auJhE2tXP+yLkpSiYMQucDKmCsWMnW9XlC5K7OSL77TXXcfvTvyZcjObEz6LIBRzs6+FqpFbUO9SJEfh6wIDAQAB";
    private static final String PRIVATE_KEY = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKAUZV+tjiNBKhlBZbKBnzeugpdYPhh5PbHanjV0aQ+LF7vetPYhbTiCVqA3a+Chmge44+prlqd3qQCYra6OYIe7oPVq4mETa1c/7IuSlKJgxC5wMqYKxYydb1eULkrs5IvvtNddx+9O/JlyM5sTPosgFHOzr4WqkVtQ71IkR+HrAgMBAAECgYAkQLo8kteP0GAyXAcmCAkA2Tql/8wASuTX9ITD4lsws/VqDKO64hMUKyBnJGX/91kkypCDNF5oCsdxZSJgV8owViYWZPnbvEcNqLtqgs7nj1UHuX9S5yYIPGN/mHL6OJJ7sosOd6rqdpg6JRRkAKUV+tmN/7Gh0+GFXM+ug6mgwQJBAO9/+CWpCAVoGxCA+YsTMb82fTOmGYMkZOAfQsvIV2v6DC8eJrSa+c0yCOTa3tirlCkhBfB08f8U2iEPS+Gu3bECQQCrG7O0gYmFL2RX1O+37ovyyHTbst4s4xbLW4jLzbSoimL235lCdIC+fllEEP96wPAiqo6dzmdH8KsGmVozsVRbAkB0ME8AZjp/9Pt8TDXD5LHzo8mlruUdnCBcIo5TMoRG2+3hRe1dHPonNCjgbdZCoyqjsWOiPfnQ2Brigvs7J4xhAkBGRiZUKC92x7QKbqXVgN9xYuq7oIanIM0nz/wq190uq0dh5Qtow7hshC/dSK3kmIEHe8z++tpoLWvQVgM538apAkBoSNfaTkDZhFavuiVl6L8cWCoDcJBItip8wKQhXwHp0O3HLg10OEd14M58ooNfpgt+8D8/8/2OOFaR0HzA+2Dm";

    // private KeyPair generateKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException {
    //     KeyPairGenerator keyPairGenerator  = KeyPairGenerator.getInstance(ALGORITHM);
    //     keyPairGenerator.initialize(KEY_SIZE);
    //     KeyPair keyPair = keyPairGenerator.generateKeyPair();
    //     return keyPair;
    // }

    /**
     * Encrypts the given data using RSA encryption.
     *
     * @param data The data to encrypt.
     * @return Base64-encoded encrypted data.
     * @throws NoSuchAlgorithmException     If RSA algorithm is not available.
     * @throws NoSuchPaddingException       If padding scheme is not available.
     * @throws InvalidKeyException          If the key is invalid.
     * @throws IllegalBlockSizeException    If the block size is invalid.
     * @throws BadPaddingException          If the padding is bad.
     * @throws UnsupportedEncodingException If the encoding is not supported.
     * @throws InvalidKeySpecException 
     * @throws IllegalArgumentException     If data is null.
     */
    public static String encrypt(String data)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, InvalidKeySpecException {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(PUBLIC_KEY));
        byte[] cipherText = cipher.doFinal(data.getBytes(Constants.MESSAGE_SOURCE_DEFAULT_ENCODING));
        return Base64.getEncoder().encodeToString(cipherText);
    }

    /**
     * Decrypts the given Base64-encoded encrypted data using RSA decryption.
     *
     * @param encryptedData The Base64-encoded encrypted data.
     * @return The decrypted data.
     * @throws NoSuchAlgorithmException     If RSA algorithm is not available.
     * @throws NoSuchPaddingException       If padding scheme is not available.
     * @throws InvalidKeyException          If the key is invalid.
     * @throws IllegalBlockSizeException    If the block size is invalid.
     * @throws BadPaddingException          If the padding is bad.
     * @throws UnsupportedEncodingException If the encoding is not supported.
     * @throws InvalidKeySpecException 
     * @throws IllegalArgumentException     If encryptedData is null.
     */
    public static String decrypt(String encryptedData)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, InvalidKeySpecException {
        if (encryptedData == null) {
            throw new IllegalArgumentException("Encrypted data cannot be null");
        }
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(PRIVATE_KEY));
        byte[] decryptedValue = cipher.doFinal(Base64.getMimeDecoder().decode(encryptedData));
        return new String(decryptedValue, Constants.MESSAGE_SOURCE_DEFAULT_ENCODING);
    }

    /**
     * Converts a Base64-encoded public key into a PublicKey object.
     *
     * @param base64PublicKey The Base64-encoded public key.
     * @return PublicKey object.
     * @throws NoSuchAlgorithmException If RSA algorithm is not available.
     * @throws InvalidKeySpecException  If the key specification is invalid.
     */
    public static PublicKey getPublicKey(String base64PublicKey)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(base64PublicKey.getBytes()));
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * Converts a Base64-encoded private key into a PrivateKey object.
     *
     * @param base64PrivateKey The Base64-encoded private key.
     * @return PrivateKey object.
     * @throws NoSuchAlgorithmException If RSA algorithm is not available.
     * @throws InvalidKeySpecException  If the key specification is invalid.
     */
    public static PrivateKey getPrivateKey(String base64PrivateKey)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(base64PrivateKey.getBytes()));
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        return keyFactory.generatePrivate(keySpec);
    }
}
