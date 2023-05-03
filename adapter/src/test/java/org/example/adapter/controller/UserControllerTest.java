package org.example.adapter.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.annotation.Resource;
import org.example.adapter.dao.UserDao;
import org.example.domain.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

  @Resource private MockMvc mockMvc;

  @Resource private UserDao userDao;
  @Resource ObjectMapper objectMapper;

  @Test
  public void test() throws Exception {
    // add user
    User user = new User(null, "test", "password");
    final String content = objectMapper.writeValueAsString(user);
    MvcResult result =
        mockMvc
            .perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(content))
            .andExpect(status().isOk())
            .andReturn();
    user = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);
    assertNotNull(user.getId());
    // get user
    mockMvc
        .perform(MockMvcRequestBuilders.get("/users/" + user.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("test"))
        .andExpect(jsonPath("$.password").value("password"))
        .andReturn();
    // update user
    user.setName("new_name");
    mockMvc
        .perform(
            put("/users/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("new_name"))
        .andExpect(jsonPath("$.password").value("password"))
        .andReturn();
    // delete user
    mockMvc.perform(delete("/users/" + user.getId())).andExpect(status().isOk());
    assertFalse(userDao.findById(user.getId()).isPresent());
  }
}
