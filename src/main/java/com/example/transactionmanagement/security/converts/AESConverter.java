package com.example.transactionmanagement.security.converts;

import java.security.GeneralSecurityException;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.example.transactionmanagement.utils.AESUtil;
import com.example.transactionmanagement.utils.Constants;

/**
 * Attribute converter for encrypting and decrypting String attributes using AES encryption.
 */
@Converter
public class AESConverter implements AttributeConverter<String, String> {

    /**
     * Converts the attribute value to be stored in the database by encrypting it with AES.
     *
     * @param attribute The attribute value to encrypt.
     * @return The encrypted value.
     * @throws IllegalArgumentException If the attribute is null.
     * @throws RuntimeException         If encryption fails.
     */
    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) {
            throw new IllegalArgumentException(Constants.ATTRIBUTE_NOT_NULL);
        }
        try {
            return AESUtil.encrypt(attribute);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(Constants.ERROR_DECRYPTING_ACCOUNT_NUMBER, e);
        } catch (Exception e) {
            throw new RuntimeException(Constants.ERROR_DECRYPTING_ACCOUNT_NUMBER, e);
        }
    }

    /**
     * Converts the database column value back to the entity attribute by decrypting it with AES.
     *
     * @param dbData The database value to decrypt.
     * @return The decrypted attribute value.
     * @throws IllegalArgumentException If the database value is null.
     * @throws RuntimeException         If decryption fails.
     */
    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            throw new IllegalArgumentException(Constants.ATTRIBUTE_NOT_NULL);
        }
        try {
            return AESUtil.decrypt(dbData);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(Constants.ERROR_DECRYPTING_ACCOUNT_NUMBER, e);
        } catch (Exception e) {
            throw new RuntimeException(Constants.ERROR_DECRYPTING_ACCOUNT_NUMBER, e);
        }
    }
}
