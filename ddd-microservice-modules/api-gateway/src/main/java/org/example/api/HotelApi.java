package org.example.api;

import javax.annotation.Resource;
import org.example.hotel.AppendHotelReq;
import org.example.hotel.HotelRemoteService;
import org.example.hotel.HotelResp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/hotels")
public class HotelApi {
  @Resource HotelRemoteService hotelRemoteService;

  @PutMapping("/open/{id}")
  void open(@PathVariable Long id) {
    hotelRemoteService.open(id);
  }

  /** 歇业 */
  @PutMapping("/close/{id}")
  void close(@PathVariable Long id) {
    hotelRemoteService.close(id);
  }

  /**
   * 添加酒店
   *
   * @param hotel ~
   */
  @PostMapping("")
  HotelResp append(@RequestBody AppendHotelReq hotel) {
    return hotelRemoteService.append(hotel);
  }

  // 营业
  @GetMapping("/{id}")
  HotelResp getById(@PathVariable(value = "id") Long id) {
    return hotelRemoteService.getById(id);
  }
}
