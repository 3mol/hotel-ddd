package org.example.order;

public interface OrderRemoteService {
  OrderResp findByNumber(String orderNumber);
}
