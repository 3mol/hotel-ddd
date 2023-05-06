package org.example.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.base.Discount;
import org.example.hotel.HotelId;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppendRoomReq {

  private HotelId hotelId;
  private RoomType type; // 类型 [单人间、双人间]
  private Double price; // 价格
  private String number; // 房间号
  private Discount discount; // 折扣
}
