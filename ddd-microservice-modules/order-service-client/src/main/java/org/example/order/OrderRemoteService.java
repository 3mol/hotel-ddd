package org.example.order;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    name = "OrderRemoteService",
    url = "${order-service-url:http://order-service:8080}",
    path = "/orders")
public interface OrderRemoteService {
  @PostMapping("/booking")
  OrderResp booking(@RequestBody BookingReq bookingReq);

  @PostMapping("/cancel")
  OrderResp cancel(CancelReq req);

  @PostMapping("/check-in")
  OrderResp checkIn(CheckInReq req);

  @PostMapping("/check-out")
  OrderResp checkOut(CheckOutReq req);

  @GetMapping("/order-number/{orderNumber}")
  OrderResp findByNumber(@PathVariable(value = "orderNumber") String orderNumber);

  @GetMapping("/order-id/{orderId}")
  OrderResp getById(Long orderId);
}
