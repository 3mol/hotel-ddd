package org.example.payment;

import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {
  @Resource PaymentService paymentService;

  @PostMapping("/booking")
  public void payForBooking(BookingPaymentReq req) {
    paymentService.payForBooking(req);
  }

  @PostMapping("/check-in")
  public void payForCheckIn(CheckInPaymentReq req) {
    paymentService.payForCheckIn(req);
  }

  @GetMapping("/has-unpaid-payment/{orderNumber}")
  public Boolean hasUnpaidPayment(@PathVariable String orderNumber) {
    return paymentService.hasUnpaidPayment(orderNumber);
  }

  @GetMapping("/payment-id/{id}")
  PaymentResp getById(@PathVariable Long id) {
    return paymentService.getById(id);
  }
}
