package org.example.application.order;

import lombok.Data;
import org.example.domain.order.OrderId;

@Data
public class CheckOutReq {
  private OrderId orderId;
}
