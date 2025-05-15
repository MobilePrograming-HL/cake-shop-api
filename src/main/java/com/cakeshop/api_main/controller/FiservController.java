package com.cakeshop.api_main.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/fiserv")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FiservController {

    @GetMapping("/payment-success")
    public ResponseEntity<Void> success(HttpServletResponse response) {
        response.setHeader("ngrok-skip-browser-warning", "true");
        URI uri = URI.create("myapp://payment-result/success");
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(uri)
                .build();
    }

    @GetMapping("/payment-fail")
    public ResponseEntity<Void> fail(HttpServletResponse response) {
        response.setHeader("ngrok-skip-browser-warning", "true");
        URI uri = URI.create("myapp://payment-result/fail");
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(uri)
                .build();
    }
}
