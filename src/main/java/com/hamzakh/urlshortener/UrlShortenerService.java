package com.hamzakh.urlshortener;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class UrlShortenerService {

    public String hash(String url) throws NoSuchAlgorithmException {

        var urlValidator = new UrlValidator(new String[]{"http", "https"});

        if (!urlValidator.isValid(url)) {
            throw new RuntimeException("URL Invalid: " + url);
        }

        var digest = MessageDigest.getInstance("SHA-256");

        var bytes = digest.digest(url.getBytes());

        var hash = String.format("%32x", new BigInteger(1, bytes));

        return hash;


    }
}
