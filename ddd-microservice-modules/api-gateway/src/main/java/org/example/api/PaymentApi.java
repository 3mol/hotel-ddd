package org.example.api;

import javax.annotation.Resource;
import org.example.payment.BookingPaymentReq;
import org.example.payment.CheckInPaymentReq;
import org.example.payment.PaymentRemoteService;
import org.example.payment.PaymentResp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/payments")
public class PaymentApi {
  @Resource PaymentRemoteService paymentRemoteService;

  @PostMapping("/booking")
  public void payForBooking(BookingPaymentReq req) {
    paymentRemoteService.payForBooking(req);
  }

  @PostMapping("/check-in")
  public void payForCheckIn(CheckInPaymentReq req) {
    paymentRemoteService.payForCheckIn(req);
  }

  @GetMapping("/has-unpaid-payment/{orderNumber}")
  public Boolean hasUnpaidPayment(@PathVariable String orderNumber) {
    return paymentRemoteService.hasUnpaidPayment(orderNumber);
  }

  @GetMapping("/payment-id/{id}")
  public PaymentResp getById(@PathVariable Long id) {
    return paymentRemoteService.getById(id);
  }
}
