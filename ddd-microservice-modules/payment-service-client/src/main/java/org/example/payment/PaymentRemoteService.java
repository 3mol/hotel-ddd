package org.example.payment;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
    name = "PaymentRemoteService",
    url = "${payment-service-url:http://payment-service:8080}",
    path = "/payments")
public interface PaymentRemoteService {
  @PostMapping("/booking")
  public void payForBooking(BookingPaymentReq req);

  @PostMapping("/check-in")
  public void payForCheckIn(CheckInPaymentReq req);

  @GetMapping("/has-unpaid-payment/{orderNumber}")
  public Boolean hasUnpaidPayment(@PathVariable String orderNumber);

  @GetMapping("/payment-id/{id}")
  PaymentResp getById(@PathVariable Long id);
}
