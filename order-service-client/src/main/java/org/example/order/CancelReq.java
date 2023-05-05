package org.example.order;

import lombok.Data;

@Data
public class CancelReq {
  private OrderId orderId;
}
