package org.example.room;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
    name = "RoomRemoteService",
    url = "${room-service-url:http://room-service:8080}",
    path = "/rooms")
public interface RoomRemoteService {
  @GetMapping(value = "/{id}")
  RoomResp getById(@PathVariable(value = "id") Long id);

  @GetMapping(value = "/can-be-reserved/{id}")
  Boolean canBeReserved(@PathVariable(value = "id") Long id);

  @GetMapping(value = "/discount-price/{id}")
  double getDiscountPrice(@PathVariable(value = "id") Long id);
}
