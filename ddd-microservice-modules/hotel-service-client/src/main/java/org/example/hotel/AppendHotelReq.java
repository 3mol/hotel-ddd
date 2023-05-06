package org.example.hotel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppendHotelReq {
  private String name;
  private String address;
  private String phone;
  private String description;
}
