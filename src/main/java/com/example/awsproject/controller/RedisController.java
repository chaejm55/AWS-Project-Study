package com.example.awsproject.controller;

import com.example.awsproject.dto.MessageDto;
import com.example.awsproject.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RedisController {
    private final RedisService redisService;
    private final String LIST_KEY = "messages";
    // endpoints
    @PostMapping("/messages")
    public ResponseEntity<String> setRedis(@RequestBody MessageDto messageDto) {
        redisService.lPush(LIST_KEY, messageDto.getMessage());
        return ResponseEntity.ok().body("Message added to list");
    }

    @GetMapping("/messages")
    public ResponseEntity<List<String>> getRedis() {
        return ResponseEntity.ok().body(redisService.lRange(LIST_KEY, 0, -1));
    }
}

