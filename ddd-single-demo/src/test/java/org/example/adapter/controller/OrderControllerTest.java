package org.example.adapter.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cn.hutool.core.date.DateUtil;
import java.util.List;
import javax.annotation.Resource;
import lombok.SneakyThrows;
import org.example.application.order.BookingReq;
import org.example.application.order.OrderResp;
import org.example.application.room.AppendRoomReq;
import org.example.application.room.RoomResp;
import org.example.application.room.RoomService;
import org.example.domain.order.PayStatus;
import org.example.domain.order.RoomId;
import org.example.domain.payment.Payment;
import org.example.domain.payment.PaymentRepository;
import org.example.domain.room.RoomType;
import org.example.domain.user.Customer;
import org.example.domain.user.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

class OrderControllerTest extends BaseControllerTest {
  @Resource PaymentRepository paymentRepository;
  @Resource RoomService roomService;
  private RoomResp roomResp;

  @BeforeEach
  void setUp() {
    System.out.println("web 端口：" + port);
    final AppendRoomReq appendRoomReq = new AppendRoomReq();
    appendRoomReq.setType(RoomType.SINGLE);
    appendRoomReq.setPrice(100.0D);
    appendRoomReq.setNumber("401");
    roomResp = roomService.appendRoom(appendRoomReq);
  }

  @Test
  @SneakyThrows
  void booking() {
    final BookingReq bookingReq = new BookingReq();
    final RoomId roomId = new RoomId();
    roomId.setId(1L);
    roomId.setNumber("401");
    bookingReq.setRoomId(roomId);
    final Customer customer = new Customer();
    customer.setPhone("10086");
    customer.setName("张三");
    customer.setIdCard("88888888");
    bookingReq.setCustomer(customer);
    bookingReq.setPhone("10086");
    // 预定周五
    bookingReq.setCheckInDate(DateUtil.parseDate("2023-04-28"));
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

    Thread.sleep(200);
    // 事件会产生支付信息，20元的待支付信息
    final List<Payment> payments = paymentRepository.findAllBySerialNumber(resp.getNumber());
    assertEquals(PayStatus.UNPAID, payments.get(0).getStatus());
    // 100 为原价，周五打五折，收20%的订金 => 10元
    final double expected = 100 * 0.5 * 0.2;
    assertEquals(expected, payments.get(0).getAmount());
  }
}
