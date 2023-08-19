package com.benson.bensonservice.service;

import org.springframework.http.ResponseEntity;

public interface LineApiService {
    String authNotify();

    ResponseEntity<String> tokenNotify(String code);
}
