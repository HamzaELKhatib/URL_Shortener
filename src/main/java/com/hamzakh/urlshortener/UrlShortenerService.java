package com.hamzakh.urlshortener;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class UrlShortenerService {

    @Autowired
    StringRedisTemplate redisTemplate;

    public String hash(final String url) throws NoSuchAlgorithmException {

        var urlValidator = new UrlValidator(new String[]{"http", "https"});

        if (!urlValidator.isValid(url)) {
            throw new RuntimeException("URL Invalid: " + url);
        }

        var digest = MessageDigest.getInstance("SHA-256");

        var bytes = digest.digest(url.getBytes());

        return String.format("%32x", new BigInteger(1, bytes));


    }

    public String cacheAndShorten(final String url) throws NoSuchAlgorithmException {

        var hash = this.hash(url).substring(0, 6);

        redisTemplate.opsForValue().set(hash, url);

        return hash;
    }

    public ResponseEntity<HttpStatus> retrieveAndRedirect(final String hash) {

        final String url = redisTemplate.opsForValue().get(hash);

        if (url == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity
                .status(HttpStatus.MOVED_PERMANENTLY)
                .location(URI.create(url))
                .header(HttpHeaders.CONNECTION, "close")
                .build();
    }
}
