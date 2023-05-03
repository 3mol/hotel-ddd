package org.example.client.hotel;

import lombok.Data;
import org.example.domain.hotel.HotelStatus;

@Data
public class HotelResp {
  private Long id;
  private String name;
  private String address;
  private String phone;
  private HotelStatus status;
  private String description;
}
