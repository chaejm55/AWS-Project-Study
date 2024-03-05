package com.example.awsproject.controller;

import com.example.awsproject.dto.MessageDto;
import com.example.awsproject.service.RedisService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RedisControllerTest {
    private final String LIST_KEY = "messages";
    private final String LIST_VALUE = "testing with redis";


    @Autowired
    private RedisService redisService;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
        redisService.deleteKey(LIST_KEY);
    }

    @AfterEach
    public void teardown() {
        redisService.deleteKey(LIST_KEY);
    }

    @Test
    @DisplayName("responds with a success message")
    public void setMessages() throws Exception {
        String content = objectMapper.writeValueAsString(new MessageDto(LIST_VALUE));
        mockMvc.perform(post("/messages")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Message added to list"));
    }

    @Test
    @DisplayName("responds with all messages")
    public void getMessages() throws Exception {
        redisService.lPush(LIST_KEY, "msg1");
        redisService.lPush(LIST_KEY, "msg2");
        mockMvc.perform(get("/messages"))
                .andExpect(status().isOk())
                .andExpect(content().json("[\"msg2\", \"msg1\"]"));
    }
}
