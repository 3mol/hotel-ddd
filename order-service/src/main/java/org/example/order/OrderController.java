package org.example.order;

import javax.annotation.Resource;
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
}
