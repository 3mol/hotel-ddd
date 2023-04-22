package org.example.event;

import org.example.domain.order.PayEvent;

public class PayEventHandler {
  public void handle(PayEvent event) {
    // 1. 根据订单号查询订单
    System.out.println("PayEventHandler.handle = event:" + event);
  }
}
