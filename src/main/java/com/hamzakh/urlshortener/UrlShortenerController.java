package com.hamzakh.urlshortener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.NoSuchAlgorithmException;

@RequestMapping("/rest/url")
@RestController
public class UrlShortenerController {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    UrlShortenerService urlShortenerService;

    @GetMapping("/{hash}")
    public ResponseEntity<HttpStatus> resolve(@PathVariable String hash) {

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

    @PostMapping
    public String shorten(@RequestBody String url) throws NoSuchAlgorithmException {
        var hash = urlShortenerService.hash(url).substring(0, 6);
        redisTemplate.opsForValue().set(hash, url);
        return hash;
    }
}
