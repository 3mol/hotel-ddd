package org.example.payment;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
    name = "PaymentRemoteService",
    url = "${payment-service-url:http://payment-service:8080}",
    path = "/payments")
public interface PaymentRemoteService {
  @GetMapping(value = "/hasUnpaidPayment")
  Boolean hasUnpaidPayment(String number);
}
