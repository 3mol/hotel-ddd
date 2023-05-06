package org.example.hotel;

import lombok.Data;

@Data
public class HotelResp {
  private Long id;
  private String name;
  private String address;
  private String phone;
  private HotelStatus status;
  private String description;
}
