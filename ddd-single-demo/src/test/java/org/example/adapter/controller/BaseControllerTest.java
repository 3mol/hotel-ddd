package org.example.adapter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.annotation.Resource;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class BaseControllerTest {
  @Resource ObjectMapper objectMapper;
  @Resource MockMvc mockMvc;
}
