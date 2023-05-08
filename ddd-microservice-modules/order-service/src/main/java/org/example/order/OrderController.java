package org.example.order;

import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {
  @Resource OrderService orderService;
  // 用户下预约单
  @PostMapping("/booking")
  public OrderResp booking(@RequestBody BookingReq bookingReq) {
    return orderService.booking(bookingReq);
  }

  @PostMapping("/cancel")
  public OrderResp cancel(CancelReq req) {
    return orderService.cancel(req);
  }

  @PostMapping("/check-in")
  public OrderResp checkIn(CheckInReq req) {
    return orderService.checkIn(req);
  }

  @PostMapping("/check-out")
  public OrderResp checkOut(CheckOutReq req) {
    return orderService.checkOut(req);
  }

  @GetMapping("/order-number/{orderNumber}")
  public OrderResp findByNumber(@PathVariable(value = "orderNumber") String orderNumber) {
    return orderService.findByNumber(orderNumber);
  }

  @GetMapping("/order-id/{orderId}")
  OrderResp getById(@PathVariable(value = "orderId") Long orderId) {
    return orderService.getById(orderId);
  }
}
