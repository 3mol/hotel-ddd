package org.example.adapter.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;
import javax.annotation.Resource;
import lombok.SneakyThrows;
import org.example.application.payment.BookingPaymentReq;
import org.example.domain.order.Order;
import org.example.domain.order.OrderRepository;
import org.example.domain.order.OrderStatus;
import org.example.domain.order.PayMethod;
import org.example.domain.order.PayStatus;
import org.example.domain.order.PayType;
import org.example.domain.order.RoomId;
import org.example.domain.payment.Payment;
import org.example.domain.payment.PaymentId;
import org.example.domain.payment.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

class PaymentControllerTest extends BaseControllerTest {
  @Resource PaymentRepository paymentRepository;
  @Resource OrderRepository orderRepository;

  @Test
  @SneakyThrows
  void payForBooking() {
    final Payment payment = getPayment();
    final Order order = getOrder();
    paymentRepository.save(payment);
    orderRepository.save(order);

    final BookingPaymentReq req = new BookingPaymentReq();
    req.setPaymentId(new PaymentId(1L, "OrderNumber"));
    req.setThirdPartySerialNumber("test-number");
    req.setPayMethod(PayMethod.WECHAT);
    final String content = objectMapper.writeValueAsString(req);
    final MvcResult mvcResult =
        mockMvc
            .perform(
                post("/payments/booking").contentType(MediaType.APPLICATION_JSON).content(content))
            .andExpect(status().isOk())
            .andReturn();
    System.out.println(mvcResult);
    // 等待异步事件完成
    Thread.sleep(1000);
    assertEquals(PayStatus.PAID, paymentRepository.findById(payment.getId()).get().getStatus());
    assertEquals(OrderStatus.RESERVED, orderRepository.findById(order.getId()).get().getStatus());
  }

  private static Order getOrder() {
    final Order order = new Order();
    order.setId(1L);
    order.setNumber("OrderNumber");
    order.setRoomId(new RoomId());
    order.setStatus(OrderStatus.PENDING);
    order.setCheckInTime(null);
    order.setCheckOutTime(null);
    return order;
  }

  private static Payment getPayment() {
    final Payment payment = new Payment();
    payment.setId(1L);
    payment.setMethod(PayMethod.WECHAT);
    payment.setSerialNumber("OrderNumber");
    payment.setThirdPartySerialNumber(null);
    payment.setType(PayType.DEPOSIT);
    payment.setStatus(PayStatus.UNPAID);
    payment.setAmount(10.0D);
    payment.setCreatedAt(new Date());
    payment.setPaidAt(null);
    return payment;
  }
}
