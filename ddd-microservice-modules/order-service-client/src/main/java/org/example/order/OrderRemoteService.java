package org.example.order;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
    name = "OrderRemoteService",
    url = "${order-service-url:http://order-service:8080}",
    path = "/orders")
public interface OrderRemoteService {

  @GetMapping("{orderNumber}")
  OrderResp findByNumber(@PathVariable(value = "orderNumber") String orderNumber);
}
