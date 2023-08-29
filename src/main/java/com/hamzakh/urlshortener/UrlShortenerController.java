package com.hamzakh.urlshortener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@RequestMapping("/rest/url")
@RestController
public class UrlShortenerController {

    @Autowired
    UrlShortenerService urlShortenerService;

    @GetMapping("/{hash}")
    public ResponseEntity<HttpStatus> resolve(@PathVariable String hash) {
        return urlShortenerService.retrieveAndRedirect(hash);
    }

    @PostMapping
    public String shorten(@RequestBody String url) throws NoSuchAlgorithmException {
        return urlShortenerService.cacheAndShorten(url);
    }
}
