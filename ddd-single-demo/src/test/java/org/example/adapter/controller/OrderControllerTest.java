package org.example.adapter.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;
import javax.annotation.Resource;
import lombok.SneakyThrows;
import org.example.application.order.BookingReq;
import org.example.application.order.OrderResp;
import org.example.domain.order.OrderRepository;
import org.example.domain.order.RoomId;
import org.example.domain.user.Customer;
import org.example.domain.user.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

class OrderControllerTest extends BaseControllerTest {
  @Resource OrderRepository orderRepository;

  @BeforeEach
  void setUp() {
    System.out.println("web 端口：" + port);
  }

  @Test
  @SneakyThrows
  void booking() {
    final BookingReq bookingReq = new BookingReq();
    final RoomId roomId = new RoomId();
    roomId.setId(1L);
    roomId.setNumber("234");
    bookingReq.setRoomId(roomId);
    final Customer customer = new Customer();
    customer.setPhone("10086");
    customer.setName("张三");
    customer.setIdCard("88888888");
    bookingReq.setCustomer(customer);
    bookingReq.setPhone("10086");
    bookingReq.setCheckInDate(new Date());
    bookingReq.setUserId(new UserId(1L, "王二"));
    final String content = objectMapper.writeValueAsString(bookingReq);
    MvcResult result =
        mockMvc
            .perform(
                post("/orders/booking").contentType(MediaType.APPLICATION_JSON).content(content))
            .andExpect(status().isOk())
            .andReturn();
    final OrderResp resp =
        objectMapper.readValue(result.getResponse().getContentAsString(), OrderResp.class);
    assertNotNull(resp.getId());
    assertNotNull(resp.getNumber());
    assertNotNull(resp.getRoomId());
  }
}
