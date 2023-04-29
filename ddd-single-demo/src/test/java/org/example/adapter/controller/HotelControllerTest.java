package org.example.adapter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.annotation.Resource;
import lombok.SneakyThrows;
import org.example.client.hotel.AppendHotelReq;
import org.example.client.hotel.HotelResp;
import org.example.domain.hotel.HotelStatus;
import org.example.domain.user.User;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class HotelControllerTest {
  @Resource
  ObjectMapper objectMapper;
  @Resource
  MockMvc mockMvc;

  @Test
  @SneakyThrows
  void append() {
    var hotelReq = new AppendHotelReq("test", "address", "10086", "good hotel");
    final String content = objectMapper.writeValueAsString(hotelReq);
    MvcResult result =
       mockMvc
          .perform(post("/hotels").contentType(MediaType.APPLICATION_JSON).content(content))
          .andExpect(status().isOk())
          .andReturn();
    final HotelResp hotelResp = objectMapper.readValue(result.getResponse().getContentAsString(), HotelResp.class);
    assertNotNull(hotelResp.getId());
  }

  @Test
  @SneakyThrows
  void open() {
    var hotelReq = new AppendHotelReq("test", "address", "10086", "good hotel");
    final String content = objectMapper.writeValueAsString(hotelReq);
    MvcResult result =
       mockMvc
          .perform(post("/hotels").contentType(MediaType.APPLICATION_JSON).content(content))
          .andExpect(status().isOk())
          .andReturn();
    final HotelResp hotelResp = objectMapper.readValue(result.getResponse().getContentAsString(), HotelResp.class);
    assertNotNull(hotelResp.getId());


    mockMvc
       .perform(put("/hotels/open/"+ hotelResp.getId()))
       .andExpect(status().isOk())
       .andReturn();

    final MvcResult mvcResult = mockMvc
       .perform(get("/hotels/" + hotelResp.getId()).contentType(MediaType.APPLICATION_JSON))
       .andExpect(status().isOk())
       .andReturn();
    final HotelResp hotelResp2 = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), HotelResp.class);
    assertEquals(hotelResp2.getStatus(), HotelStatus.OPEN);
  }
}