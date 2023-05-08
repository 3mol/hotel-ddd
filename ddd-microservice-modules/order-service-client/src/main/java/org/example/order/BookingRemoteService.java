package org.example.order;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
    name = "BookingRemoteService",
    url = "${order-service-url:http://order-service:8080}",
    path = "/bookings")
public interface BookingRemoteService {
  @GetMapping("{id}")
  Booking findByOrderId(@PathVariable("id") Long id);
}
