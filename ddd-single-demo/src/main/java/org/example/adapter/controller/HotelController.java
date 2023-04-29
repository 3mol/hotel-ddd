package org.example.adapter.controller;

import javax.annotation.Resource;
import org.example.application.hotel.HotelService;
import org.example.client.hotel.AppendHotelReq;
import org.example.client.hotel.HotelResp;
import org.example.domain.hotel.Hotel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hotels")
public class HotelController {
  @Resource
  HotelService hotelService;

  // 营业
  @PutMapping("/open/{id}")
  public void open(@PathVariable(value = "id") Long id) {
    // ...
    hotelService.open(id);
  }

  /**
   * 歇业
   */

  @PutMapping("/close/{id}")
  public void close(Long id) {
    // ...
    hotelService.close(id);
  }

  /**
   * 添加酒店
   *
   * @param hotel ~
   */
  @PostMapping("")
  public HotelResp append(@RequestBody AppendHotelReq hotel) {
    return hotelService.append(hotel);
  }

  // 营业
  @GetMapping("/{id}")
  public HotelResp getOne(@PathVariable(value = "id") Long id) {
    // ...
    return hotelService.getOne(id);
  }
}
