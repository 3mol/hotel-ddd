package org.example.adapter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.annotation.Resource;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class BaseControllerTest {
  @Resource ObjectMapper objectMapper;
  @Resource MockMvc mockMvc;
  @LocalServerPort int port;
}
