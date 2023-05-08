package org.example.hotel;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    name = "HotelRemoteService",
    url = "${hotel-service-url:http://hotel-service:8080}",
    path = "/hotels")
public interface HotelRemoteService {
  @PutMapping("/open/{id}")
  void open(@PathVariable Long id);

  /** 歇业 */
  @PutMapping("/close/{id}")
  void close(@PathVariable Long id);

  /**
   * 添加酒店
   *
   * @param hotel ~
   */
  @PostMapping("")
  HotelResp append(@RequestBody AppendHotelReq hotel);

  @GetMapping("/{id}")
  HotelResp getById(@PathVariable(value = "id") Long id);
}
