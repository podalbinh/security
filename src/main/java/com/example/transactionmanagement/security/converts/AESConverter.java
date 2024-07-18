package com.example.transactionmanagement.security.converts;

import java.security.GeneralSecurityException;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.example.transactionmanagement.utils.AESUtil;

@Converter
public class AESConverter implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) {
            throw new IllegalArgumentException("Attribute must not be null");
        }
        try {
            return AESUtil.encrypt(attribute);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("Error encrypting account number", e);
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting account number", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            throw new IllegalArgumentException("Attribute must not be null");
        }
        try {
            return AESUtil.decrypt(dbData);
        }
         catch (GeneralSecurityException e) {
            throw new RuntimeException("Error decrypting account number", e);
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting account number", e);
        }
    }
}