package org.example.adapter.controller;

import javax.annotation.Resource;
import org.example.application.payment.BookingPaymentReq;
import org.example.application.payment.PaymentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {
  @Resource PaymentService service;

  // 用户支付预定款
  @PostMapping("/booking")
  public void payForBooking(@RequestBody BookingPaymentReq req) {
    service.payForBooking(req);
  }
}
