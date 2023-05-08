package org.example.api;

import javax.annotation.Resource;
import org.example.order.BookingReq;
import org.example.order.CancelReq;
import org.example.order.CheckInReq;
import org.example.order.CheckOutReq;
import org.example.order.OrderRemoteService;
import org.example.order.OrderResp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/orders")
public class OrderApi {
  @Resource OrderRemoteService orderRemoteService;

  @PostMapping("/booking")
  OrderResp booking(@RequestBody BookingReq req) {
    return orderRemoteService.booking(req);
  }

  @PostMapping("/cancel")
  OrderResp cancel(CancelReq req) {
    return orderRemoteService.cancel(req);
  }

  @PostMapping("/check-in")
  OrderResp checkIn(CheckInReq req) {
    return orderRemoteService.checkIn(req);
  }

  @PostMapping("/check-out")
  OrderResp checkOut(CheckOutReq req) {
    return orderRemoteService.checkOut(req);
  }

  @GetMapping("/order-number/{orderNumber}")
  OrderResp findByNumber(@PathVariable(value = "orderNumber") String orderNumber) {
    return orderRemoteService.findByNumber(orderNumber);
  }

  @GetMapping("/order-id/{orderId}")
  OrderResp getById(@PathVariable(value = "orderId") Long orderId) {
    return orderRemoteService.getById(orderId);
  }
}
